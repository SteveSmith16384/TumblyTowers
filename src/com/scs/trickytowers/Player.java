package com.scs.trickytowers;

import com.scs.trickytowers.entity.shapes.AbstractShape;
import com.scs.trickytowers.input.IInputDevice;

public class Player {
	
	public int score, id_ZB;
	public IInputDevice input;
	public AbstractShape currentShape;
	public float prevY;
	private Main main;

	private static int nextId = 0;
	
	public Player(Main _main, IInputDevice _input) {
		super();
		
		id_ZB = nextId++;
		main = _main;
		input = _input;
	}
	
	
	public float getShapeStartX() {
		return main.getCentreBucketPos(id_ZB);
	}

}
