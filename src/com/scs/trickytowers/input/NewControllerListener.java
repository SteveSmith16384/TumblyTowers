package com.scs.trickytowers.input;

public interface NewControllerListener {

	void newController(IInputDevice input);

	void controllerRemoved(IInputDevice input);

}
