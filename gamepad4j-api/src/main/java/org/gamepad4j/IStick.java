/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Interface for handling one joy- or gamepad-stick, touchpad,
 * accelerometer etc.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IStick {

	/**
	 * Returns the deviceID of this stick.
	 * 
	 * @return The stick deviceID.
	 */
	StickID getID();

	/**
	 * Returns a holder of some pre-calculated stick position
	 * data (such as the degree and the distance to the center).
	 * 
	 * @return The stick position data holder.
	 */
	StickPosition getPosition();
	
	/**
	 * Returns all axes of this stick.
	 * 
	 * @return The axes of this stick.
	 */
	IAxis[] getAxes();
	
	/**
	 * Returns a specific axis.
	 * 
	 * @param axis The axis to return.
	 * @return The given axis.
	 * @throws IllegalArgumentException If there is no such axis.
	 */
	IAxis getAxis(AxisID axis) throws IllegalArgumentException;
}
