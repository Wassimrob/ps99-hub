@echo off
set DIR=%~dp0
set APP_BASE_NAME=%~n0
set APP_HOME=%DIR%

set JAVA_EXE=%JAVA_HOME%\bin\java.exe

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

"%JAVA_EXE%" -Xmx64m -Xms64m -Dorg.gradle.appname=%APP_BASE_NAME% -classpath %CLASSPATH% org.gradle.wrapper.GradleWrapperMain %*