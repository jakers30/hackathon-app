# _runapp.ps1 - one-shot launcher used to "run the app":
#   1) start the Ktor AI proxy (background, waits for GET /health)
#   2) build + install + launch com.raite.studyroom on the USB device
# Everything is appended to %USERPROFILE%\AndroidBuild\_runapp.log so the run
# can be followed without holding a foreground shell open.
$ErrorActionPreference = 'Continue'
$root = 'C:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$out  = Join-Path $env:USERPROFILE 'AndroidBuild'
New-Item -ItemType Directory -Force -Path $out | Out-Null
$log  = Join-Path $out '_runapp.log'
function Log($m) {
    $line = ('[{0}] {1}' -f (Get-Date -Format 'HH:mm:ss'), $m)
    Add-Content -Path $log -Value $line -Encoding UTF8
    Write-Host $line
}
Set-Content -Path $log -Value ('[{0}] === run-the-app start ===' -f (Get-Date -Format 'HH:mm:ss')) -Encoding UTF8

Log 'STEP 1/2  start AI proxy (start-ai-proxy.ps1)'
& powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $root 'start-ai-proxy.ps1') *>> $log
Log ("STEP 1/2  proxy step exit=$LASTEXITCODE")

Log 'STEP 2/2  build + install + launch on USB device (run-demo-usb.ps1)'
& powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $root 'run-demo-usb.ps1') *>> $log
Log ("STEP 2/2  app step exit=$LASTEXITCODE")

Log '=== run-the-app finished ==='
