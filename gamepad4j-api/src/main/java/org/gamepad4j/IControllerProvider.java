/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;


/**
 * Interface for platform-specific provider for instances
 * of IController.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface IControllerProvider {

	/**
	 * Invoked once; may perform any kind of initialization.
	 */
	void initialize();
	
	/**
	 * Invoked once when the game terminates. Can be used
	 * to free native stuff in JNI wrappers etc.
	 */
	void release();
	
	/**
	 * Checks the state of all controllers. This method is 
	 * invoked by the update-thread which may be used if the
	 * underlying implementation does not support the callback
	 * listeners.
	 */
	void checkControllers();
	
	/**
	 * Registers a listener for controller events.
	 * 
	 * @param listener The controller listener.
	 */
	public void addListener(IControllerListener listener);
	
	/**
	 * Removes a listener for controller events.
	 * 
	 * @param listener The controller listener to remove.
	 */
	public void removeListener(IControllerListener listener);
}
