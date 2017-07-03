

BIN_DIR=target
SRC_SHARED=../../../src/main/c/shared
SRC_MAC=../../../src/main/c/macos
INCLUDE=../../../src/main/h/shared
TARGET=../../../${BIN_DIR}/macos

OSX_JAVA_HEADERS=/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers
OSX_SDK=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk

mkdir -p ${TARGET}

clang -arch x86_64 -framework IOKit -framework CoreFoundation -dynamiclib -o ${TARGET}/libgamepad4j.jnilib -I${OSX_JAVA_HEADERS} -I./include -I${INCLUDE} -isysroot ${OSX_SDK} ${SRC_SHARED}/Gamepad_private.c ${SRC_SHARED}/GamepadJniWrapper.c ${SRC_MAC}/Gamepad_macosx.c 


