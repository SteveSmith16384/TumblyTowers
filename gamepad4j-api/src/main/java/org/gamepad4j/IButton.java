/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Provides functionality of a single button, which may be
 * a face-button or a shoulder button (not a trigger).
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IButton {

	/**
	 * Returns the deviceID of this button. May be "UNKNOWN" if
	 * it's not a known, mapped key (but never null).
	 * 
	 * @return The deviceID of the button. If it is "ButtonID.UNKNOWN", the method
	 *         "getCode()" can be used to query the actual numerical value.
	 */
	ButtonID getID();
	
	/**
	 * Returns the numerical code of this button. NOTE: This value can vary for 
	 * one and the same button on a given joypad, depending on the operating 
	 * system the pad is used with.
	 * 
	 * @return The buttons code.
	 */
	int getCode();
	
	/**
	 * Returns true if this button is currently pressed.
	 * 
	 * @return True if the button is pressed.
	 */
	boolean isPressed();
	
	/**
	 * Convenience method for checking if this button (digital or analog)
	 * is currently pressed, but only for one single invocation. After that,
	 * the method will return 'false', even if the button is still pressed,
	 * until it is released once. This can be used when the user should press
	 * the button repeatedly instead of just keeping it pressed.
	 * 
	 * @return True if it's pressed.
	 */
	boolean isPressedOnce();
	
	/**
	 * Returns the name of the message resource key which holds
	 * the text label for this button (which might be something
	 * like "Triangle" for the upper face button on a PS3 DualShock 3 pad).
	 * 
	 * @return The message resource key of the text label.
	 */
	String getLabelKey();
	
	/**
	 * Returns the default text label for this button. Can be used
	 * to display a message to the user such as "press button XY".
	 * For multi-lingual localization, use the "getLabelKey()" method 
	 * and externalized text labels in message resource bundles instead.
	 * 
	 * @return The default text label for this button.
	 */
	String getDefaultLabel();
}
