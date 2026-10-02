# _restartproxy.ps1 - the last rebuild left the ORIGINAL proxy JVM (PID 34964) alive,
# so start-ai-proxy.ps1 saw port 8080 busy and reused the OLD code. This script kills
# that JVM (and any stale gradle `run` client), waits for the port, syncs the mirror
# and starts the proxy again so the new PDF/priority-independent code is live.
$ErrorActionPreference = 'Continue'

$root = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$prog = Join-Path $root '_restart_progress.txt'
$log  = Join-Path $root '_restart.log'

function Say($m) { Add-Content -Path $prog -Value ((Get-Date).ToString('HH:mm:ss') + '  ' + $m) }

Set-Content -Path $prog -Value ('start ' + (Get-Date).ToString('HH:mm:ss'))
Set-Content -Path $log  -Value ''

Say '1/4 sync source -> mirror'
robocopy (Join-Path $root 'hackathon-app') (Join-Path $env:USERPROFILE 'AndroidBuild\hackathon-app') `
    /E /XD build .gradle .idea .git /NFL /NDL /NJH /NJS /R:1 /W:1 | Out-Null
Say ("    robocopy exit " + $LASTEXITCODE)

Say '2/4 kill stale proxy JVMs'
$killed = @()
Get-CimInstance Win32_Process -Filter "Name = 'java.exe'" | ForEach-Object {
    $cmd = $_.CommandLine
    if ($cmd -and ($cmd -match 'aiproxy' -or $cmd -match 'ApplicationKt') -and $cmd -notmatch 'GradleDaemon') {
        Say ("    stopping PID " + $_.ProcessId)
        Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue
        $killed += $_.ProcessId
    }
}
Say ("    killed: " + ($killed -join ', '))

for ($i = 1; $i -le 20; $i++) {
    $busy = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
    if (-not $busy) { break }
    Say ("    port 8080 still held by PID " + ($busy.OwningProcess -join ','))
    foreach ($p in ($busy.OwningProcess | Select-Object -Unique)) {
        Stop-Process -Id $p -Force -ErrorAction SilentlyContinue
    }
    Start-Sleep -Seconds 3
}

Say '3/4 start-ai-proxy.ps1 (rebuilds the new code and waits for /health)'
& powershell.exe -ExecutionPolicy Bypass -NoProfile -File (Join-Path $root 'start-ai-proxy.ps1') *>> $log
Say ("    start-ai-proxy exit " + $LASTEXITCODE)

Say '4/4 checks'
$health = & curl.exe -s -m 5 http://127.0.0.1:8080/health
Say ("    /health => '" + $health + "'")
$listener = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
Say ("    listening PID(s): " + (($listener.OwningProcess | Select-Object -Unique) -join ','))
Get-Content (Join-Path $env:USERPROFILE 'AndroidBuild\_proxy.log') -Tail 12 -ErrorAction SilentlyContinue |
    ForEach-Object { Say ("    log| " + $_) }
Say 'DONE'
