package com.scs.trickytowers.entity.systems;

import org.jbox2d.common.Vec2;

import ssmith.lang.Functions;

import com.scs.trickytowers.Main;
import com.scs.trickytowers.Player;
import com.scs.trickytowers.Statics;
import com.scs.trickytowers.entity.shapes.AbstractShape;
import com.scs.trickytowers.entity.shapes.LShape;
import com.scs.trickytowers.entity.shapes.Rectangle;

public class PlayerInputSystem {

	//private static final float FORCE = 10000;
	//private static final float TORQUE_FORCE = 100000f;

	private Main main;

	public PlayerInputSystem(Main _main) {
		main = _main;
	}


	public void process(Player player) {
		if (player.currentShape != null && player.currentShape.body != null) { // body is null if shape dropped of bottom
			if (player.currentShape.body.getWorldCenter().equals(player.prevPos) == false) { // Still moving
				player.prevPos.set(player.currentShape.body.getWorldCenter());
				if (player.currentShape.collided == false) {
					Vec2 newPos = new Vec2(player.currentShape.body.getWorldCenter());
					float newAngle = player.currentShape.body.getAngle();
					if (player.input.isLeftPressed()) {
						newPos.x--;
						//player.currentShape.body.applyForceToCenter(new Vec2(-FORCE, 0));
					} else if (player.input.isRightPressed()) {
						//player.currentShape.body.applyForceToCenter(new Vec2(FORCE, 0));
						newPos.x++;
					} else if (player.input.isUpPressed()) {
						newAngle -= 0.1f;
						//player.currentShape.body.applyTorque(TORQUE_FORCE);
					} else if (player.input.isDownPressed()) {
						newAngle += 0.1f;
						//player.currentShape.body.applyForceToCenter(new Vec2(0, FORCE));
					}
					player.currentShape.body.setTransform(newPos, newAngle);
				}
			} else { // Stopped moving
				if (player.currentShape.getPosition().y < Statics.WIN_HEIGHT) {
					main.playerWon(player);
				}
				player.currentShape = null;
			}
		} else {
			// Drop new shape
			player.currentShape = getRandomShape(player.getShapeStartX());
			main.addEntity(player.currentShape);
		}
	}


	public AbstractShape getRandomShape(float x) {
		int i = Functions.rnd(0, 3);
		switch (i) {
		case 0: // Square
			return new Rectangle(main, Statics.STD_CELL_SIZE*2, Statics.STD_CELL_SIZE*2, x);
			
		case 1:
			return new LShape(main, x, 10);
			
		case 2: // Long and thin
			return new Rectangle(main, Statics.STD_CELL_SIZE*4, Statics.STD_CELL_SIZE, x);
			
		case 3: // Rectangle
			return new Rectangle(main, Statics.STD_CELL_SIZE*2, Statics.STD_CELL_SIZE*3, x);
			
		default:
			throw new RuntimeException("Unknown shape: " + i);
		}
	}

}
