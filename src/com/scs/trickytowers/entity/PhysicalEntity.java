package com.scs.trickytowers.entity;

import java.awt.Graphics;
import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import com.scs.trickytowers.Main_TumblyTowers;
import com.scs.trickytowers.entity.components.IDrawable;
import com.scs.trickytowers.entity.components.IGetPosition;
import com.scs.trickytowers.entity.systems.DrawingSystem;

public abstract class PhysicalEntity extends Entity implements IDrawable, IGetPosition {

	protected Point tmp = new Point();
	public Body body;
	
	public PhysicalEntity(Main_TumblyTowers _main, String _name) {
		super(_main, _name);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawShape(tmp, g, body);
		
	}


	@Override
	public Vec2 getPosition() {
		return body.getWorldCenter();
	}


	/*public void applyForceToCenter(Vec2 vec) {
		body.applyForceToCenter(vec);
		
	}*/


	/*public void applyLinearImpulse(Vec2 vec) {
		body.applyLinearImpulse(vec, Statics.VEC_CENTRE, true);
		
	}*/

	
	@Override
	public void cleanup(World world) {
		world.destroyBody(body);
		body = null;
	}


}
