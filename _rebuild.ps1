# _rebuild.ps1 - ONE detached pass that (1) syncs the OneDrive source into the
# non-OneDrive mirror Gradle can build, (2) compiles + unit-tests the ai-proxy
# (this is what resolves PDFBox and proves FileText parses a PDF), (3) restarts the
# proxy so the phone talks to the new code.
#
# Progress goes to _rebuild_progress.txt (append-only, so it can be read while this
# script still runs). Gradle output goes to _rebuild.log.
$ErrorActionPreference = 'Continue'

$root = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$prog = Join-Path $root '_rebuild_progress.txt'
$log  = Join-Path $root '_rebuild.log'

function Say($m) {
    Add-Content -Path $prog -Value ((Get-Date).ToString('HH:mm:ss') + '  ' + $m)
}

$env:JAVA_HOME        = 'C:\Users\PREDATOR\.jdks\jdk-21.0.12.1+1'
$env:ANDROID_HOME     = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk'
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME

$gradle = 'C:\Users\PREDATOR\.gradle\wrapper\dists\gradle-8.9-bin\90cnw93cvbtalezasaz0blq0a\gradle-8.9\bin\gradle.bat'
$src    = Join-Path $root 'hackathon-app'
$dst    = Join-Path $env:USERPROFILE 'AndroidBuild\hackathon-app'
$proxy  = Join-Path $dst 'ai-proxy'

Set-Content -Path $prog -Value ('start ' + (Get-Date).ToString('HH:mm:ss'))
Set-Content -Path $log  -Value ''

# ---- 1. source -> mirror -----------------------------------------------------
Say '1/4 sync source -> mirror'
robocopy $src $dst /E /XD build .gradle .idea .git /NFL /NDL /NJH /NJS /R:1 /W:1 | Out-Null
Say ("    robocopy exit " + $LASTEXITCODE + " (0-7 = ok)")

# ---- 2. stop the running proxy (it holds the mirror's build dir) -------------
Say '2/4 stop proxy listening on 8080'
$listen = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
if ($listen) {
    foreach ($pid in ($listen.OwningProcess | Select-Object -Unique)) {
        Say "    killing PID $pid"
        Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
    }
    Start-Sleep -Seconds 4
} else {
    Say '    nothing listening'
}

# ---- 3. compile + unit-test the proxy (resolves PDFBox) ----------------------
Say '3/4 gradle -p ai-proxy test'
& $gradle -p $proxy test --console=plain *>> $log
$testExit = $LASTEXITCODE
Say ("    test exit " + $testExit)

# ---- 4. restart the proxy ---------------------------------------------------
Say '4/4 start-ai-proxy.ps1'
& powershell.exe -ExecutionPolicy Bypass -NoProfile -File (Join-Path $root 'start-ai-proxy.ps1') *>> $log
Say ("    proxy start exit " + $LASTEXITCODE)

$health = & curl.exe -s -m 5 http://127.0.0.1:8080/health
Say ("    /health => '" + $health + "'")
Say "DONE"
