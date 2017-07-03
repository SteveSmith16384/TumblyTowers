echo off
SET BIN_DIR=target
SET SRC_SHARED=..\..\..\src\main\c\shared
SET SRC_WINDOWS=..\..\..\src\main\c\windows
SET INCLUDE=..\..\..\src\main\h\shared
SET JDK_INCLUDE="C:\devel\Java\jdk7\include"
SET JDK_INCLUDE_WINDOWS="C:\devel\Java\jdk7\include\win32"
SET TARGET_64=..\..\..\%BIN_DIR%\windows-64

RMDIR /S /Q %TARGET_64%
MD %TARGET_64%\tmp
gcc -m64 -Wall -pthread -c -I %INCLUDE% -I %JDK_INCLUDE% -I %JDK_INCLUDE_WINDOWS% %SRC_SHARED%\Gamepad_private.c -o %TARGET_64%\tmp\Gamepad_private.o
gcc -m64 -Wall -pthread -c -I %INCLUDE% -I %JDK_INCLUDE% -I %JDK_INCLUDE_WINDOWS% %SRC_SHARED%\GamepadJniWrapper.c -o %TARGET_64%\tmp\GamepadJniWrapper.o
gcc -m64 -Wall -pthread -c -I %INCLUDE% -I %JDK_INCLUDE% -I %JDK_INCLUDE_WINDOWS% %SRC_WINDOWS%\Gamepad_windows.c -o %TARGET_64%\tmp\Gamepad_windows.o
gcc -o %TARGET_64%\libgamepad4j.dll -s -m64 -shared %TARGET_64%\tmp\*.o -Wl,--subsystem,windows -Wl,--add-stdcall-alias  -lwinmm 
