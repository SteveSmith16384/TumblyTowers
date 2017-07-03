package com.scs.trickytowers.entity;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import ssmith.util.Timer;

import com.scs.trickytowers.BodyUserData;
import com.scs.trickytowers.JBox2DFunctions;
import com.scs.trickytowers.Main;
import com.scs.trickytowers.entity.components.IProcessable;

public class VibratingPlatform extends PhysicalEntity implements IProcessable {

	private Timer timer = new Timer(1000);
	
	public VibratingPlatform(Main main, float x, float y, float width) {
		super(main, "VibratingPlatform");
		
		BodyUserData bud = new BodyUserData("Rectangle", Color.red, this);
		this.body = JBox2DFunctions.AddRectangle(bud, main.world, x, y, width, 5f, BodyType.DYNAMIC, 0.1f, 1f, 2f);
	}

	
	@Override
	public void preprocess(long interpol) {
		if (timer.hasHit(interpol)) {
			this.body.applyForceToCenter(new Vec2(0, -1000));
		}
		
	}

	
	@Override
	public void postprocess(long interpol) {
		
	}

}
