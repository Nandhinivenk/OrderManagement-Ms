@echo off
echo ===================================================
echo Discovery Server Fix and Run Script
echo ===================================================
echo.

:: Set Maven path if not in PATH
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Maven not found in PATH. Using Maven from C:\maven\apache-maven-3.9.9\bin
    set PATH=%PATH%;C:\maven\apache-maven-3.9.9\bin
)

:: Navigate to discovery-server directory
cd discovery-server

:: Clean and package
echo Building Discovery Server...
call mvn clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo Build failed. Trying to fix common issues...
    
    :: Check if target directory exists and clean it
    if exist target (
        echo Cleaning target directory...
        rmdir /S /Q target
    )
    
    :: Try building again
    echo Rebuilding Discovery Server...
    call mvn clean package -DskipTests
)

if %ERRORLEVEL% neq 0 (
    echo Build still failing. Please check the logs for specific errors.
    pause
    exit /b 1
)

:: Run the application
echo.
echo ===================================================
echo Starting Discovery Server...
echo ===================================================
echo.
echo The Discovery Server will be available at: http://localhost:8761
echo.
echo Press Ctrl+C to stop the server
echo.

java -jar target\discovery-server-1.0-SNAPSHOT.jar

pause
