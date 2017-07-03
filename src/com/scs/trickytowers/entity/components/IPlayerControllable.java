package com.scs.trickytowers.entity.components;

import org.jbox2d.common.Vec2;

public interface IPlayerControllable {

	Vec2 getPosition();
	
	void processInput();
	
}
