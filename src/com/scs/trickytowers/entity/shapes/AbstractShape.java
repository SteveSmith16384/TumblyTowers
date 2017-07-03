package com.scs.trickytowers.entity.shapes;

import java.awt.Color;

import ssmith.lang.Functions;

import com.scs.trickytowers.Main;
import com.scs.trickytowers.Statics;
import com.scs.trickytowers.entity.Entity;
import com.scs.trickytowers.entity.PhysicalEntity;
import com.scs.trickytowers.entity.components.ICollideable;
import com.scs.trickytowers.entity.components.IProcessable;

public abstract class AbstractShape extends PhysicalEntity implements ICollideable, IProcessable {

	protected static final float RESTITUTION = .0f;
	protected static final float FRICTION = 1f;
	protected static final float WEIGHT = 0.5f;
	
	public boolean collided = false; // Can't move once collided
	
	public AbstractShape(Main _main, String name) {
		super(_main, name);
		
	}

	
	protected static Color getRandomColour() {
		int i = Functions.rnd(1, 5);
		switch (i) {
		case 0: return Color.yellow;
		case 1: return Color.magenta;
		case 2: return Color.green;
		case 3: return Color.cyan;
		case 4: return Color.orange;
		default: return Color.pink;
			
		}
	}
	
	
	@Override
	public void collided(Entity other) {
		this.collided = true;
		
	}


	@Override
	public void preprocess(long interpol) {
		if (this.body.getWorldCenter().y > Statics.WORLD_HEIGHT_LOGICAL) {
			main.removeEntity(this);
		}
	}


	@Override
	public void postprocess(long interpol) {
		
	}

}
