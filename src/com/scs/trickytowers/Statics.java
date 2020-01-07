package com.scs.trickytowers;

import java.util.Random;

import ssmith.awt.ImageCache;

public class Statics {

	public static final boolean FULL_SCREEN = false;
	public static final boolean RELEASE_MODE = false;
	
	public static final boolean SHOW_CONTROLLER_EVENTS = !RELEASE_MODE && true;

	public static final int FPS = 30;
	public static final float STD_CELL_SIZE = 8f;
	
	public static final int WINDOW_WIDTH = 800; // Only used when windowed
	public static final int WINDOW_HEIGHT = 600; // Only used when windowed

	public static final int WORLD_WIDTH_LOGICAL = 400;// = (int)(WINDOW_WIDTH / LOGICAL_TO_PIXELS);
	public static final int WORLD_HEIGHT_LOGICAL = 300;// = (int)(WINDOW_HEIGHT / LOGICAL_TO_PIXELS);

	public static float LOGICAL_TO_PIXELS;// = 3f;
	public static final float LOGICAL_WINNING_HEIGHT = WORLD_HEIGHT_LOGICAL * 0.2f;
	
	public static final Random rnd = new Random();

	public static ImageCache img_cache;

	private Statics() {

	}


	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ":" + s);
	}


}
