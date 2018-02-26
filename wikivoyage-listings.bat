@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Archivo de órdenes de inicio de Wikivoyage listings para WIndows
@rem
@rem ##########################################################################

@REM elimina archivos de registro de sesiones anteriores si existen
if exist debug.log (
	del /F /Q debug.log
)

@rem Inicia ámbito local para las varialbes en líneas de comando de Windows NT
if "%OS%"=="Windows_NT" setlocal

@rem Aquí se pueden añadir opciones para JVM
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
set CMD_LINE_ARGS=%$

:execute
set BZIP2=%APP_HOME%\lib\bzip2-20090327.jar
set COMMONS_COMPRESS=%APP_HOME%\lib\commons-compress-1.9.jar
set COMMONS_LOGGING=%APP_HOME%\lib\commons-logging-1.1.1.jar
set COMMONS_VALIDATOR=%APP_HOME%\lib\commons-validator-1.5.0.jar
set GNU_TROVE_OSMAND=%APP_HOME%\lib\gnu-trove-osmand.jar
set LOG4J_LOGGER=%APP_HOME%\lib\log4j-1.2.17.jar
set JCOMMANDER=%APP_HOME%\lib\jcommander-1.30.jar
set JSON=%APP_HOME%\lib\json-20170516.jar
set JUNIDECODE=%APP_HOME%\lib\junidecode-0.1.jar
set KXML2=%APP_HOME%\lib\kxml2-2.3.0.jar
set OSMAND_CORE=%APP_HOME%\lib\OsmAnd-core.jar
set OSMAND_MAPCREATOR=%APP_HOME%\lib\OsmAndMapCreator.jar
set SQLITE_JDBC=%APP_HOME%\lib\sqlite-jdbc-3.7.6.3-20110609.081603-3.jar
set SWC_ENGINE=%APP_HOME%\lib\swc-engine-2.0.0-jar-with-dependencies.jar

REM Ejecuta el programa Java con argumentos
"%JAVA_EXE%" -Xmx1024m -cp "%APP_HOME%/wikivoyage-listings.jar;%APP_HOME%/build/libs/wikivoyage-listings.jar;%BZIP2%;%COMMONS_COMPRESS%;%COMMONS_LOGGING%;%COMMONS_VALIDATOR%;%GNU_TROVE_OSMAND%;%JCOMMANDER%;%JSON%;%JUNIDECODE%;%KXML2%;%LOG4J_LOGGER%;%OSMAND_CORE%;%OSMAND_MAPCREATOR%;%SQLITE_JDBC%;%SWC_ENGINE%;%APP_HOME%/config/" org.wikivoyage.listings.Main %CMD_LINE_ARGS%

:end 
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:loquesea