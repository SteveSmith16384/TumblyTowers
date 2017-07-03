package com.scs.trickytowers;

import org.jbox2d.common.Vec2;

import com.scs.trickytowers.entity.shapes.AbstractShape;
import com.scs.trickytowers.input.IInputDevice;

public class Player {
	
	public int score, id;
	public IInputDevice input;
	public AbstractShape currentShape;
	public Vec2 prevPos = new Vec2();

	private static int nextId = 0;
	
	public Player(IInputDevice _input) {
		super();
		
		id = nextId++;
		input = _input;
	}
	
	
	public float getShapeStartX() {
		return Statics.WORLD_WIDTH_LOGICAL/2; //todo - check player num 
	}

}
