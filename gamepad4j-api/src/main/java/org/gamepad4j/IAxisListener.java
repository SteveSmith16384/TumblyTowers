/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Interface for listening to axis events.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IAxisListener {

	/**
	 * Whenever the axis value changes, this method is called on
	 * the listeners.
	 * 
	 * @param value The current value of the axis.
	 */
	public void moved(float value);
}
