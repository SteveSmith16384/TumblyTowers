package com.scs.trickytowers.input;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public final class PS4Controller implements IInputDevice {

	private Controller gamepad;
	private long lastEventTime;
/*
	private static final int FACE_UP = 0; // todo - set
	private static final int FACE_DOWN = 0; // todo - set
	private static final int FACE_LEFT = 0; // todo - set
	private static final int FACE_RIGHT = 0; // todo - set
/*
	private RealtimeInterval leftTimer = new RealtimeInterval(150, true); // todo - make const
	private RealtimeInterval rightTimer = new RealtimeInterval(150, true);
	private RealtimeInterval spinLeftTimer = new RealtimeInterval(150, true);
	private RealtimeInterval spinRightTimer = new RealtimeInterval(150, true);
*/
	
	private boolean leftPressed, rightPressed;
	private boolean spinLeftPressed, spinRightPressed;
	private boolean firePressed;
	
	public PS4Controller(Controller _gamepad) {
		gamepad = _gamepad;
	}


	@Override
	public void readEvents() {
		gamepad.poll();

		/* Get the controllers event queue */
		EventQueue queue = gamepad.getEventQueue();

		/* Create an event object for the underlying plugin to populate */
		Event event = new Event();

		/* For each object in the queue */
		while (queue.getNextEvent(event)) {
			if (event.getNanos() < lastEventTime) {
				continue;
			}
			this.lastEventTime = event.getNanos();

			float value = event.getValue();
			//if (value == 0 || value < -0.5 || value > 0.5) {
				
				StringBuffer str = new StringBuffer(gamepad.getName());
				str.append(" at ");
				str.append(event.getNanos()).append(", ");
				Component comp = event.getComponent();
				str.append(comp.getName()).append(" changed to ");

				if (comp.isAnalog()) {
					str.append(value);
					/*if (comp.getIdentifier()) {
					
					}*/
				} else {
					if (value == 1.0f) {
						str.append("On (" + value + ")");
					} else {
						str.append("Off (" + value + ")");
					}
				}
				System.out.println(str.toString());
				
				
			//}
		}		
	}


	@Override
	public boolean isLeftPressed() {
		return leftPressed;
	}


	@Override
	public boolean isRightPressed() {
		return rightPressed;
	}


	@Override
	public boolean isFirePressed() {
		return firePressed;
	}


	@Override
	public boolean isSpinLeftPressed() {
		return spinLeftPressed;
	}


	@Override
	public boolean isSpinRightPressed() {
		return spinRightPressed;
	}


	@Override
	public int getID() {
		return gamepad.hashCode();//.getDeviceID();
	}


	@Override
	public String toString() {
		return "PS4Controller:" + getID();
	}


	@Override
	public void clearInputs() {
		this.leftPressed = false;
		this.rightPressed = false;
		this.spinLeftPressed = false;
		this.spinRightPressed = false;
		this.firePressed = false;
	}


}
