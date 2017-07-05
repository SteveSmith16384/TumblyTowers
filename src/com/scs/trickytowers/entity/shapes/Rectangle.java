package com.scs.trickytowers.entity.shapes;

import org.jbox2d.dynamics.BodyType;

import com.scs.trickytowers.BodyUserData;
import com.scs.trickytowers.JBox2DFunctions;
import com.scs.trickytowers.Main_TumblyTowers;

public class Rectangle extends AbstractShape {
	
	public Rectangle(Main_TumblyTowers _main, float width, float height, float sx) {
		super(_main, "Rectangle");

		BodyUserData bud = new BodyUserData("Rectangle", AbstractShape.getRandomColour(), this);
		body = JBox2DFunctions.AddRectangle(bud, main.world, sx, 0, width, height, BodyType.DYNAMIC, RESTITUTION, FRICTION, WEIGHT);
	}

	
}
