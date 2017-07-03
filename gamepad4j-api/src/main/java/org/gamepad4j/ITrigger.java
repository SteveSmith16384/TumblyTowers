/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Provides functionality of a single trigger (the analog
 * shoulder buttons).
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface ITrigger {

	/**
	 * Returns the deviceID of this trigger. May be "UNKNOWN" if
	 * it's not a known, mapped trigger (but never null).
	 * 
	 * @return The deviceID of the trigger. If it is "TriggerID.UNKNOWN", the method
	 *         "getCode()" can be used to query the actual numerical value.
	 */
	TriggerID getID();
	
	/**
	 * Returns the numerical code of this trigger. NOTE: This value can vary for 
	 * one and the same button on a given joypad, depending on the operating 
	 * system the pad is used with.
	 * 
	 * @return The buttons code.
	 */
	int getCode();

	/**
	 * Returns the analogue value of this trigger when it's pressed,
	 * given that it's an analogue button. If it's not, the method
	 * may just return 1.0 if the button is pressed, and 0.0 if it's not.
	 * 
	 * @return The pressure on the button as a value between 0.0 (not pressed)
	 *         and 1.0 (fully pressed).
	 */
	float analogValue();
	
	/**
	 * Returns the percentage of how strong this trigger is
	 * being pressed. At 0, it's not pressed at all, at 100
	 * percent it being fully pressed.
	 * 
	 * @return The percentage of the trigger pressure.
	 */
	int getPercentage();
	
	/**
	 * Returns the name of the message resource key which holds
	 * the text label for this trigger (which might be something
	 * like "Triangle" for the upper face button on a PS3 DualShock 3 pad).
	 * 
	 * @return The message resource key of the text label.
	 */
	String getLabelKey();
	
	/**
	 * Returns the default text label for this trigger. Can be used
	 * to display a message to the user such as "press button XY".
	 * For multi-lingual localization, use the "getLabelKey()" method 
	 * and externalized text labels in message resource bundles instead.
	 * 
	 * @return The default text label for this trigger.
	 */
	String getDefaultLabel();
}
