package com.scs.trickytowers.entity.components;

import java.awt.Graphics;

import com.scs.trickytowers.entity.systems.DrawingSystem;

public interface IDrawable {

	void draw(Graphics g, DrawingSystem system);
}
