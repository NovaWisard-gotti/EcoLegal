@rem Gradle startup script for Windows (standard Gradle wrapper batch file).
@rem If gradle\wrapper\gradle-wrapper.jar is missing, run:
@rem   gradle wrapper --gradle-version 8.7
@rem using a locally installed Gradle to regenerate it.
@if "%DEBUG%"=="" @echo off
setlocal

set DIRNAME=%~dp0
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

set JAVA_EXE=java.exe
if defined JAVA_HOME set JAVA_EXE=%JAVA_HOME%\bin\java.exe

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
if not exist "%CLASSPATH%" (
    echo ERROR: gradle-wrapper.jar not found. Run 'gradle wrapper --gradle-version 8.7' locally, or use the CI workflow.
    exit /b 1
)

"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

endlocal
