package com.scs.trickytowers.entity.shapes;

import java.awt.Color;

import org.jbox2d.common.Vec2;

import com.scs.trickytowers.Main_TumblyTowers;
import com.scs.trickytowers.Statics;
import com.scs.trickytowers.entity.Entity;
import com.scs.trickytowers.entity.PhysicalEntity;
import com.scs.trickytowers.entity.components.ICollideable;
import com.scs.trickytowers.entity.components.IProcessable;

import ssmith.lang.Functions;

public abstract class AbstractShape extends PhysicalEntity implements ICollideable, IProcessable {

	protected static final float RESTITUTION = 0.001f;
	protected static final float FRICTION = .9f;
	protected static final float WEIGHT = 0.01f;

	private static Vec2 ANTI_GRAV = new Vec2(0, -11);

	public boolean collided = false; // Can't move once collided

	public AbstractShape(Main_TumblyTowers _main, String name) {
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
			// todo - sound fx
			main.removeEntity(this);
		}
	}


	public void applyDrag() {
		final float H = 0.5f;
		
		Vec2 v = body.getLinearVelocity();

		//Get the square of the velocity by computing the square of the distance from the origin
		float vSqrd = v.lengthSquared();//.dst2(new Vec2());

		//Calculate the magnitude of the drag force
		float fMag = H*vSqrd;

		//Calculate the drag force vector to apply
		//We do this by taking the norm of the velocity and negating it to get the direction.
		//That vector is multiplied by the magnitude to get the drag force we want to apply
		Vec2 fd = v.clone();
		fd.normalize();//.*fMag;

		//Finally we communicate this to box2d by calling applyForceToCenter
		body.applyForceToCenter(fd.mul(-1 * fMag));
	}


	@Override
	public void postprocess(long interpol) {

	}

}
