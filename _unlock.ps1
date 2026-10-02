# _unlock.ps1 - the phone is on its lock screen (mCurrentFocus=NotificationShade is
# the keyguard window), so the app UI cannot be dumped. Wake it, swipe up (works when
# no secure lock is set) and report the state. -> _unlock.txt
$ErrorActionPreference = 'Continue'

$root = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$out  = Join-Path $root '_unlock.txt'
$xml  = Join-Path $root '_ui5.xml'
$adb  = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk\platform-tools\adb.exe'

function Say($m) { Add-Content -Path $out -Value $m }
Set-Content -Path $out -Value ('unlock attempt ' + (Get-Date).ToString('HH:mm:ss'))

Say '--- keyguard state ---'
& $adb shell dumpsys window policy 2>&1 | Select-String 'keyguard|Keyguard|mAwake|mScreenOn' |
    Select-Object -First 12 | ForEach-Object { Say $_.ToString().Trim() }

Say '--- wake + swipe up ---'
& $adb shell input keyevent 224 2>&1 | Out-Null
Start-Sleep -Seconds 1
& $adb shell input swipe 540 1900 540 400 250 2>&1 | Out-Null
Start-Sleep -Seconds 3
& $adb shell am start -n com.raite.studyroom/.MainActivity 2>&1 | Out-Null
Start-Sleep -Seconds 5

Say '--- focused window after unlock attempt ---'
& $adb shell dumpsys window 2>&1 | Select-String 'mCurrentFocus' | ForEach-Object { Say $_.ToString().Trim() }

& $adb shell uiautomator dump /sdcard/_ui5.xml 2>&1 | Out-Null
Remove-Item $xml -ErrorAction SilentlyContinue
& $adb pull /sdcard/_ui5.xml $xml 2>&1 | Out-Null

if (Test-Path $xml) {
    [xml]$doc = Get-Content -LiteralPath $xml -Raw
    $nodes = $doc.SelectNodes('//node')
    Say ("nodes = " + $nodes.Count)
    $texts = @()
    foreach ($n in $nodes) { if ($n.text -and $n.text.Trim() -ne '') { $texts += $n.text } }
    Say ("texts: " + (($texts | Select-Object -First 40) -join ' | '))
    Say '--- priority chip nodes on the Home quick-add ---'
    foreach ($n in $nodes) {
        if ($n.text -eq 'Low' -or $n.text -eq 'Medium' -or $n.text -eq 'High' -or $n.text -eq 'Add a task') {
            $p = $n.ParentNode
            Say ("    '" + $n.text + "' " + $n.bounds + " | parent " + $p.class + " clickable=" + $p.clickable + " bounds=" + $p.bounds)
        }
    }
} else {
    Say 'no _ui5.xml'
}
Say 'DONE'
