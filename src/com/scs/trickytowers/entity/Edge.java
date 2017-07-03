package com.scs.trickytowers.entity;

import java.awt.Color;

import org.jbox2d.dynamics.BodyType;

import com.scs.trickytowers.BodyUserData;
import com.scs.trickytowers.JBox2DFunctions;
import com.scs.trickytowers.Main;

public class Edge extends PhysicalEntity {

	public Edge(Main _main, float sx, float sy, float ex, float ey) {
		super(_main, "Edge");
		
		BodyUserData bud = new BodyUserData("Edge", Color.black, this);
		body = JBox2DFunctions.AddEdgeShapeByTL(main.world, bud, sx, sy, ex, ey, BodyType.STATIC, 0, 1, 1);

	}

}
