# _appbuild.ps1 - detached wrapper around run-demo-usb.ps1: rebuilds the APK from
# the synced mirror, installs it on the USB phone, re-creates `adb reverse` and
# launches the app. Progress goes to _appbuild_progress.txt, output to _appbuild.log.
$ErrorActionPreference = 'Continue'

$root = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$prog = Join-Path $root '_appbuild_progress.txt'
$log  = Join-Path $root '_appbuild.log'
$adb  = "$env:ANDROID_HOME\platform-tools\adb.exe"
if (-not (Test-Path $adb)) { $adb = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk\platform-tools\adb.exe' }

Set-Content -Path $prog -Value ('start ' + (Get-Date).ToString('HH:mm:ss'))
Set-Content -Path $log  -Value ''

& powershell.exe -ExecutionPolicy Bypass -NoProfile -File (Join-Path $root 'run-demo-usb.ps1') *>> $log
$code = $LASTEXITCODE
Add-Content -Path $prog -Value ('run-demo-usb exit ' + $code + ' at ' + (Get-Date).ToString('HH:mm:ss'))

Add-Content -Path $prog -Value '--- adb devices -l ---'
& $adb devices -l 2>&1 | Add-Content -Path $prog
Add-Content -Path $prog -Value '--- adb reverse --list ---'
& $adb reverse --list 2>&1 | Add-Content -Path $prog
Add-Content -Path $prog -Value 'DONE'
