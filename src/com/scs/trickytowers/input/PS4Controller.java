package com.scs.trickytowers.input;

import org.gamepad4j.ButtonID;
import org.gamepad4j.IController;
import org.gamepad4j.IStick;
import org.gamepad4j.StickID;
import org.gamepad4j.StickPosition;

import ssmith.util.RealtimeInterval;

public final class PS4Controller implements IInputDevice {

	private IController gamepad;
	private RealtimeInterval lrTimer = new RealtimeInterval(200, true);
	private RealtimeInterval spinTimer = new RealtimeInterval(200, true);

	public PS4Controller(IController _gamepad) {
		gamepad = _gamepad;
	}


	@Override
	public boolean isLeftPressed() {
		if (lrTimer.hitInterval()) {
			StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
			//return pos.getDirection() == DpadDirection.LEFT;
			//Statics.p("Left=" + pos.getDegree());
			return pos.getDegree() > 252 && pos.getDegree() < 360; // 90=right, 270=left
		} else {
			return false;
		}

	}


	@Override
	public boolean isRightPressed() {
		if (lrTimer.hitInterval()) {
			StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
			//return pos.getDirection() == DpadDirection.RIGHT;
			return pos.getDegree() > 17 && pos.getDegree() < 152; // 90=right, 270=left
		} else {
			return false;
		}
	}


	@Override
	public boolean isFirePressed() {
		return gamepad.isButtonPressed(ButtonID.FACE_DOWN);
	}


	@Override
	public boolean isSpinLeftPressed() {
		if (spinTimer.hitInterval()) {
			//StickPosition pos = gamepad.getStick(StickID.RIGHT).getPosition();
			//return pos.getDirection() == DpadDirection.LEFT;
			return gamepad.isButtonPressed(ButtonID.FACE_LEFT);
		} else {
			return false;
		}
	}


	@Override
	public boolean isSpinRightPressed() {
		if (spinTimer.hitInterval()) {
			//StickPosition pos = gamepad.getStick(StickID.RIGHT).getPosition();
			//return pos.getDirection() == DpadDirection.RIGHT;
			//return pos.getDegree() > 107 && pos.getDegree() < 252;
			return gamepad.isButtonPressed(ButtonID.FACE_RIGHT);
		} else {
			return false;
		}
	}


	@Override
	public float getStickDistance() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		/*if (Statics.DEBUG) {
			Statics.p("Dist=" + pos.getDistanceToCenter());
		}*/
		return pos.getDistanceToCenter();
	}


	@Override
	public int getAngle() {
		IStick leftStick = gamepad.getStick(StickID.LEFT);
		StickPosition pos = leftStick.getPosition();
		return (int)pos.getDegree() -90; // 0=up, 90=right
	}


	@Override
	public int getID() {
		return gamepad.getDeviceID();
	}


	@Override
	public String toString() {
		return "PS4Controller:" + getID();
	}


	@Override
	public void clearInputs() {
		// TODO Auto-generated method stub

	}

}
