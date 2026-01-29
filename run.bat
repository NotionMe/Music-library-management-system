@echo off
title Music Library Management System Player
color 0B

echo ========================================================
echo       Music Library Management System Launcher
echo ========================================================
echo.

echo [INFO] Checking for 'lib' directory...
if not exist "lib" (
    echo [ERROR] 'lib' directory not found! Please run this script from the project root.
    pause
    exit /b 1
)

echo [INFO] Compiling project (ant compile)...
call ant compile
if %errorlevel% neq 0 (
    echo [ERROR] Build failed. Please check build errors above.
    pause
    exit /b %errorlevel%
)
echo [OK] Build successful.

echo.
echo ========================================================
echo [WARNING] Ensure VLC Media Player is installed!
echo This app requires VLC (vlcj) to play music.
echo ========================================================
echo.

echo [INFO] Starting Application...
echo.

java -cp "build;lib/*" ua.notion.musiclibrary.Main

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Application crashed or exited with an error.
    pause
)
