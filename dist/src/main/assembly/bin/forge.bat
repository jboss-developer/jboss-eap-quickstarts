@REM ----------------------------------------------------------------------------
@REM  Licensed under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Seam Forge Startup script
@REM
@REM Required Environment vars:
@REM ------------------
@REM JAVA_HOME - location of a JRE home dir
@REM
@REM Optional Environment vars
@REM ------------------
@REM FORGE_HOME - location of Forge's installed home dir
@REM FORGE_OPTS - parameters passed to the Java VM when running Forge
@REM ----------------------------------------------------------------------------

@echo off

@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

@REM Execute a user defined script before this one
if exist "%HOME%\forgerc_pre.bat" call "%HOME%\forgerc_pre.bat"

set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkFHome

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:chkFHome
if not "%FORGE_HOME%"=="" goto valFHome

if "%OS%"=="Windows_NT" SET "FORGE_HOME=%~dp0.."
if "%OS%"=="WINNT" SET "FORGE_HOME=%~dp0.."
if not "%FORGE_HOME%"=="" goto valFHome

echo.
echo ERROR: FORGE_HOME not found in your environment.
echo Please set the FORGE_HOME variable in your environment to match the
echo location of the Forge installation
echo.
goto error

:valFHome

:stripFHome
if not "_%FORGE_HOME:~-1%"=="_\" goto checkFBat
set "FORGE_HOME=%FORGE_HOME:~0,-1%"
goto stripFHome

:checkFBat
if exist "%FORGE_HOME%\bin\forge.bat" goto init

echo.
echo ERROR: FORGE_HOME is set to an invalid directory.
echo FORGE_HOME = "%FORGE_HOME%"
echo Please set the FORGE_HOME variable in your environment to match the
echo location of the Forge installation
echo.
goto error
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Windows NT with Novell Login
if "%OS%"=="WINNT" goto WinNTNovell

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

:WinNTNovell

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set FORGE_CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set FORGE_CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set FORGE_CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set FORGE_CMD_LINE_ARGS=%FORGE_CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
SET FORGE_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTCWJars

set FORGE_JARS="%FORGE_HOME%\lib\*"
goto runForge

@REM Start Forge
:runForge
set FORGE_MAIN_CLASS=org.jboss.seam.forge.shell.Bootstrap
%FORGE_JAVA_EXE% %FORGE_OPTS% -classpath %FORGE_JARS% "-Dforge.home=%FORGE_HOME%" %FORGE_MAIN_CLASS% %FORGE_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT
if "%OS%"=="WINNT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set FORGE_JAVA_EXE=
set FORGE_CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal & set ERROR_CODE=%ERROR_CODE%

:postExec
if exist "%HOME%\forgerc_post.bat" call "%HOME%\forgerc_post.bat"

if "%FORGE_TERMINATE_CMD%" == "on" exit %ERROR_CODE%

cmd /C exit /B %ERROR_CODE%


