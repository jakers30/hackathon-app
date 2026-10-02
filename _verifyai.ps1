# _verifyai.ps1 - proves the PDF -> text path end to end WITHOUT the phone:
#   1. logs into Supabase as the demo student (password grant, anon key from .env)
#   2. finds the DEMO01 room
#   3. marks the proxy log, POSTs /ai/roadmap (which reads EVERY room resource)
#   4. prints the model's answer plus the new proxy log lines, which include one
#      "resource '...' (application/pdf) -> N bytes, M chars of text" line per file
# No secrets are printed: only the URL and whether the key is present.
$ErrorActionPreference = 'Continue'

$root = 'c:\Users\PREDATOR\OneDrive\Desktop\Hackathon'
$out  = Join-Path $root '_verifyai.txt'
$log  = Join-Path $env:USERPROFILE 'AndroidBuild\_proxy.log'

function Say($m) { Add-Content -Path $out -Value $m }
Set-Content -Path $out -Value ('ai verify ' + (Get-Date).ToString('HH:mm:ss'))

$envFile = Join-Path $env:USERPROFILE 'AndroidBuild\hackathon-app\ai-proxy\.env'
$cfg = @{}
foreach ($line in (Get-Content $envFile)) {
    $t = $line.Trim()
    if ($t -eq '' -or $t.StartsWith('#') -or -not $t.Contains('=')) { continue }
    $i = $t.IndexOf('=')
    $cfg[$t.Substring(0, $i).Trim()] = $t.Substring($i + 1).Trim().Trim('"')
}
$url  = $cfg['SUPABASE_URL']
$anon = $cfg['SUPABASE_ANON_KEY']
Say ("supabase = " + $url + " | anon key present = " + [bool]$anon)
Say ("provider = " + $cfg['AI_PROVIDER'] + " / " + $cfg['DEEPSEEK_MODEL'])

try {
    $auth = Invoke-RestMethod -Method Post -Uri "$url/auth/v1/token?grant_type=password" `
        -Headers @{ apikey = $anon; 'Content-Type' = 'application/json' } `
        -Body (@{ email = 'demo@studyroom.app'; password = 'demo1234' } | ConvertTo-Json -Compress)
    $token = $auth.access_token
    Say ("login ok as " + $auth.user.email + " (token len " + $token.Length + ")")
} catch {
    Say ("LOGIN FAILED: " + $_.Exception.Message)
    Say 'DONE'
    exit 1
}

try {
    # The room is resolved from the demo user's own memberships (RLS-safe), with a
    # fallback to listing the rooms the token can see.
    $userId = $auth.user.id
    $mine = Invoke-RestMethod -Method Get `
        -Uri "$url/rest/v1/room_members?user_id=eq.$userId&select=room_id" `
        -Headers @{ apikey = $anon; Authorization = "Bearer $token" }
    Say ("room_members rows: " + @($mine).Count)

    $visible = Invoke-RestMethod -Method Get `
        -Uri "$url/rest/v1/rooms?select=id,name,invite_code&limit=10" `
        -Headers @{ apikey = $anon; Authorization = "Bearer $token" }
    foreach ($r in @($visible)) { Say ("visible room: " + $r.id + " | " + $r.name + " | code " + $r.invite_code) }

    $roomId = if (@($mine).Count -gt 0) { $mine[0].room_id }
              elseif (@($visible).Count -gt 0) { $visible[0].id }
              else { $null }
    Say ("using roomId = " + $roomId)
    if (-not $roomId) {
        Say 'NO ROOM AVAILABLE FOR THIS USER'
        Say 'DONE'
        exit 1
    }
} catch {
    Say ("ROOM LOOKUP FAILED: " + $_.Exception.Message)
    Say 'DONE'
    exit 1
}

$mark = (Get-Item $log -ErrorAction SilentlyContinue).Length
Say ("proxy log mark = " + $mark + " bytes")

$body = @{ roomId = "$roomId" } | ConvertTo-Json -Compress
$sw = [Diagnostics.Stopwatch]::StartNew()
try {
    $resp = Invoke-RestMethod -Method Post -Uri 'http://127.0.0.1:8080/ai/roadmap' `
        -Headers @{ Authorization = "Bearer $token"; 'Content-Type' = 'application/json' } `
        -Body $body -TimeoutSec 300
    $sw.Stop()
    Say ("POST /ai/roadmap -> " + $sw.Elapsed.TotalSeconds.ToString('0.0') + "s, items = " + @($resp.items).Count)
    if (@($resp.items).Count -gt 0) {
        Say ("first topic  : " + $resp.items[0].topic)
        Say ("description  : " + $resp.items[0].description)
        Say ("subtopics    : " + ($resp.items[0].subtopics -join ' | '))
    }
} catch {
    $sw.Stop()
    Say ("AI CALL FAILED after " + $sw.Elapsed.TotalSeconds.ToString('0.0') + "s: " + $_.Exception.Message)
}

Say '--- new proxy log lines (resource extraction + request) ---'
try {
    $fs = [IO.File]::Open($log, 'Open', 'Read', 'ReadWrite')
    $fs.Seek($mark, 'Begin') | Out-Null
    $reader = New-Object IO.StreamReader($fs)
    $chunk = $reader.ReadToEnd()
    $reader.Close()
    foreach ($line in ($chunk -split "`r?`n")) {
        if ($line -match "resource '|/ai/|POST|INFO|Responding") { Say ("log| " + $line) }
    }
} catch {
    Say ("log read failed: " + $_.Exception.Message)
}
Say 'DONE'
