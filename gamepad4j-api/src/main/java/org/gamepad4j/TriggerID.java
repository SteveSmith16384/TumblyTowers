/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Enumeration of IDs of gamepad triggers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public enum TriggerID {
	
	/** Analog trigger buttons */
	LEFT_UP("Left upper trigger"),
	LEFT_DOWN("Left lower trigger"),
	RIGHT_UP("Right upper trigger"),
	RIGHT_DOWN("Right lower trigger"),
	
	UNKNOWN("Unknown")
	;
	
	/** Stores this buttons default text label. */
	private String defaultLabel = "";
	
	/**
	 * Creates a button deviceID holder.
	 * 
	 * @param label The default text label for this button.
	 */
	TriggerID(String label) {
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
	 * Returns the TriggerID object which has the same
	 * name as the given string value.
	 * 
	 * @param value The TriggerID name to look for.
	 * @return The TriggerID holder, or null.
	 */
	public static TriggerID getTriggerIDfromString(String value) {
		for(TriggerID id : values()) {
			if(id.name().equals(value)) {
				return id;
			}
		}
		return null;
	}
}
