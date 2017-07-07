package com.scs.trickytowers;

import org.jbox2d.common.Vec2;

import ssmith.lang.Functions;

import com.scs.trickytowers.entity.shapes.AbstractShape;
import com.scs.trickytowers.entity.shapes.LShape;
import com.scs.trickytowers.entity.shapes.Rectangle;
import com.scs.trickytowers.input.IInputDevice;

public class Player {

	public int score, id_ZB;
	public IInputDevice input;
	public AbstractShape currentShape;
	public float prevShapeY;
	private Main_TumblyTowers main;

	private static int nextId = 0;

	public Player(Main_TumblyTowers _main, IInputDevice _input) {
		super();

		id_ZB = nextId++;
		main = _main;
		input = _input;
	}


	public void process() {
		if (currentShape != null && currentShape.body != null) { // body is null if shape dropped off the bottom
			//if (player.currentShape.body.getLinearVelocity().length() <= 0) {//.isActive()) {//player.currentShape.body.getWorldCenter().y > player.prevY) { // Still moving
			//if (currentShape.body.getWorldCenter().y < Statics.WIN_HEIGHT || Math.abs(currentShape.body.getWorldCenter().y - prevShapeY) > 0.1f) { // Still moving
			float diff = Math.abs(currentShape.body.getWorldCenter().y - prevShapeY);
			//Statics.p("Diff=" + diff);
			if (currentShape.body.getWorldCenter().y < 1 || diff > 0.01f) { // Still moving
				prevShapeY = currentShape.body.getWorldCenter().y;
				if (currentShape.collided == false) {
					Vec2 newPos = new Vec2(currentShape.body.getWorldCenter());
					float newAngle = currentShape.body.getAngle();
					if (input.isLeftPressed()) {
						if (currentShape.body.getWorldCenter().x > main.getLeftBucketPos(id_ZB)) {
							newPos.x -= Statics.STD_CELL_SIZE/2;
						}
						input.clearInputs();
					} else if (input.isRightPressed()) {
						if (currentShape.body.getWorldCenter().x < main.getRightBucketPos(id_ZB)) {
							newPos.x += Statics.STD_CELL_SIZE/2;
						}
						input.clearInputs();
					} else if (input.isSpinLeftPressed()) {
						newAngle -= Math.PI/8;//12;// 0.1f;
						input.clearInputs();
					} else if (input.isSpinRightPressed()) {
						newAngle += Math.PI/8;//12;// 0.1f;
						input.clearInputs();
					}
					currentShape.body.setTransform(newPos, newAngle);
				}
			} else { // Stopped moving
				if (currentShape.getPosition().y < Statics.WIN_HEIGHT) {
					main.playerWon(this);
				}
				Statics.p("Shape removed");
				currentShape = null;
			}
		} else {
			// Drop new shape
			Statics.p("Dropping new Shape");
			currentShape = getRandomShape(main.getShapeStartPosX(id_ZB));
			main.addEntity(currentShape);
		}
	}


	private AbstractShape getRandomShape(float x) {
		/*int i = Functions.rnd(0, 3);
		switch (i) {
		case 0: // Square
			int z = Functions.rnd(1,  3);
			return new Rectangle(main, Statics.STD_CELL_SIZE*z, Statics.STD_CELL_SIZE*z, x);

		case 1:
			return new LShape(main, x, Statics.STD_CELL_SIZE);

		case 2: // Long and thin
			return new Rectangle(main, Statics.STD_CELL_SIZE*Functions.rnd(2, 6), Statics.STD_CELL_SIZE, x);

		case 3: // Rectangle*/
			int w = Functions.rnd(1, 3);
			int h = Functions.rnd(1, 3);
			return new Rectangle(main, Statics.STD_CELL_SIZE*w, Statics.STD_CELL_SIZE*h, x);

		/*default:
			throw new RuntimeException("Unknown shape: " + i);
		}*/
	}


}
