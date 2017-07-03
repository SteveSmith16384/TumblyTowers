/*
 * GamepadJniWrapper.c
 *
 *  Created on: Jul 28, 2013
 *      Author: msc
 */

#include <jni.h>
#include <stdio.h>
#include <Gamepad.h>
#include <org_gamepad4j_desktop_GamepadJniWrapper.h>

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natInit
(JNIEnv *env, jobject obj) {
	printf("Initialize native Gamepad API.\n");
	Gamepad_init();
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natRelease
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natRelease
(JNIEnv *env, jobject obj) {
	printf("Shutdown native Gamepad API.\n");
	Gamepad_shutdown();
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetNumberOfPads
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetNumberOfPads(
		JNIEnv *env, jobject obj) {
	return Gamepad_numDevices();
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetDeviceID
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetDeviceID(
		JNIEnv *env, jobject obj, jint index) {
	struct Gamepad_device * device = Gamepad_deviceAtIndex(index);
	if(device == NULL) {
		return -1;
	}
	return device->deviceID;
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetNumberOfButtons
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetNumberOfButtons(
		JNIEnv *env, jobject obj, jint index) {
	struct Gamepad_device * device = Gamepad_deviceAtIndex(index);
	if(device == NULL) {
		return -1;
	}
	return device->numButtons;
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetNumberOfAxes
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetNumberOfAxes(
		JNIEnv *env, jobject obj, jint index) {
	struct Gamepad_device * device = Gamepad_deviceAtIndex(index);
	if(device == NULL) {
		return -1;
	}
	return device->numAxes;
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetIdOfPad
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetIdOfPad(
		JNIEnv *env, jobject obj, jint index) {
	struct Gamepad_device * device = Gamepad_deviceAtIndex(index);
	if(device == NULL) {
		return -1;
	}
	return device->deviceID;
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natDetectPads
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natDetectPads
(JNIEnv *env, jobject obj) {
	Gamepad_detectDevices();
	// The following call is required on Windows
	Gamepad_processEvents();
	return;
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetControllerId
 * Signature: (I)S
 */
JNIEXPORT jstring JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetControllerDescription(
		JNIEnv *env, jobject obj, jint gamepadIndex)
{
	unsigned int index = gamepadIndex;
	struct Gamepad_device * device = Gamepad_deviceAtIndex(index);
	if(device == NULL) {
		return NULL;
	}
    jstring result = (*env)->NewStringUTF(env, device->description); // C style string to Java String
    return result;
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetControllerId
 * Signature: (I[])V
 */
JNIEXPORT void JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetControllerIDs(
		JNIEnv *env, jobject obj, jint gamepadIndex, jintArray idArray)
{
	struct Gamepad_device * device = Gamepad_deviceAtIndex(gamepadIndex);
	if(device == NULL) {
		return;
	}
	jint *ids = (*env)->GetIntArrayElements(env, idArray, NULL);

	ids[0] = device->deviceID;
	ids[1] = device->vendorID;
	ids[2] = device->productID;

	(*env)->ReleaseIntArrayElements(env, idArray, ids, 0);
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetControllerButtonStates
 * Signature: (B[])V
 */
JNIEXPORT void JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetControllerButtonStates(
		JNIEnv *env, jobject obj, jint gamepadIndex, jbooleanArray buttonArray)
{
	unsigned int buttonIndex;

	struct Gamepad_device * device = Gamepad_deviceAtIndex(gamepadIndex);
	if(device == NULL) {
		return;
	}

	jboolean *states = (*env)->GetBooleanArrayElements(env, buttonArray, NULL);

	for (buttonIndex = 0; buttonIndex <= device->numButtons; buttonIndex++) {
		states[buttonIndex] = JNI_FALSE;
		if (device->buttonStates[buttonIndex]) {
			states[buttonIndex] = JNI_TRUE;
		}
	}

	(*env)->ReleaseBooleanArrayElements(env, buttonArray, states, 0);
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetControllerButtonState
 * Signature: (B[])V
 */
JNIEXPORT jint JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetControllerButtonState(
		JNIEnv *env, jobject obj, jint gamepadIndex, jint buttonIndex)
{
	struct Gamepad_device * device = Gamepad_deviceAtIndex(gamepadIndex);
	if(device == NULL) {
		return -1;
	}
	return device->buttonStates[buttonIndex];
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetControllerAxesStates
 * Signature: (F[])V
 */
JNIEXPORT void JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetControllerAxesStates(
		JNIEnv *env, jobject obj, jint gamepadIndex, jfloatArray axesArray)
{
	unsigned int axisIndex;

	struct Gamepad_device * device = Gamepad_deviceAtIndex(gamepadIndex);
	if(device == NULL) {
		printf("ERROR: Can't update axes of device!\n");
		return;
	}

	jfloat *states = (*env)->GetFloatArrayElements(env, axesArray, NULL);

	for (axisIndex = 0; axisIndex <= device->numAxes; axisIndex++) {
		states[axisIndex] = device->axisStates[axisIndex];
	}

	(*env)->ReleaseFloatArrayElements(env, axesArray, states, 0);
}

/*
 * Class:     org_gamepad4j_desktop_GamepadJniWrapper
 * Method:    natGetControllerAxisState
 * Signature: (B[])V
 */
JNIEXPORT jfloat JNICALL Java_org_gamepad4j_desktop_GamepadJniWrapper_natGetControllerAxisState(
		JNIEnv *env, jobject obj, jint gamepadIndex, jint axisIndex)
{
	struct Gamepad_device * device = Gamepad_deviceAtIndex(gamepadIndex);
	if(device == NULL) {
		printf("ERROR: Can't update axis of device!\n");
		return -1;
	}
	// TODO: Bounds check for "axisIndex" variable
	float result = device->axisStates[axisIndex];
	return result;
}


