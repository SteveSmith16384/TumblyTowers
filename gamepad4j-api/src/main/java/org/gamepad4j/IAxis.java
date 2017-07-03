/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Interface for handling one axis of joy- or gamepad-sticks, touchpads,
 * accelerometers etc.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IAxis {

	/**
	 * Returns the deviceID of this axis.
	 * 
	 * @return The axis deviceID (type).
	 */
	AxisID getID();
	
	/**
	 * Returns the number of the axis.
	 * 
	 * @return The axis number.
	 */
	int getNumber();
	
	/**
	 * Returns the value of this axis (usually a value
	 * between -1.0 and 1.0).
	 * 
	 * @return The value of this axis.
	 */
	float getValue();
	
	/**
	 * Adds a listener for events of this axis.
	 * 
	 * @param listener The axis listener.
	 */
	void addAxisListener(IAxisListener listener);
}
