@echo off
REM ==============================================================================
REM Generate and Serve Allure HTML Report
REM ==============================================================================

echo [INFO] Generating Allure Report from target/allure-results...
cd /d "%~dp0.."

call mvn allure:report
echo [INFO] Allure report generated in target/site/allure-maven-plugin/
echo [INFO] To view live report in browser, run:
echo        mvn allure:serve
pause
