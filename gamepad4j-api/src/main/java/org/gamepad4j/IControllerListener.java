/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Interface for handling controller callback (events).
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IControllerListener {

	/**
	 * Is invoked when a controller is connected.
	 * 
	 * @param controller The controller that was connected.
	 */
	void connected(IController controller);

	/**
	 * Is invoked when a controller is disconnected.
	 * 
	 * @param controller The controller that was disconnected.
	 */
	void disConnected(IController controller);

	/**
	 * Notifies a button press.
	 * 
	 * @param controller The controller on which a button was pressed.
	 * @param button The button that was pressed. If an analog pressure value
	 *               is required, it can be queried from this object.
	 * @param buttonID The deviceID of the button that was pressed.
	 */
	void buttonDown(IController controller, IButton button, ButtonID buttonID);

	/**
	 * Notifies a button release.
	 * 
	 * @param controller The controller on which a button was released.
	 * @param button The button that was released.
	 * @param buttonID The deviceID of the button that was released.
	 */
	void buttonUp(IController controller, IButton button, ButtonID buttonID);
	
	/**
	 * TODO: Check if axis / value must be parameters too (analog sticks not implemented yet)
	 * 
	 * @param controller
	 * @param stick
	 */
	void moveStick(IController controller, StickID stick);
}
