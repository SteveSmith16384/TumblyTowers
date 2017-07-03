/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles adding, removing and accessing controller listeners.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class ControllerListenerAdapter {

	/** The list of registered listeners. */
	private List<IControllerListener> listeners = new ArrayList<IControllerListener>();

	/**
	 * Returns the list of listeners.
	 * 
	 * @return The list of controller listeners.
	 */
	public List<IControllerListener> getListeners() {
		return this.listeners;
	}
	
	/**
	 * Registers a listener for controller events.
	 * 
	 * @param listener The controller listener.
	 */
	public void addListener(IControllerListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener for controller events.
	 * 
	 * @param listener The controller listener to remove.
	 */
	public void removeListener(IControllerListener listener) {
		listeners.remove(listener);
	}
}
