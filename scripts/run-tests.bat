@echo off
REM ==============================================================================
REM Ulta Beauty AI-Powered Test Automation Suite Execution Script (Windows CMD)
REM ==============================================================================

echo [INFO] Starting Ulta Beauty Test Automation Suite...
cd /d "%~dp0.."

echo [INFO] Environment: mock (standalone testbed)
echo [INFO] Executing Maven Test Lifecycle...

call mvn clean test
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Test execution failed! Check target/surefire-reports for details.
    exit /b %ERRORLEVEL%
)

echo [INFO] Generating AI Test Quality Summary Report...
call mvn exec:java "-Dexec.mainClass=com.ulta.automation.utils.AIResultAnalyzer"

echo [SUCCESS] Test execution and AI Report generation completed successfully!
echo [INFO] Report available at: test-artifacts\AI_TEST_REPORT_SUMMARY.md
pause
