# run-on-phone.ps1  -  build com.raite.studyroom and run it on the USB phone.
# Usage:  powershell -ExecutionPolicy Bypass -File run-on-phone.ps1
#
# WHY THE COPY: this project lives inside OneDrive\Desktop. OneDrive converts files into
# "cloud placeholder" reparse points which Gradle cannot snapshot, and it also holds
# handles on freshly written build output -> random "unable to delete / not a regular file"
# failures. Building from a local, non-synced copy avoids this entirely.
# Edit your source in OneDrive as usual; this script syncs it over each run.

$ErrorActionPreference = 'Stop'

$env:JAVA_HOME        = 'C:\Users\PREDATOR\.jdks\jdk-21.0.12.1+1'
$env:ANDROID_HOME     = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk'
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME

$gradle  = 'C:\Users\PREDATOR\.gradle\wrapper\dists\gradle-8.9-bin\90cnw93cvbtalezasaz0blq0a\gradle-8.9\bin\gradle.bat'
$adb     = "$env:ANDROID_HOME\platform-tools\adb.exe"
$pkg     = 'com.raite.studyroom'
$src     = Join-Path $PSScriptRoot 'hackathon-app'
$dst     = Join-Path $env:USERPROFILE 'AndroidBuild\hackathon-app'

Write-Host "== 1/4  sync source -> $dst" -ForegroundColor Cyan
robocopy $src $dst /E /XD build .gradle .idea .git /NFL /NDL /NJH /NJS /R:1 /W:1 | Out-Null

Write-Host "== 2/4  assembleDebug" -ForegroundColor Cyan
& $gradle -p $dst :app:assembleDebug --console=plain
if ($LASTEXITCODE -ne 0) { throw "Gradle build failed (exit $LASTEXITCODE)" }

$apk = Join-Path $dst 'app\build\outputs\apk\debug\app-debug.apk'
if (-not (Test-Path $apk)) { throw "APK not found at $apk" }

Write-Host "== 3/4  install" -ForegroundColor Cyan
# start the adb server detached so it is not killed when this shell exits
Start-Process -FilePath $adb -ArgumentList 'start-server' -WindowStyle Hidden
Start-Sleep -Seconds 2
$serial = (& $adb devices) | Select-String '^\S+\s+device$' | Select-Object -First 1
if (-not $serial) { throw "No authorised device. Plug in the phone and tap 'Allow USB debugging'." }
$dev = ($serial -split '\s+')[0]
Write-Host "   device = $dev"
& $adb -s $dev install -r $apk
if ($LASTEXITCODE -ne 0) { throw "adb install failed. If it says 'unauthorized', accept the USB debugging prompt on the phone." }

Write-Host "== 4/4  launch" -ForegroundColor Cyan
& $adb -s $dev shell monkey -p $pkg -c android.intent.category.LAUNCHER 1 | Out-Null
Write-Host "Done. $pkg is on the phone." -ForegroundColor Green
