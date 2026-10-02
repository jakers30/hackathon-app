# _probe9.ps1 - stability + connectivity probe, all output to probe9.txt (shell streaming is unreliable)
$ErrorActionPreference = 'Continue'
$root = 'C:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$p    = Join-Path $root 'probe9.txt'
$serial = 'a61d9c1b'

# adb is not always on PATH in a fresh non-interactive shell -> resolve explicitly
$adb = @(
    (Join-Path $env:LOCALAPPDATA 'Android\Sdk\platform-tools\adb.exe'),
    (Join-Path $env:USERPROFILE 'AppData\Local\Android\Sdk\platform-tools\adb.exe'),
    'C:\Android\platform-tools\adb.exe'
) | Where-Object { Test-Path $_ } | Select-Object -First 1
if (-not $adb) { $adb = 'adb' }   # last resort: hope it is on PATH

function Say($m) { Add-Content -Path $p -Value $m -Encoding UTF8 }
Set-Content -Path $p -Value ('=== probe9 @ {0} ===' -f (Get-Date -Format 'HH:mm:ss')) -Encoding UTF8

Say '--- 1. interfering launcher processes (powershell/python/java holding scripts) ---'
Get-CimInstance Win32_Process -Filter "Name = 'powershell.exe' OR Name = 'pwsh.exe' OR Name = 'python.exe' OR Name = 'java.exe'" |
    Where-Object { $_.CommandLine -match '_runapp|run-demo-usb|start-ai-proxy|gradle' } |
    ForEach-Object { Say ("PID {0} :: {1}" -f $_.ProcessId, $_.CommandLine) }
Say '(end process list)'

Say '--- 2. adb reverse --list ---'
Say ((& $adb -s $serial reverse --list 2>&1 | Out-String).Trim())
Say '--- 3. adb devices ---'
Say ((& $adb -s $serial devices 2>&1 | Out-String).Trim())

Say '--- 4. proxy health ---'
try {
    $r = Invoke-WebRequest -Uri 'http://127.0.0.1:8080/health' -UseBasicParsing -TimeoutSec 8
    Say ("health HTTP {0} body={1}" -f $r.StatusCode, $r.Content)
} catch { Say ("health FAILED: {0}" -f $_.Exception.Message) }

Say '--- 5. app pid/focus BEFORE 20s soak ---'
Say ('pidof: ' + ((& $adb -s $serial shell pidof com.raite.studyroom 2>&1 | Out-String).Trim()))
Say ((& $adb -s $serial shell dumpsys window 2>&1 | Select-String -Pattern 'mCurrentFocus' | Out-String).Trim())

Say '--- 6. soak 20s ---'
Start-Sleep -Seconds 20

Say '--- 7. app pid/focus AFTER soak ---'
$pidAfter = ((& $adb -s $serial shell pidof com.raite.studyroom 2>&1 | Out-String).Trim())
Say ('pidof after: ' + $pidAfter)
Say ((& $adb -s $serial shell dumpsys window 2>&1 | Select-String -Pattern 'mCurrentFocus' | Out-String).Trim())

Say '--- 8. crash/ANR scan (whole buffer) ---'
Say ((& $adb -s $serial logcat -d 2>&1 | Select-String -Pattern 'FATAL|AndroidRuntime|ANR in|Force finishing|Process com.raite.studyroom .*died|lowmemorykiller|Killing' |
       Select-Object -Last 40 | Out-String).Trim())

Say '--- 9. app logcat chatter (last 60 lines tagged for our pid) ---'
if ($pidAfter -match '^\d+$') {
    Say ((& $adb -s $serial logcat -d --pid $pidAfter 2>&1 | Select-Object -Last 60 | Out-String).Trim())
} else { Say '(no pid -> app not running; skipped app-pid logcat)' }

Say '--- 10. screenshot after soak ---'
& $adb -s $serial shell screencap -p /sdcard/app_screenshot3.png 2>&1 | Out-Null
& $adb -s $serial pull /sdcard/app_screenshot3.png (Join-Path $root 'app_screenshot3.png') 2>&1 | Out-Null
$f = Get-Item (Join-Path $root 'app_screenshot3.png') -ErrorAction SilentlyContinue
if ($f) { Say ('screenshot3 bytes: ' + $f.Length) } else { Say 'screenshot3 MISSING' }

Say 'ENDDONE'

