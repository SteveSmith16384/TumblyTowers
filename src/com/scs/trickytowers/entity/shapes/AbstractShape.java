package com.scs.trickytowers.entity.shapes;

import com.scs.trickytowers.Main;
import com.scs.trickytowers.Statics;
import com.scs.trickytowers.entity.Entity;
import com.scs.trickytowers.entity.PhysicalEntity;
import com.scs.trickytowers.entity.components.ICollideable;
import com.scs.trickytowers.entity.components.IProcessable;

public abstract class AbstractShape extends PhysicalEntity implements ICollideable, IProcessable {

	public boolean collided = false; // Can't move once collided
	
	public AbstractShape(Main _main, String name) {
		super(_main, name);
		
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
		// TODO Auto-generated method stub
		
	}

}
