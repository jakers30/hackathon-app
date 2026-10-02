# _fixscreen.ps1 - the shade would not close with BACK, so: collapse the status bar,
# press HOME, relaunch MainActivity and dump the UI again. -> _fixscreen.txt
$ErrorActionPreference = 'Continue'

$root = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$out  = Join-Path $root '_fixscreen.txt'
$xml  = Join-Path $root '_ui4.xml'
$adb  = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk\platform-tools\adb.exe'

function Say($m) { Add-Content -Path $out -Value $m }
Set-Content -Path $out -Value ('fix screen ' + (Get-Date).ToString('HH:mm:ss'))

Say '--- collapse status bar ---'
& $adb shell cmd statusbar collapse 2>&1 | ForEach-Object { Say ("cmd| " + $_) }
Start-Sleep -Seconds 1
& $adb shell input keyevent 3 2>&1 | Out-Null
Start-Sleep -Seconds 2
& $adb shell am start -n com.raite.studyroom/.MainActivity 2>&1 | Out-Null
Start-Sleep -Seconds 6

Say '--- focused window ---'
& $adb shell dumpsys window 2>&1 | Select-String 'mCurrentFocus' | ForEach-Object { Say $_.ToString().Trim() }

& $adb shell uiautomator dump /sdcard/_ui4.xml 2>&1 | Out-Null
Remove-Item $xml -ErrorAction SilentlyContinue
& $adb pull /sdcard/_ui4.xml $xml 2>&1 | Out-Null

if (Test-Path $xml) {
    [xml]$doc = Get-Content -LiteralPath $xml -Raw
    $nodes = $doc.SelectNodes('//node')
    Say ("nodes = " + $nodes.Count)
    Say '--- text nodes ---'
    $i = 0
    foreach ($n in $nodes) {
        if ($n.text -and $n.text.Trim() -ne '') {
            $i++
            if ($i -le 80) {
                $p = $n.ParentNode
                Say ("    '" + $n.text + "' " + $n.bounds + " clickable=" + $n.clickable +
                     " | parent clickable=" + $p.clickable + " bounds=" + $p.bounds)
            }
        }
    }
    Say ("text nodes total = " + $i)
} else {
    Say 'no _ui4.xml'
}
Say 'DONE'
