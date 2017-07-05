package com.scs.trickytowers.entity;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.trickytowers.Main_TumblyTowers;

public abstract class Entity { 
	
	private static int next_id = 0;

	public int id;
	public Point tmpPoint = new Point();
	protected Main_TumblyTowers main;
	public String name; // todo - remove
	
	public Entity(Main_TumblyTowers _main, String _name) {
		super();
		
		main = _main;
		id = next_id++;
		name =_name;
	}
	

	public abstract void cleanup(World world);
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + super.toString();
	}





}
