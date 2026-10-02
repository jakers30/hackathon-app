# start-ai-proxy.ps1 - loads ai-proxy\.env into the ENVIRONMENT and starts the Ktor
# proxy in the background, then waits for GET /health to answer 'ok'.
#
# WHY the env load: Config.kt reads every secret via System.getenv(...), so the
# .env file must be exported before Gradle launches the JVM - running plain
# `gradle -p ai-proxy run` fails with "Missing required environment variable
# SUPABASE_URL".
#
# Usage:  powershell -ExecutionPolicy Bypass -File start-ai-proxy.ps1 [-Source]
#   (default) run the non-OneDrive mirror copy under %USERPROFILE%\AndroidBuild
#   -Source   run the copy inside the OneDrive repo (can hit file locks)
#
# Leave the window/process running for the whole demo. Logs:
#   %USERPROFILE%\AndroidBuild\_proxy.log  and  _proxy.err

param([switch] $Source)

$ErrorActionPreference = 'Stop'

$env:JAVA_HOME        = 'C:\Users\PREDATOR\.jdks\jdk-21.0.12.1+1'
$env:ANDROID_HOME     = 'C:\Users\PREDATOR\AppData\Local\Android\Sdk'
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME

$gradle  = 'C:\Users\PREDATOR\.gradle\wrapper\dists\gradle-8.9-bin\90cnw93cvbtalezasaz0blq0a\gradle-8.9\bin\gradle.bat'
$outDir  = Join-Path $env:USERPROFILE 'AndroidBuild'
New-Item -ItemType Directory -Force -Path $outDir | Out-Null

if ($Source) { $appDir = Join-Path $PSScriptRoot 'hackathon-app' }
else         { $appDir = Join-Path $outDir 'hackathon-app' }
$proxyDir = Join-Path $appDir 'ai-proxy'
$envFile  = Join-Path $proxyDir '.env'

if (-not (Test-Path $envFile)) { throw "No .env at $envFile (copy ai-proxy\.env.example and fill it in)." }

$already = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
if ($already) {
    Write-Host ("Port 8080 is already listening (PID " + ($already.OwningProcess -join ',') + ") - reusing it.") -ForegroundColor Yellow
} else {
    # Export every KEY=VALUE line from .env into this process, then let the child inherit it.
    foreach ($line in (Get-Content $envFile)) {
        $t = $line.Trim()
        if ($t -eq '' -or $t.StartsWith('#')) { continue }
        $i = $t.IndexOf('=')
        if ($i -gt 0) { Set-Item -Path ('env:' + $t.Substring(0, $i).Trim()) -Value $t.Substring($i + 1).Trim().Trim('"') }
    }
    Write-Host ("Starting ai-proxy (provider=$env:AI_PROVIDER model=$env:DEEPSEEK_MODEL) ...") -ForegroundColor Cyan
    $p = Start-Process -FilePath $gradle -ArgumentList @('-p', $proxyDir, 'run', '--console=plain') `
         -PassThru -WindowStyle Hidden `
         -RedirectStandardOutput (Join-Path $outDir '_proxy.log') `
         -RedirectStandardError  (Join-Path $outDir '_proxy.err')
    Write-Host "   gradle PID $($p.Id)"
}

$ok = $false
for ($i = 1; $i -le 36; $i++) {
    Start-Sleep -Seconds 5
    $h = & curl.exe -s -m 4 http://127.0.0.1:8080/health
    if ($h -eq 'ok') { $ok = $true; Write-Host "GET /health => ok (after ~$($i * 5)s)" -ForegroundColor Green; break }
    if ($i % 6 -eq 0) { Write-Host "   ...still waiting ($($i * 5)s)" }
}
if (-not $ok) {
    Write-Host 'GET /health never answered - see _proxy.log / _proxy.err' -ForegroundColor Red
    exit 1
}

$addr = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue |
        Select-Object -ExpandProperty LocalAddress -Unique
Write-Host ("Listening on: " + ($addr -join ', '))
Write-Host 'AI proxy is up. Leave this PC awake and this process running.' -ForegroundColor Green
