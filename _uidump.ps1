# _uidump.ps1 - dumps the phone's current UI tree and reports whether the Home
# quick-add priority chips (Low / Medium / High) are on screen, with their bounds,
# plus the focused activity. Results -> _uidump.txt, raw tree -> _ui2.xml.
$ErrorActionPreference = 'Continue'

$root = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$out  = Join-Path $root '_uidump.txt'
$adb  = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk\platform-tools\adb.exe'

function Say($m) { Add-Content -Path $out -Value $m }
Set-Content -Path $out -Value ('ui dump ' + (Get-Date).ToString('HH:mm:ss'))

Say '--- adb devices -l ---'
& $adb devices -l 2>&1 | ForEach-Object { Say $_ }
Say '--- adb reverse --list ---'
& $adb reverse --list 2>&1 | ForEach-Object { Say $_ }

Say '--- focused window ---'
& $adb shell dumpsys window 2>&1 | Select-String 'mCurrentFocus|mFocusedApp' | ForEach-Object { Say $_.ToString().Trim() }

Say '--- ui dump ---'
& $adb shell uiautomator dump /sdcard/_ui2.xml 2>&1 | ForEach-Object { Say $_ }
$xml = Join-Path $root '_ui2.xml'
Remove-Item $xml -ErrorAction SilentlyContinue
& $adb pull /sdcard/_ui2.xml $xml 2>&1 | ForEach-Object { Say $_ }

if (Test-Path $xml) {
    [xml]$doc = Get-Content -LiteralPath $xml -Raw
    $nodes = $doc.SelectNodes('//node')
    Say ("nodes = " + $nodes.Count)

    $want = @('Add a task', 'Low', 'Medium', 'High', 'Add', 'Hi,')
    foreach ($w in $want) {
        $hit = @($nodes | Where-Object { $_.text -eq $w })
        Say ("text '" + $w + "' matches = " + $hit.Count)
        foreach ($n in $hit) {
            $p = $n.ParentNode
            Say ("    node bounds=" + $n.bounds + " clickable=" + $n.clickable +
                 " | parent " + $p.class + " clickable=" + $p.clickable + " bounds=" + $p.bounds)
        }
    }

    Say '--- every Low/Medium/High-class node (with clickable ancestors) ---'
    $nodes | Where-Object { $_.text -eq 'Low' -or $_.text -eq 'Medium' -or $_.text -eq 'High' } | ForEach-Object {
        $a = $_
        $chain = @()
        for ($i = 0; $i -lt 3 -and $a -ne $null; $i++) { $chain += ($a.class + ':' + $a.clickable); $a = $a.ParentNode }
        Say ("    " + $_.text + " " + $_.bounds + " <- " + ($chain -join ' / '))
    }
} else {
    Say 'no _ui2.xml'
}
Say 'DONE'
