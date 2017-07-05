package com.scs.trickytowers.input;

public interface IInputDevice {

	int getID();
	
	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isSpinLeftPressed();
	
	boolean isSpinRightPressed();
	
	float getStickDistance();
	
	boolean isFirePressed();
	
	int getAngle();

	String toString();
	
	void clearInputs();

}
