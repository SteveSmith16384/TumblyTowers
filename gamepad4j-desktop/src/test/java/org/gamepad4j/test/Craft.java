/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.test;

import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

/**
 * Test program for demonstrating how to move a sprite around
 * using the gamepad.
 * 
 * The code of this class was taken from this Java game programming tutorial:
 * 
 * http://zetcode.com/tutorials/javagamestutorial/movingsprites/
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class Craft {

    private String craft = "/craft.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private Image image;

    public Craft() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(craft));
        image = ii.getImage();
        x = 40;
        y = 60;
    }


    public void move() {
        x += dx;
        y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }
/*
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            goLeft();
        }

        if (key == KeyEvent.VK_RIGHT) {
            goRight();
        }

        if (key == KeyEvent.VK_UP) {
            goUp();
        }

        if (key == KeyEvent.VK_DOWN) {
            goDown();
        }
    }
*/
	/**
	 * 
	 */
	public void goDown() {
		dy = 1;
	}

	/**
	 * 
	 */
	public void goUp() {
		dy = -1;
	}

	/**
	 * 
	 */
	public void goRight() {
		dx = 1;
	}

	/**
	 * 
	 */
	public void goLeft() {
		dx = -1;
	}
/*
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            stopMoving();
        }

        if (key == KeyEvent.VK_RIGHT) {
            stopMoving();
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
*/

	/**
	 * 
	 */
	public void stopMoving() {
		dx = 0;
        dy = 0;
	}
}