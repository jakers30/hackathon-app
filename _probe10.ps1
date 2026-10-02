# _probe10.ps1 - FAST verification (no long soak, must finish in a few seconds so the
# shell cannot be torn down mid-run). Confirms: app alive, app in foreground, no crash,
# installed package info, device size, and a screenshot.
$ErrorActionPreference = 'Continue'
$root   = 'C:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$p      = Join-Path $root 'probe10.txt'
$serial = 'a61d9c1b'
$adb    = Join-Path $env:LOCALAPPDATA 'Android\Sdk\platform-tools\adb.exe'

function Say($m) { Add-Content -Path $p -Value $m -Encoding UTF8 }
Set-Content -Path $p -Value ('=== probe10 @ {0} ===' -f (Get-Date -Format 'HH:mm:ss')) -Encoding UTF8

$appPid = ((& $adb -s $serial shell pidof com.raite.studyroom 2>&1 | Out-String).Trim())
Say ('pidof: ' + $appPid)
Say ((& $adb -s $serial shell dumpsys window 2>&1 | Select-String -Pattern 'mCurrentFocus' | Out-String).Trim())
Say ('wm size: ' + ((& $adb -s $serial shell wm size 2>&1 | Out-String).Trim()))
Say '--- installed package (version / lastUpdateTime / codePath) ---'
Say ((& $adb -s $serial shell dumpsys package com.raite.studyroom 2>&1 |
      Select-String -Pattern 'versionName|lastUpdateTime|codePath' | Out-String).Trim())

Say '--- logcat for app pid (last 40 lines, any level) ---'
if ($appPid -match '^\d+$') {
    Say ((& $adb -s $serial logcat -d -t 40 --pid $appPid 2>&1 | Out-String).Trim())
} else { Say '(app not running - skipped)' }

Say '--- FATAL / ANR scan ---'
$bad = (& $adb -s $serial logcat -d 2>&1 | Select-String -Pattern 'FATAL EXCEPTION|ANR in com\.raite|Force finishing' |
        Select-Object -Last 15 | Out-String).Trim()
if ($bad) { Say $bad } else { Say '(none)' }

Say '--- screenshot ---'
& $adb -s $serial shell screencap -p /sdcard/s10.png 2>&1 | Out-Null
& $adb -s $serial pull /sdcard/s10.png (Join-Path $root 'app_screenshot10.png') 2>&1 | Out-Null
$f = Get-Item (Join-Path $root 'app_screenshot10.png') -ErrorAction SilentlyContinue
if ($f) { Say ('screenshot10 bytes: ' + $f.Length) } else { Say 'screenshot10 MISSING' }

Say 'ENDDONE'
