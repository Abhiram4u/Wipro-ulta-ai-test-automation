# Interactive PowerShell Runner for Ulta Beauty Acceptance Criteria (AC1 to AC7)
param (
    [string]$AC = ""
)

function Show-Menu {
    Clear-Host
    Write-Host "===============================================================================" -ForegroundColor Cyan
    Write-Host "       ULTA BEAUTY AI TEST AUTOMATION - STEP-BY-STEP AC RUNNER" -ForegroundColor Yellow
    Write-Host "===============================================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  [1] Run AC1 - Navigate & Landing (Homepage, Search Bar, Account Icon)"
    Write-Host "  [2] Run AC2 - Search for Products ('moisturizer', 'mascara', 'vitamin c serum')"
    Write-Host "  [3] Run AC3 - Add Products to Cart (Click product, PDP, Add to Bag, Cart Badge)"
    Write-Host "  [4] Run AC4 - Verify Cart Contents (Shopping Bag >= 3 items with correct names)"
    Write-Host "  [5] Run AC5 - Validate PDP Recommendations (Moisturizer >= 30% keyword relevance)"
    Write-Host "  [6] Run AC6 - Validate Cart Recommendations (Moisturizer+Mascara >= 25% relevance)"
    Write-Host "  [7] Run AC7 - End-to-End Recommendation Consistency (Beauty Category >= 20%)"
    Write-Host ""
    Write-Host "  [8] Run ALL ACs Sequentially (AC1 to AC7 in order, Headless)" -ForegroundColor Green
    Write-Host "  [9] Run ALL ACs in Visible Browser (Slow-Motion Demo Mode + On-Screen HUD)" -ForegroundColor Magenta
    Write-Host "  [0] Exit"
    Write-Host "===============================================================================" -ForegroundColor Cyan
}

function Run-AC([string]$tag, [bool]$visible=$true) {
    Write-Host "`n>>> Executing Tag: $tag (Visible Browser: $visible)..." -ForegroundColor Green
    $headlessFlag = if ($visible) { "-Dheadless=false" } else { "-Dheadless=true" }
    mvn test "-Dcucumber.filter.tags=$tag" $headlessFlag
}

if ($AC -ne "") {
    Run-AC -tag "@$AC" -visible $true
    exit 0
}

do {
    Show-Menu
    $choice = Read-Host "Select an option [0-9]"
    switch ($choice) {
        "1" { Run-AC -tag "@AC1"; pause }
        "2" { Run-AC -tag "@AC2"; pause }
        "3" { Run-AC -tag "@AC3"; pause }
        "4" { Run-AC -tag "@AC4"; pause }
        "5" { Run-AC -tag "@AC5"; pause }
        "6" { Run-AC -tag "@AC6"; pause }
        "7" { Run-AC -tag "@AC7"; pause }
        "8" { 
            Write-Host "`n>>> Running ALL Scenarios Fast (Headless)..." -ForegroundColor Green
            mvn test "-Dheadless=true"
            pause 
        }
        "9" { 
            Write-Host "`n>>> Launching Full Visible Demo with HUD Banner and Pacing..." -ForegroundColor Magenta
            mvn test "-Dheadless=false" "-Daction.delay.ms=1000"
            pause
        }
        "0" { break }
        default { Write-Host "Invalid option. Please try again." -ForegroundColor Red; Start-Sleep -Seconds 1 }
    }
} while ($choice -ne "0")
