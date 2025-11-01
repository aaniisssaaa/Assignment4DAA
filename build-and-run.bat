@echo off
REM Build and Run Script for Smart City Scheduling Project

echo ======================================
echo Smart City Scheduling Build Script
echo ======================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo.
    echo Please install Maven from: https://maven.apache.org/download.cgi
    echo After installation, add Maven's bin directory to your PATH environment variable.
    echo.
    pause
    exit /b 1
)

echo [1/4] Cleaning previous build...
call mvn clean
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven clean failed
    pause
    exit /b 1
)

echo.
echo [2/4] Compiling source code...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo [3/4] Running tests...
call mvn test
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: Some tests failed
    pause
)

echo.
echo [4/4] Running main program...
echo.
call mvn exec:java -Dexec.mainClass="Main"

echo.
echo ======================================
echo Build and execution complete!
echo ======================================
pause
