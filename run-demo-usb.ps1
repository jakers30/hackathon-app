# run-demo-usb.ps1 - build com.raite.studyroom, put it on the USB phone and wire it
# to the local AI proxy WITHOUT needing an Administrator firewall rule.
#
# WHY USB MODE: the phone cannot reach http://192.168.100.76:8080 while Windows
# Firewall has no inbound allow rule for TCP 8080 on the active (Public) network
# profile - and adding that rule needs Administrator. Instead this script
#   * publishes the PC's port 8080 on the phone's own loopback
#         adb reverse tcp:8080 tcp:8080
#   * builds the app with AI_PROXY_URL=http://127.0.0.1:8080
#         (changed in the non-OneDrive mirror copy only - the repo keeps the LAN IP)
# so the phone talks to the proxy through the cable. No admin, no firewall change.
#
# Wi-Fi mode (no cable) once you can run an elevated shell, one time only:
#   New-NetFirewallRule -DisplayName "AI proxy 8080" -Direction Inbound `
#     -Protocol TCP -LocalPort 8080 -Action Allow
# then use run-on-phone.ps1, which keeps AI_PROXY_URL=http://192.168.100.76:8080.
#
# Start the proxy first:  powershell -File start-ai-proxy.ps1
# Usage:  powershell -ExecutionPolicy Bypass -File run-demo-usb.ps1

$ErrorActionPreference = 'Stop'

$env:JAVA_HOME        = 'C:\Users\PREDATOR\.jdks\jdk-21.0.12.1+1'
$env:ANDROID_HOME     = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk'
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME

$gradle   = 'C:\Users\PREDATOR\.gradle\wrapper\dists\gradle-8.9-bin\90cnw93cvbtalezasaz0blq0a\gradle-8.9\bin\gradle.bat'
$adb      = "$env:ANDROID_HOME\platform-tools\adb.exe"
$pkg      = 'com.raite.studyroom'
$src      = Join-Path $PSScriptRoot 'hackathon-app'
$dst      = Join-Path $env:USERPROFILE 'AndroidBuild\hackathon-app'
$proxyUrl = 'http://127.0.0.1:8080'

Write-Host '== 1/5  sync source -> non-OneDrive mirror' -ForegroundColor Cyan
# OneDrive turns files into cloud-placeholder reparse points Gradle cannot snapshot.
robocopy $src $dst /E /XD build .gradle .idea .git /NFL /NDL /NJH /NJS /R:1 /W:1 | Out-Null

Write-Host "== 2/5  mirror AI_PROXY_URL -> $proxyUrl" -ForegroundColor Cyan
$lp = Join-Path $dst 'local.properties'
$txt = Get-Content -LiteralPath $lp
if ($txt -match '^AI_PROXY_URL=') {
    $txt -replace '^AI_PROXY_URL=.*', "AI_PROXY_URL=$proxyUrl" | Set-Content -LiteralPath $lp
} else {
    Add-Content -LiteralPath $lp -Value "AI_PROXY_URL=$proxyUrl"
}

Write-Host '== 3/5  assembleDebug' -ForegroundColor Cyan
& $gradle -p $dst :app:assembleDebug --console=plain
if ($LASTEXITCODE -ne 0) { throw "Gradle build failed (exit $LASTEXITCODE)" }
$apk = Join-Path $dst 'app\build\outputs\apk\debug\app-debug.apk'
if (-not (Test-Path $apk)) { throw "APK not found at $apk" }

Write-Host '== 4/5  adb reverse + install' -ForegroundColor Cyan
Start-Process -FilePath $adb -ArgumentList 'start-server' -WindowStyle Hidden
Start-Sleep -Seconds 2
$serial = (& $adb devices) | Select-String '^\S+\s+device$' | Select-Object -First 1
if (-not $serial) { throw "No authorised device. Plug in the phone and tap 'Allow USB debugging'." }
$dev = ($serial -split '\s+')[0]
Write-Host "   device = $dev"
# Phone's 127.0.0.1:8080 -> this PC's 8080 (works over the cable, no firewall needed).
& $adb -s $dev reverse --remove-all 2>$null
& $adb -s $dev reverse tcp:8080 tcp:8080
if ($LASTEXITCODE -ne 0) { throw 'adb reverse failed - is USB debugging authorised?' }
& $adb -s $dev install -r $apk
if ($LASTEXITCODE -ne 0) { throw "adb install failed. If it says 'unauthorized', accept the prompt on the phone." }

Write-Host '== 5/5  launch' -ForegroundColor Cyan
& $adb -s $dev shell monkey -p $pkg -c android.intent.category.LAUNCHER 1 | Out-Null
Write-Host "Done. $pkg is on $dev and the AI proxy is reachable at $proxyUrl via adb reverse." -ForegroundColor Green
Write-Host 'Demo login demo@studyroom.app / demo1234, room code DEMO01.' -ForegroundColor Green
