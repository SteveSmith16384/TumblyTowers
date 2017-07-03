package com.scs.trickytowers.input;

import org.gamepad4j.Controllers;

public final class DeviceShutdownHook extends Thread {

	public DeviceShutdownHook() {
		super(DeviceShutdownHook.class.getSimpleName());
		
	}
	
	
	public void run() {
		if (DeviceThread.USE_CONTROLLERS) {
			Controllers.shutdown();
		}
	}

}
