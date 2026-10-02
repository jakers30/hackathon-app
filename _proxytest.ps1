# _proxytest.ps1 - boots ai-proxy locally with the real .env, polls GET /health,
# then stops it again. Writes its report to %USERPROFILE%\AndroidBuild\_proxytest.txt
# (kept off OneDrive so Gradle/OneDrive file locks cannot interfere).
$ErrorActionPreference = 'Continue'
$root    = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$report  = Join-Path $env:USERPROFILE 'AndroidBuild\_proxytest.txt'
$lines   = New-Object System.Collections.Generic.List[string]
function Say($t) {
    $t = [string]$t
    $lines.Add($t)
    Add-Content -Path $report -Value $t -Encoding UTF8
    Write-Host $t
}

foreach ($l in (Get-Content (Join-Path $root 'hackathon-app\ai-proxy\.env'))) {
    $t = $l.Trim()
    if ($t -eq '' -or $t.StartsWith('#')) { continue }
    $i = $t.IndexOf('=')
    if ($i -gt 0) { Set-Item -Path ('env:' + $t.Substring(0, $i)) -Value $t.Substring($i + 1) }
}
$lines.Add('env: AI_PROVIDER=' + $env:AI_PROVIDER +
           ' DEEPSEEK_MODEL=' + $env:DEEPSEEK_MODEL +
           ' SUPABASE_URL_set=' + [bool]$env:SUPABASE_URL +
           ' SERVICE_ROLE_set=' + [bool]$env:SUPABASE_SERVICE_ROLE_KEY +
           ' API_KEY_set=' + [bool]$env:DEEPSEEK_API_KEY)

$env:JAVA_HOME        = 'C:\Users\PREDATOR\.jdks\jdk-21.0.12.1+1'
$env:ANDROID_HOME     = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk'
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME

$pre = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
if ($pre) { $lines.Add('port 8080 already listening (PID ' + ($pre.OwningProcess -join ',') + ')') }
else      { $lines.Add('port 8080 was free') }

$gradle = 'C:\Users\PREDATOR\.gradle\wrapper\dists\gradle-8.9-bin\90cnw93cvbtalezasaz0blq0a\gradle-8.9\bin\gradle.bat'
$mirror = Join-Path $env:USERPROFILE 'AndroidBuild\hackathon-app'
$log    = Join-Path $env:USERPROFILE 'AndroidBuild\_proxy.log'
$err    = Join-Path $env:USERPROFILE 'AndroidBuild\_proxy.err'
Remove-Item $log, $err -Force -ErrorAction SilentlyContinue

$proxy = Join-Path $mirror 'ai-proxy'
$lines.Add('launching: gradle -p ' + $proxy + ' run  (ai-proxy is a standalone Gradle build)')
$p = Start-Process -FilePath $gradle -ArgumentList @('-p', $proxy, 'run', '--console=plain') `
     -PassThru -WindowStyle Hidden -RedirectStandardOutput $log -RedirectStandardError $err
$lines.Add('gradle run PID ' + $p.Id)

$lines | Set-Content $report -Encoding UTF8
$ok = $false
for ($i = 1; $i -le 36; $i++) {
    Start-Sleep -Seconds 5
    $h = & curl.exe -s -m 5 http://127.0.0.1:8080/health
    if ($h -eq 'ok') { $ok = $true; $lines.Add("GET /health => 'ok' after ~" + ($i * 5) + 's'); break }
    if ($i % 6 -eq 0) { $lines.Add('  ...still waiting (' + ($i * 5) + 's)'); $lines | Set-Content $report -Encoding UTF8 }
}
if (-not $ok) { $lines.Add('GET /health => no response within 180s') }

$addr = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue |
        Select-Object -ExpandProperty LocalAddress -Unique
Say ('listener address(es): ' + ($addr -join ', '))
$lan = & curl.exe -s -m 10 http://192.168.100.76:8080/health
Say ("GET http://192.168.100.76:8080/health => '" + $lan + "'  (the phone's path)")
try {
    $fw = Get-NetFirewallRule -DisplayName 'Study Room AI proxy 8080' -ErrorAction Stop
    Say 'inbound firewall rule for TCP 8080: present'
} catch {
    Say 'inbound firewall rule for TCP 8080: MISSING -> add it as Administrator, or the phone cannot connect'
}
try {
    $prof = Get-NetConnectionProfile -ErrorAction Stop | Select-Object -ExpandProperty NetworkCategory -Unique
    Say ('network profile(s): ' + ($prof -join ', '))
} catch {
    Say ('network profile lookup failed: ' + $_.Exception.Message)
}

$own = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue |
       Select-Object -ExpandProperty OwningProcess -Unique
foreach ($o in $own) { Stop-Process -Id $o -Force -ErrorAction SilentlyContinue }
Stop-Process -Id $p.Id -Force -ErrorAction SilentlyContinue
$lines.Add('stopped listeners: ' + ($own -join ','))

foreach ($f in @($log, $err)) {
    if (Test-Path $f) {
        $lines.Add('--- ' + (Split-Path $f -Leaf) + ' tail ---')
        foreach ($line in (Get-Content $f -Tail 10)) { $lines.Add($line) }
    } else {
        $lines.Add('--- ' + (Split-Path $f -Leaf) + ' missing ---')
    }
}

$lines | Set-Content $report -Encoding UTF8
Write-Host ($lines -join "`n")
