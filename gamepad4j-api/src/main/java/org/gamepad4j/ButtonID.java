/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Enumeration of IDs of gamepad buttons.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public enum ButtonID {

	/** Digital face buttons */
	FACE_UP("Upper face-button"),
	FACE_DOWN("Lower face-button"),
	FACE_LEFT("Left face-button"),
	FACE_RIGHT("Right face-button"),
	
	/** Digital shoulder buttons */
	SHOULDER_LEFT_UP("Left upper shoulder button"),
	SHOULDER_LEFT_DOWN("Left lower shoulder button"),
	SHOULDER_RIGHT_UP("Right upper shoulder button"),
	SHOULDER_RIGHT_DOWN("Right lower shoulder button"),

	/** Thumbsticks (as buttons) */
	LEFT_ANALOG_STICK("Left analog stick"),
	RIGHT_ANALOG_STICK("Right analog stick"),

	/** Special buttons */
	ACCEPT("Accept"),
	CANCEL("Cancel"),
	MENU("Menu"),
	HOME("Home"),
	BACK("Back"),
	PAUSE("Pause"),
	START("Start"),

	/** D-Pad (which may in fact be analog */
	D_PAD_UP("D-Pad Up"),
	D_PAD_DOWN("D-Pad Down"),
	D_PAD_LEFT("D-Pad Left"),
	D_PAD_RIGHT("D-Pad Right"),
	
	UNKNOWN("Unknown")
	;
	
	/** Stores this buttons default text label. */
	private String defaultLabel = "";
	
	/**
	 * Creates a button deviceID holder.
	 * 
	 * @param label The default text label for this button.
	 */
	ButtonID(String label) {
		this.defaultLabel = label;
	}
	
	/**
	 * Returns the default text label for this button.
	 * 
	 * @return The default text label.
	 */
	public String getLabel() {
		return this.defaultLabel;
	}

	/**
	 * Returns the ButtonID object which has the same
	 * name as the given string value.
	 * 
	 * @param value The ButtonID name to look for.
	 * @return The ButtonID holder, or null.
	 */
	public static ButtonID getButtonIDfromString(String value) {
		for(ButtonID id : values()) {
			if(id.name().equals(value)) {
				return id;
			}
		}
		return null;
	}
}
