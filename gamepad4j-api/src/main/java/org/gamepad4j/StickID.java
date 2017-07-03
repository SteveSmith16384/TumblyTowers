/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Enumeration of IDs of gamepad sticks, pads, accelerometers etc.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public enum StickID {
	
	LEFT,
	RIGHT,
	D_PAD,
	
	UNKNOWN
	;

	/**
	 * Returns the StickID object which has the same
	 * name as the given string value.
	 * 
	 * @param value The StickID name to look for.
	 * @return The StickID holder, or null.
	 */
	public static StickID getStickIDfromString(String value) {
		for(StickID id : values()) {
			if(id.name().equals(value)) {
				return id;
			}
		}
		return null;
	}
}
