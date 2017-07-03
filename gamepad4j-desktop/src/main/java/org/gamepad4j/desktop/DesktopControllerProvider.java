/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.desktop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.gamepad4j.ControllerListenerAdapter;
import org.gamepad4j.IControllerListener;
import org.gamepad4j.IControllerProvider;
import org.gamepad4j.util.Log;

/**
 * Controller provider for desktop systems (Linux, MacOS X, Windows).
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class DesktopControllerProvider implements IControllerProvider {
	
	/** Stores controller listeners. */
	private ControllerListenerAdapter listeners = new ControllerListenerAdapter();

	public static GamepadJniWrapper jniWrapper = null;

	/** Map of all connected controllers (deviceID / controller). */
	private static Map<Integer, DesktopController> connected = new HashMap<Integer, DesktopController>();
	
	/** Stores the controllers instance pool. */
	private static DesktopController[] controllerPool = new DesktopController[16];
	
	/** Stores the number of connected controllers. */
	private static int numberOfControllers = -1;
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IControllerProvider#initialize()
	 */
	@Override
	public void initialize() {
		jniWrapper = new GamepadJniWrapper();
		jniWrapper.initialize();
		for(int i = 0; i < controllerPool.length; i++) {
			controllerPool[i] = new DesktopController(-1);
		}
		System.out.flush();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IControllerProvider#release()
	 */
	@Override
	public void release() {
		jniWrapper.natRelease();
	}

	/**
	 * Returns a controller holder instance from the pool and
	 * sets its code to the given value.
	 * 
	 * @param code The code to set for the controller instance.
	 * @return The controller holder (or null if there was none free).
	 */
	private synchronized static DesktopController getInstanceFromPool(int index) {
		for(int i = 0; i < controllerPool.length; i++) {
			if(controllerPool[i] != null) {
				DesktopController reference = controllerPool[i]; 
				reference.setIndex(index);
//				connected.put(code, reference);
				controllerPool[i] = null;
				return reference;
			}
		}
		return null;
	}

	/**
	 * Returns the given controller instance to the pool.
	 * 
	 * @param controller The controller instance to return (must not be null).
	 */
	private synchronized static void returnInstanceToPool(DesktopController controller) {
		for(int i = 0; i < controllerPool.length; i++) {
			if(controllerPool[i] == null) {
				controllerPool[i] = controller;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IControllerProvider#checkControllers()
	 */
	@Override
	public synchronized void checkControllers() {
		jniWrapper.natDetectPads();
		for(DesktopController controller : this.connected.values()) {
			controller.setChecked(false);
		}
		
		
		
		float value = jniWrapper.natGetControllerAxisState(0, 8);
//		System.err.println(">> Axis 8: " + value);
		
		
		// 1st check which controllers are (still) connected
		int newNumberOfControllers = jniWrapper.natGetNumberOfPads();
		if(newNumberOfControllers != this.numberOfControllers) {
			this.numberOfControllers = newNumberOfControllers;
			if(Log.debugEnabled) {
				Log.logger.debug("Number of controllers: " + this.numberOfControllers);
			}
		}
//		Log.log("Check for newly connected controllers...");
		for(int ct = 0; ct < this.numberOfControllers; ct++) {
			int connectedId = jniWrapper.natGetDeviceID(ct);
			if(connectedId != -1) {
				DesktopController controller = this.connected.get(connectedId);
				if(controller != null) {
					controller.setChecked(true);
				} else {
					DesktopController newController = getInstanceFromPool(ct);
					if(newController == null) {
						throw new IllegalStateException("** DesktopController instance pool exceeded! **");
					}
					newController.setChecked(true);
					jniWrapper.updateControllerInfo(newController);
					Log.logger.debug("===> device ID now: " + newController.getDeviceTypeIdentifier());
					if(Mapping.hasMapping(newController)) {
						DesktopControllerProvider.connected.put(newController.getDeviceID(), newController);
						if(Log.infoEnabled) {
							Log.logger.info("***********************************************************************");
							Log.logger.info("Newly connected controller found: " + newController.getDeviceID()
									+ " (" + Integer.toHexString(newController.getVendorID()) + "/"
									+ Integer.toHexString(newController.getProductID())
									+ ") / " + newController.getDescription());
							Log.logger.info("***********************************************************************");
						}
						listeners.getListeners().get(0).connected(newController);
					} else {
						newController.setChecked(false);
						returnInstanceToPool(newController);
						if(Log.infoEnabled) {
							Log.logger.info("***********************************************************************");
							Log.logger.info("Newly connected controller found: " + newController.getDeviceID()
									+ " (" + Integer.toHexString(newController.getVendorID()) + "/"
									+ Integer.toHexString(newController.getProductID())
									+ ") / " + newController.getDescription());
							Log.logger.info("Controller disabled by API because no mapping is available.");
							Log.logger.info("***********************************************************************");
						}
					}
				}
			}
		}

		// 2nd remove the controllers not found in the first loop
//		Log.log("Check for disconnected controllers...");
		Iterator<Entry<Integer, DesktopController>> iter = this.connected.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Integer, DesktopController> entry = iter.next();
			DesktopController controller = entry.getValue();
			if(!controller.isChecked()) {
				if(Log.infoEnabled) {
					Log.logger.info("Controller disconnected: " + controller.getDeviceID() + " / " + controller.getDescription());
				}
				listeners.getListeners().get(0).disConnected(controller);
				returnInstanceToPool(controller);
				// Must be removed from map with iterator, otherwise
				// ConcurrentModificationException will occur
				iter.remove();
			}
		}
		
		// 3rd update the state of all remaining controllers
//		Log.log("Update controllers...");
		for(DesktopController controller : this.connected.values()) {
			jniWrapper.updateControllerStatus(controller);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IControllerProvider#addListener(org.gamepad4j.util.IControllerListener)
	 */
	@Override
	public void addListener(IControllerListener listener) {
		this.listeners.addListener(listener);
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IControllerProvider#removeListener(org.gamepad4j.util.IControllerListener)
	 */
	@Override
	public void removeListener(IControllerListener listener) {
		this.listeners.removeListener(listener);
	}
}
