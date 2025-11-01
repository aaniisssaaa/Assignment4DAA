@echo off
REM Quick Test Script

echo Running JUnit Tests...
echo.

where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    pause
    exit /b 1
)

call mvn test

echo.
echo Tests complete!
pause
