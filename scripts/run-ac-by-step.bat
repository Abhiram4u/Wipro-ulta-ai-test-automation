@echo off
setlocal
title Ulta AI Automation - Step-by-Step AC Test Runner

:MENU
cls
echo ===============================================================================
echo        ULTA BEAUTY AI TEST AUTOMATION - STEP-BY-STEP AC RUNNER
echo ===============================================================================
echo.
echo   Select an Acceptance Criteria scenario to execute:
echo.
echo   [1] Run AC1 - Navigate and Landing Page
echo   [2] Run AC2 - Search for Products
echo   [3] Run AC3 - Add Products to Cart
echo   [4] Run AC4 - Verify Cart Contents
echo   [5] Run AC5 - Validate PDP Recommendations
echo   [6] Run AC6 - Validate Cart Recommendations
echo   [7] Run AC7 - End-to-End Recommendation Consistency
echo.
echo   [8] Run ALL Scenarios Fast (Headless)
echo   [9] Run ALL Scenarios with HUD and Visible Browser (Demo Mode)
echo   [0] Exit
echo.
echo ===============================================================================
set "CHOICE="
set /p "CHOICE=Enter your choice [0-9]: "

if "%CHOICE%"=="1" (
    echo.
    echo Running AC1: Navigate and Landing...
    call mvn test "-Dcucumber.filter.tags=@AC1" "-Dheadless=false"
    pause
    goto MENU
)
if "%CHOICE%"=="2" (
    echo.
    echo Running AC2: Search for Products...
    call mvn test "-Dcucumber.filter.tags=@AC2" "-Dheadless=false"
    pause
    goto MENU
)
if "%CHOICE%"=="3" (
    echo.
    echo Running AC3: Add Products to Cart...
    call mvn test "-Dcucumber.filter.tags=@AC3" "-Dheadless=false"
    pause
    goto MENU
)
if "%CHOICE%"=="4" (
    echo.
    echo Running AC4: Verify Cart Contents...
    call mvn test "-Dcucumber.filter.tags=@AC4" "-Dheadless=false"
    pause
    goto MENU
)
if "%CHOICE%"=="5" (
    echo.
    echo Running AC5: Validate Product Page Recommendations...
    call mvn test "-Dcucumber.filter.tags=@AC5" "-Dheadless=false"
    pause
    goto MENU
)
if "%CHOICE%"=="6" (
    echo.
    echo Running AC6: Validate Cart Page Recommendations...
    call mvn test "-Dcucumber.filter.tags=@AC6" "-Dheadless=false"
    pause
    goto MENU
)
if "%CHOICE%"=="7" (
    echo.
    echo Running AC7: End-to-End Recommendation Consistency...
    call mvn test "-Dcucumber.filter.tags=@AC7" "-Dheadless=false"
    pause
    goto MENU
)
if "%CHOICE%"=="8" (
    echo.
    echo Running ALL Scenarios Fast (Headless)...
    call mvn test "-Dheadless=true"
    pause
    goto MENU
)
if "%CHOICE%"=="9" (
    echo.
    echo Running ALL Scenarios with HUD Banner and Visible Browser...
    call mvn test "-Dheadless=false" "-Daction.delay.ms=1000"
    pause
    goto MENU
)
if "%CHOICE%"=="0" (
    exit /b 0
)

echo.
echo Invalid choice. Please enter a number between 0 and 9.
pause
goto MENU
