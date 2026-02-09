@echo off
echo Building EyeSpy mod...
call gradlew.bat build

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Build successful!
echo Copying Jar to Mods folder...

copy /Y "build\libs\EyeSpy.jar" "..\EyeSpy-Repacked.jar"

if %ERRORLEVEL% NEQ 0 (
    echo Failed to copy jar!
    pause
    exit /b %ERRORLEVEL%
)

echo Done! Updated ..\EyeSpy-Repacked.jar