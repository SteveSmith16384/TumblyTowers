
BIN_DIR=target
SRC_SHARED=../../../src/main/c/shared
SRC_LINUX=../../../src/main/c/linux
INCLUDE=../../../src/main/h/shared
JDK_INCLUDE=/stuff/java/j2se/current/include/
JDK_INCLUDE_LINUX=/stuff/java/j2se/current/include/linux/
TARGET_32=../../../${BIN_DIR}/linux-32

rm -rf ${TARGET_32}
mkdir -p ${TARGET_32}/tmp
gcc -m32 -Wall -fPIC -pthread -c -I ${INCLUDE} -I ${JDK_INCLUDE} -I ${JDK_INCLUDE_LINUX} ${SRC_SHARED}/Gamepad_private.c -o ${TARGET_32}/tmp/Gamepad_private.o
gcc -m32 -Wall -fPIC -pthread -c -I ${INCLUDE} -I ${JDK_INCLUDE} -I ${JDK_INCLUDE_LINUX} ${SRC_SHARED}/GamepadJniWrapper.c -o ${TARGET_32}/tmp/GamepadJniWrapper.o
gcc -m32 -Wall -fPIC -pthread -c -I ${INCLUDE} -I ${JDK_INCLUDE} -I ${JDK_INCLUDE_LINUX} ${SRC_LINUX}/Gamepad_linux.c -o ${TARGET_32}/tmp/Gamepad_linux.o
gcc -m32 -shared -Wall -Wl,-soname,libgamepad-jni-wrapper.so.1 -o ${TARGET_32}/libgamepad4j.so ${TARGET_32}/tmp/*.o
rm -rf ${TARGET_32}/tmp

