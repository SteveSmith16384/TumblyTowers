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

	private Main main;

	public PlayerInputSystem(Main _main) {
		main = _main;
	}


	public void process(Player player) {
		if (player.currentShape != null && player.currentShape.body != null) { // body is null if shape dropped of bottom
			//if (player.currentShape.body.getLinearVelocity().length() <= 0) {//.isActive()) {//player.currentShape.body.getWorldCenter().y > player.prevY) { // Still moving
			if (player.currentShape.body.getWorldCenter().y < Statics.WIN_HEIGHT || Math.abs(player.currentShape.body.getWorldCenter().y - player.prevY) > 0.1f) { // Still moving
				player.prevY = player.currentShape.body.getWorldCenter().y;
				if (player.currentShape.collided == false) {
					Vec2 newPos = new Vec2(player.currentShape.body.getWorldCenter());
					float newAngle = player.currentShape.body.getAngle();
					if (player.input.isLeftPressed()) {
						if (player.currentShape.body.getWorldCenter().x > main.getLeftBucketPos(player.id_ZB)) {
							newPos.x--;
						}
					} else if (player.input.isRightPressed()) {
						if (player.currentShape.body.getWorldCenter().x < main.getRightBucketPos(player.id_ZB)) {
							newPos.x++;
						}
					} else if (player.input.isUpPressed()) {
						newAngle -= 0.1f;
					} else if (player.input.isDownPressed()) {
						newAngle += 0.1f;
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
			//player.prevY = player.currentShape.getPosition().y-1;
		}
	}


	public AbstractShape getRandomShape(float x) {
		int i = Functions.rnd(0, 3);
		switch (i) {
		case 0: // Square - todo -make rnd sizes
			return new Rectangle(main, Statics.STD_CELL_SIZE*3, Statics.STD_CELL_SIZE*3, x);

		case 1:
			return new LShape(main, x, Statics.STD_CELL_SIZE);

		case 2: // Long and thin
			return new Rectangle(main, Statics.STD_CELL_SIZE*Functions.rnd(3, 7), Statics.STD_CELL_SIZE, x);

		case 3: // Rectangle - todo -make rnd sizes
			return new Rectangle(main, Statics.STD_CELL_SIZE*2, Statics.STD_CELL_SIZE*4, x);

		default:
			throw new RuntimeException("Unknown shape: " + i);
		}
	}

}
