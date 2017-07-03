/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Enumerations of all the possible directions of a D-Pad.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public enum DpadDirection {

	UP(1),
	UP_RIGHT(3),
	RIGHT(2),
	DOWN_RIGHT(6),
	DOWN(4),
	DOWN_LEFT(12),
	LEFT(8),
	UP_LEFT(9),
	NONE(0)
	;
	
	DpadDirection(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	private int value = -1;
	
	public static DpadDirection fromIntValue(int value) {
		for(DpadDirection direction : values()) {
			if(direction.value == value) {
				return direction;
			}
		}
		return NONE;
	}
}
