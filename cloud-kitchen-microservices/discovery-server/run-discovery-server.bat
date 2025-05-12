@echo off
echo Starting Discovery Server...

:: Check if Maven is in PATH
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Maven not found in PATH. Using Maven from C:\maven\apache-maven-3.9.9\bin
    set PATH=%PATH%;C:\maven\apache-maven-3.9.9\bin
)

:: Clean and package
echo Building Discovery Server...
call mvn clean package -DskipTests

:: Run the application
echo Running Discovery Server...
java -jar target\discovery-server-1.0-SNAPSHOT.jar

pause
