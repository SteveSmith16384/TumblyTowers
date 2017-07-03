echo off
SET BIN_DIR=target
SET SRC_SHARED=..\..\..\src\main\c\shared
SET SRC_WINDOWS=..\..\..\src\main\c\windows
SET INCLUDE=..\..\..\src\main\h\shared
SET JDK_INCLUDE="C:\Program Files\Java\jdk1.7.0_67\include"
SET JDK_INCLUDE_WINDOWS="C:\Program Files\Java\jdk1.7.0_67\include\win32"
SET TARGET_32=..\..\..\%BIN_DIR%\windows-32

RMDIR /S /Q %TARGET_32%
MD %TARGET_32%\tmp
gcc -m32 -Wall -pthread -c -I %INCLUDE% -I %JDK_INCLUDE% -I %JDK_INCLUDE_WINDOWS% %SRC_SHARED%\Gamepad_private.c -o %TARGET_32%\tmp\Gamepad_private.o
gcc -m32 -Wall -pthread -c -I %INCLUDE% -I %JDK_INCLUDE% -I %JDK_INCLUDE_WINDOWS% %SRC_SHARED%\GamepadJniWrapper.c -o %TARGET_32%\tmp\GamepadJniWrapper.o
gcc -m32 -Wall -pthread -c -I %INCLUDE% -I %JDK_INCLUDE% -I %JDK_INCLUDE_WINDOWS% %SRC_WINDOWS%\Gamepad_windows.c -o %TARGET_32%\tmp\Gamepad_windows.o
rem gcc -m32 -shared -Wall -o %TARGET_32%\libgamepad4j.dll %TARGET_32%\tmp\*.o -lwinmm 
gcc -o %TARGET_32%\libgamepad4j.dll -s -m32 -shared %TARGET_32%\tmp\*.o -Wl,--subsystem,windows -Wl,--add-stdcall-alias  -lwinmm 
