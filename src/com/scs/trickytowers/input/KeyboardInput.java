package com.scs.trickytowers.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class KeyboardInput implements IInputDevice, KeyListener {

	public static final int KEYBOARD1_ID = -1;
	public static final int KEYBOARD2_ID = -2;
	
	private volatile boolean left, right, up, down, fire;
	//private boolean lastMoveWasLeft = false;
	private int id;

	public KeyboardInput(JFrame frame, int _id) {
		super();

		id = _id;

		if (id >= 0) {
			throw new RuntimeException("Invalid keyboard ID - must be < 0");
		}
		
		frame.addKeyListener(this);
	}


	@Override
	public boolean isLeftPressed() {
		/*if (left) {
			lastMoveWasLeft = true;
		}*/
		return left;
	}


	@Override
	public boolean isRightPressed() {
		/*if (right) {
			lastMoveWasLeft = false;
		}*/
		return right;
	}


	@Override
	public boolean isSpinLeftPressed() {
		return up;
	}


	@Override
	public boolean isSpinRightPressed() {
		return down;
	}

/*
	@Override
	public int getAngle() {
		return lastMoveWasLeft ? 210: 330;
	}
*/

	@Override
	public boolean isFirePressed() {
		return fire;
	}


	@Override
	public void keyPressed(KeyEvent ke) {
		if (this.id == KEYBOARD1_ID) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				left = true;
				break;

			case KeyEvent.VK_RIGHT:
				right = true;
				break;

			case KeyEvent.VK_UP:
				up = true;
				break;

			case KeyEvent.VK_DOWN:
				down = true;
				break;

			case KeyEvent.VK_CONTROL:
				fire = true;
				//firePressedTime = System.currentTimeMillis();
				break;
			}
		} else if (id == KEYBOARD2_ID) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_A:
				left = true;
				break;

			case KeyEvent.VK_D:
				right = true;
				break;

			case KeyEvent.VK_W:
				up = true;
				break;

			case KeyEvent.VK_S:
				down = true;
				break;

			case KeyEvent.VK_SPACE:
				fire = true;
				//firePressedTime = System.currentTimeMillis();
				break;
			}

		} else {
			throw new RuntimeException("Invalid keyboard ID: " + id);
		}

	}


	@Override
	public void keyReleased(KeyEvent ke) {
		if (this.id == KEYBOARD1_ID) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				left = false;
				break;

			case KeyEvent.VK_RIGHT:
				right = false;
				break;

			case KeyEvent.VK_UP:
				up = false;
				break;

			case KeyEvent.VK_DOWN:
				down = false;
				break;

			case KeyEvent.VK_CONTROL:
				fire = false;
				//this.duration = System.currentTimeMillis() - this.firePressedTime;
				break;

			}

		} else if (id == KEYBOARD2_ID) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_A:
				left = false;
				break;

			case KeyEvent.VK_D:
				right = false;
				break;

			case KeyEvent.VK_W:
				up = false;
				break;

			case KeyEvent.VK_S:
				down = false;
				break;

			case KeyEvent.VK_SPACE:
				fire = false;
				//firePressedTime = System.currentTimeMillis();
				break;
			}

		}
	}


	@Override
	public void keyTyped(KeyEvent ke) {
		// Do nothing
	}

/*
	@Override
	public float getStickDistance() {
		return 1;
	}
*/

	@Override
	public int getID() {
		return id;
	}


	@Override
	public String toString() {
		return "KeyboardInput:" + id;
	}


	@Override
	public void clearInputs() {
		left = false;
		right = false;
		up = false;
		down = false;
		
	}


	@Override
	public void readEvents() {
		// Not required
	}

}
