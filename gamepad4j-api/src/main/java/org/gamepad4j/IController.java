/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Interface for handling a game controller. 
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IController {

	/**
	 * Returns an identifier which is basically a combination of the
	 * product ID and the vendor ID. This combined identifier is
	 * used to retrieve the corresponding button / stick mappings.
	 * 
	 * @return The device identifier.
	 */
	long getDeviceTypeIdentifier();
	
	/**
	 * Returns the deviceID of the joypad controller.
	 * This is usually the USB device deviceID.
	 * 
	 * @return The controller device deviceID value, or -1, if it's not available.
	 */
	int getDeviceID();

	/**
	 * Returns the vendor deviceID of the joypad controller.
	 * This is usually the USB vendor deviceID.
	 * 
	 * @return The controller vendor deviceID value, or -1, if it's not available.
	 */
	int getVendorID();

	/**
	 * Returns the product deviceID of the joypad controller.
	 * This is usually the USB product deviceID.
	 * 
	 * @return The controller product deviceID value, or -1, if it's not available.
	 */
	int getProductID();
	
	/**
	 * A descriptive text (if available), like "Xbox 360 Controller".
	 * 
	 * @return The description text (or empty).
	 */
	String getDescription();

	/**
	 * Returns all the sticks of this controller. This also includes
	 * touchpads, accelerometers etc. Basically every kind of analog
	 * multi-axis input.
	 * 
	 * @return The sticks on this controller.
	 */
	IStick[] getSticks();
	
	/**
	 * Returns the given stick.
	 * 
	 * @param stick The stick to return.
	 * @return The requested stick.
	 * @throws IllegalArgumentException If there is no such stick.
	 */
	IStick getStick(StickID stick) throws IllegalArgumentException;
	
	/**
	 * Returns an array with all axes of this controller. Note:
	 * It is usually easier to work with the stick-related methods
	 * instead of directly using axes. However, especially in case
	 * of an unknown or non-standard device, this method allows to
	 * handle all axes manually.
	 * 
	 * @return The array with the axes. May be empty (0 entries), but not null.
	 */
	IAxis[] getAxes();
	
	/**
	 * Returns the current direction on the D-pad.
	 * 
	 * @return The current direction.
	 */
	DpadDirection getDpadDirection();
	
	/**
	 * Returns the current direction on the D-pad, but only for one single invocation. 
	 * After that, the method will return "DpadDirection.NONE", even if the d-pad is still 
	 * pressed, until it is released or changes direction. This can be used when the user 
	 * should press the d-pad repeatedly instead of just keeping it pressed.
	 * 
	 * @return The current direction.
	 */
	DpadDirection getDpadDirectionOnce();
	
	/**
	 * Returns a list of all buttons of this controller.
	 * 
	 * @return The buttons on this controller.
	 */
	IButton[] getButtons();

	/**
	 * Returns a reference to a specific button on this controller. This method
	 * should be used for "non-standard" buttons for which none of the predefined
	 * button IDs work.
	 * 
	 * @param buttonIndex The code of this button (0 - n).
	 * @return The reference of that button (null if it does not exist).
	 */
	IButton getButton(int buttonIndex);
	
	/**
	 * Returns a reference to a specific button on this controller.
	 * 
	 * @param buttonID The ID of the given button.
	 * @return The reference of that button (null if it does not exist).
	 */
	IButton getButton(ButtonID buttonID);
	
	/**
	 * Convenience method for checking if a given button (digital or analog)
	 * is currently pressed.
	 * 
	 * @param buttonID The ID of the button.
	 * @return True if it's pressed.
	 */
	boolean isButtonPressed(ButtonID buttonID);
	
	/**
	 * Convenience method for checking if a given button (digital or analog)
	 * is currently pressed, but only for one single invocation. After that,
	 * the method will return 'false', even if the button is still pressed,
	 * until it is released once. This can be used when the user should press
	 * the button repeatedly instead of just keeping it pressed.
	 * 
	 * @param buttonID The ID of the button.
	 * @return True if it's pressed.
	 */
	boolean isButtonPressedOnce(ButtonID buttonID);
	
	/**
	 * Returns the current pressure on the given trigger.
	 * 
	 * @param triggerID The ID of the trigger 
	 * @return The pressure as a value between 0.0 (not pressed) and 1.0 (fully pressed).
	 * @throws IllegalArgumentException If the given trigger does not exist.
	 */
	float getTriggerPressure(TriggerID triggerID) throws IllegalArgumentException;
	
	/**
	 * Returns a list of all buttons of this controller.
	 * 
	 * @return The buttons on this controller.
	 */
	ITrigger[] getTriggers();
	
	/**
	 * Returns a reference to a specific trigger on this controller.
	 * 
	 * @param triggerID The ID of the trigger 
	 * @return The reference of that trigger (null if it does not exist).
	 */
	ITrigger getTrigger(TriggerID triggerID);
}
