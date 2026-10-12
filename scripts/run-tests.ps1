# ==============================================================================
# Ulta Beauty AI-Powered Test Automation Suite Execution Script (PowerShell)
# ==============================================================================

param (
    [string]$env = "mock",
    [string]$browser = "chrome",
    [bool]$headless = $true
)

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location (Join-Path $scriptDir "..")

Write-Host "==============================================================================" -ForegroundColor Cyan
Write-Host "Ulta Beauty AI-Powered Test Automation Suite" -ForegroundColor Cyan
Write-Host "Target Environment : $env" -ForegroundColor Yellow
Write-Host "Browser            : $browser" -ForegroundColor Yellow
Write-Host "Headless Mode      : $headless" -ForegroundColor Yellow
Write-Host "==============================================================================" -ForegroundColor Cyan

$mvnCmd = "mvn test -Denv=$env -Dbrowser=$browser -Dheadless=$headless"
Write-Host "[INFO] Executing: $mvnCmd" -ForegroundColor Green
Invoke-Expression $mvnCmd

if ($LASTEXITCODE -eq 0) {
    Write-Host "[SUCCESS] Tests completed successfully!" -ForegroundColor Green
    Write-Host "[INFO] Refreshing AI Test Report..." -ForegroundColor Green
    mvn exec:java "-Dexec.mainClass=com.ulta.automation.utils.AIResultAnalyzer"
    Write-Host "[INFO] Report available at: test-artifacts/AI_TEST_REPORT_SUMMARY.md" -ForegroundColor Cyan
} else {
    Write-Host "[ERROR] Test execution failed with exit code $LASTEXITCODE" -ForegroundColor Red
}
