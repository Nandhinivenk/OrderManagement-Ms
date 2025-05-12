@echo off
echo Setting up Maven Wrapper...

:: Create .mvn/wrapper directory
mkdir .mvn 2>nul
mkdir .mvn\wrapper 2>nul

:: Download wrapper files
echo Downloading Maven Wrapper files...
curl -o mvnw.cmd https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw.cmd
curl -o mvnw https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw
curl -o .mvn\wrapper\maven-wrapper.jar https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar

:: Create properties file
echo distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip > .mvn\wrapper\maven-wrapper.properties

echo Maven Wrapper setup complete!
echo You can now use '.\mvnw' instead of 'mvn'
pause
