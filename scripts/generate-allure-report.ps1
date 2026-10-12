# ==============================================================================
# Generate and Serve Allure HTML Report (PowerShell)
# ==============================================================================

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location (Join-Path $scriptDir "..")

Write-Host "==============================================================================" -ForegroundColor Cyan
Write-Host "Ulta Beauty Automation - Allure Report Generator" -ForegroundColor Yellow
Write-Host "==============================================================================" -ForegroundColor Cyan

Write-Host "[INFO] Generating Allure HTML Report from target/allure-results..." -ForegroundColor Green
mvn allure:report

if ($LASTEXITCODE -eq 0) {
    Write-Host "[SUCCESS] Allure report generated at: target/site/allure-maven-plugin/index.html" -ForegroundColor Green
    Write-Host "[INFO] To launch and serve the report in your browser, run:" -ForegroundColor Yellow
    Write-Host "       mvn allure:serve" -ForegroundColor Cyan
} else {
    Write-Host "[ERROR] Failed to generate Allure report." -ForegroundColor Red
}
