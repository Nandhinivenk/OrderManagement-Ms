@echo off
echo Starting Kitchen Flow Service...

:: Check if Maven is in PATH
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Maven not found in PATH. Using Maven from C:\maven\apache-maven-3.9.9\bin
    set PATH=%PATH%;C:\maven\apache-maven-3.9.9\bin
)

:: Clean and package
echo Building Kitchen Flow Service...
call mvn clean package -DskipTests

:: Run the application
echo Running Kitchen Flow Service...
java -jar target\kitchenflow-service-1.0-SNAPSHOT.jar

pause
