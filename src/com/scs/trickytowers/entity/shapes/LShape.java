package com.scs.trickytowers.entity.shapes;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import com.scs.trickytowers.BodyUserData;
import com.scs.trickytowers.JBox2DFunctions;
import com.scs.trickytowers.Main;

public class LShape extends AbstractShape {
	
	public LShape(Main _main, float x, float unit_size) {
		super(_main, "LShape");

		BodyUserData bud = new BodyUserData("LShape", AbstractShape.getRandomColour(), this);

		Vec2 verts[] = new Vec2[6];
		verts[0] = new Vec2(x+0, 0);
		verts[1] = new Vec2(x+unit_size, 0);
		verts[2] = new Vec2(x+unit_size, unit_size*2);
		verts[3] = new Vec2(x+(unit_size*2), unit_size*2);
		verts[4] = new Vec2(x+(unit_size*2), unit_size*3);
		verts[5] = new Vec2(x+0, (unit_size*3));
		body = JBox2DFunctions.CreateComplexShape(bud, main.world, verts, BodyType.DYNAMIC, RESTITUTION, FRICTION, WEIGHT);
	}

	
}
