# _bringapp.ps1 - the previous dump caught the notification shade, so this dismisses
# it, brings MainActivity to the front and dumps the Home screen again, reporting the
# quick-add priority chips with their bounds (needed to tap them). -> _bringapp.txt
$ErrorActionPreference = 'Continue'

$root = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$out  = Join-Path $root '_bringapp.txt'
$xml  = Join-Path $root '_ui3.xml'
$adb  = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk\platform-tools\adb.exe'

function Say($m) { Add-Content -Path $out -Value $m }
Set-Content -Path $out -Value ('bring app ' + (Get-Date).ToString('HH:mm:ss'))

# Back closes the notification shade without leaving the app.
& $adb shell input keyevent 4 2>&1 | Out-Null
Start-Sleep -Seconds 1
& $adb shell am start -W -n com.raite.studyroom/.MainActivity 2>&1 | ForEach-Object { Say ("start| " + $_) }
Start-Sleep -Seconds 5
Say '--- focused window ---'
& $adb shell dumpsys window 2>&1 | Select-String 'mCurrentFocus' | ForEach-Object { Say $_.ToString().Trim() }

& $adb shell uiautomator dump /sdcard/_ui3.xml 2>&1 | Out-Null
Remove-Item $xml -ErrorAction SilentlyContinue
& $adb pull /sdcard/_ui3.xml $xml 2>&1 | Out-Null

if (Test-Path $xml) {
    [xml]$doc = Get-Content -LiteralPath $xml -Raw
    $nodes = $doc.SelectNodes('//node')
    Say ("nodes = " + $nodes.Count)
    Say '--- nodes with text ---'
    foreach ($n in $nodes) {
        if ($n.text -and $n.text.Trim() -ne '') {
            $p = $n.ParentNode
            Say ("    '" + $n.text + "' " + $n.class + " bounds=" + $n.bounds + " clickable=" + $n.clickable +
                 " | parent clickable=" + $p.clickable + " bounds=" + $p.bounds)
        }
    }
} else {
    Say 'no _ui3.xml'
}
Say 'DONE'
