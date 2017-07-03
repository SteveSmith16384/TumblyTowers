package com.scs.trickytowers.input;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JFrame;

import org.gamepad4j.ButtonID;
import org.gamepad4j.Controllers;
import org.gamepad4j.IController;

import ssmith.lang.Functions;

import com.scs.trickytowers.Statics;

public final class DeviceThread extends Thread {

	public static boolean USE_CONTROLLERS = true;

	private List<IInputDevice> createdDevices = new ArrayList<>();
	private IInputDevice keyboard1, keyboard2;
	private List<NewControllerListener> listeners = new ArrayList<>();

	public DeviceThread(JFrame window) {
		super(DeviceThread.class.getSimpleName());

		this.setDaemon(true);

		try {
			Controllers.initialize();
			Runtime.getRuntime().addShutdownHook(new DeviceShutdownHook());
		} catch (Throwable ex) {
			ex.printStackTrace();
			USE_CONTROLLERS = false;
		}

		keyboard1 = new KeyboardInput(window, KeyboardInput.KEYBOARD1_ID);
		keyboard2 = new KeyboardInput(window, KeyboardInput.KEYBOARD2_ID);

	}


	public void run() {
		try {
			while (true) {
				if (USE_CONTROLLERS) {
					IController[] gamepads = null;
					Controllers.checkControllers();
					gamepads = Controllers.getControllers();

					for (IController gamepad : gamepads) {
						if (gamepad.isButtonPressed(ButtonID.FACE_DOWN)) {
							boolean found = false;
							synchronized (createdDevices) {
								/*if (!createdDevices.contains(gamepad)) {
									this.createController(new PS4Controller(gamepad));
								}*/
								for (IInputDevice exists : this.createdDevices) {
									if (exists.getID() == gamepad.getDeviceID()) {
										found = true;
										break;
									}
								}
								if (!found) {
									this.createController(new PS4Controller(gamepad));
								}
							}
						}
					}

					// Check for removed devices
					synchronized (createdDevices) {
						try {
							for (IInputDevice id : createdDevices) {
								if (id.getID() >= 0) { // Don't check keyboard
									boolean found = false;
									for (IController gamepad : gamepads) {
										if (gamepad.getDeviceID() == id.getID()) {
											found = true;
											break;
										}
									}
									if (!found) {
										this.removeController(id);
									}
								}
							} 
						} catch (ConcurrentModificationException ex) {
							// Do nothing
						}
					}
				}
				if (keyboard1.isFirePressed()) {
					synchronized (createdDevices) {
						if (createdDevices.contains(keyboard1) == false) {
							this.createController(keyboard1);
						}
					}
				}
				if (keyboard2.isFirePressed()) {
					synchronized (createdDevices) {
						if (createdDevices.contains(keyboard2) == false) {
							this.createController(keyboard2);
						}
					}
				}

				Functions.delay(500);
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private void createController(IInputDevice input) {
		synchronized (createdDevices) {
			//if (Statics.DEBUG) {
			Statics.p("Current Devices: " + this.createdDevices.size());
			Statics.p("Creating new device id:" + input.getID());
			//}
			createdDevices.add(input);
		}

		synchronized (listeners) {
			for (NewControllerListener l : this.listeners) {
				l.newController(input);
			}
		}
	}


	private void removeController(IInputDevice input) {
		//synchronized (createdDevices) {
			//if (Statics.DEBUG) {
			Statics.p("Current Devices: " + this.createdDevices.size());
			Statics.p("Removing device id:" + input.getID());
			//}
			createdDevices.remove(input);
		//}

		synchronized (listeners) {
			for (NewControllerListener l : this.listeners) {
				l.controllerRemoved(input);
			}
		}
	}


	public void addListener(NewControllerListener l) {
		synchronized (listeners) {
			this.listeners.add(l);
		}		
	}

}
