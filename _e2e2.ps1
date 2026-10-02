# _e2e2.ps1 - one careful pass:
#   1. kill the stalled _runapp.ps1 orchestrator (its STEP 1 never returned)
#   2. list adb devices (rule out an emulator as the caller)
#   3. mark the current proxy log length so a later call can be attributed
#   4. dump the on-screen UI tree (text + bounds) so taps can be aimed exactly
$ErrorActionPreference = 'Continue'
$adb  = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk\platform-tools\adb.exe'
$ser  = 'a61d9c1b'
$out  = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon\_e2e2.txt'
$log  = Join-Path $env:USERPROFILE 'AndroidBuild\_proxy.log'
$lines = New-Object System.Collections.Generic.List[string]
function Say($t) { $lines.Add([string]$t); $lines | Set-Content -LiteralPath $out -Encoding UTF8; Write-Host $t }

Say ("=== _e2e2 @ {0:HH:mm:ss} ===" -f (Get-Date))

Say '--- 1. stale orchestrator ---'
$p = Get-Process -Id 48856 -ErrorAction SilentlyContinue
if ($p) { Say ("killing PID 48856 ({0}) started {1:HH:mm:ss}" -f $p.ProcessName, $p.StartTime); Stop-Process -Id 48856 -Force -ErrorAction SilentlyContinue; Start-Sleep -Seconds 2 }
Say ("PID 48856 still present: " + [bool](Get-Process -Id 48856 -ErrorAction SilentlyContinue))

Say '--- 2. devices (an emulator here would confuse attribution) ---'
Say ((& $adb devices -l 2>&1) -join "`n")
Say ('adb reverse: ' + ((& $adb -s $ser reverse --list 2>&1) -join ' | '))

Say '--- 3. proxy log mark ---'
if (Test-Path $log) {
    $info = Get-Item $log
    $cnt  = (Get-Content $log).Count
    Say ("proxy log: {0} lines, {1} bytes, last write {2:HH:mm:ss}" -f $cnt, $info.Length, $info.LastWriteTime)
} else { Say 'proxy log MISSING' }

Say '--- 4. UI tree ---'
& $adb -s $ser shell uiautomator dump /sdcard/ui1.xml 2>&1 | ForEach-Object { Say $_ }
& $adb -s $ser pull /sdcard/ui1.xml 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon\_ui1.xml' 2>&1 | ForEach-Object { Say $_ }
$uiPath = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon\_ui1.xml'
if (Test-Path $uiPath) {
    try {
        [xml]$x = Get-Content -LiteralPath $uiPath
        $nodes = $x.SelectNodes('//node')
        Say ("ui nodes: " + $nodes.Count)
        foreach ($n in $nodes) {
            $t = [string]$n.text
            $d = [string]$n.'content-desc'
            if ([string]::IsNullOrWhiteSpace($t) -and [string]::IsNullOrWhiteSpace($d)) { continue }
            Say ("  [{0}] text='{1}' desc='{2}' clickable={3} bounds={4}" -f $n.class, $t, $d, $n.clickable, $n.bounds)
        }
    } catch { Say ('UI parse failed: ' + $_.Exception.Message) }
} else { Say 'no _ui1.xml pulled' }

Say '--- done ---'
Say 'ENDDONE'
