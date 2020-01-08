package com.scs.trickytowers.input;

public interface IInputDevice {

	void readEvents();
	
	int getID();
	
	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isSpinLeftPressed();
	
	boolean isSpinRightPressed();
	
	boolean isFirePressed();
	
	String toString();
	
	void clearInputs();

}
