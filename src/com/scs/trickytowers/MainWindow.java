package com.scs.trickytowers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public BufferStrategy BS;

	public MainWindow(Main_TumblyTowers main) {
		if (Statics.FULL_SCREEN) {
			this.setUndecorated(true);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
			device.setFullScreenWindow(this);

		} else {
			float frac = (float)Statics.WORLD_WIDTH_LOGICAL / (float)Statics.WORLD_HEIGHT_LOGICAL;
			this.setSize((int)(Statics.WINDOW_HEIGHT * frac), Statics.WINDOW_HEIGHT);
		}
		Statics.LOGICAL_TO_PIXELS = this.getHeight() / Statics.WORLD_HEIGHT_LOGICAL;
		//Statics.WORLD_WIDTH_LOGICAL = (int)(this.getWidth() / Statics.LOGICAL_TO_PIXELS);
		//Statics.WORLD_HEIGHT_LOGICAL = (int)(this.getHeight() / Statics.LOGICAL_TO_PIXELS);


		this.setVisible(true);
		this.addKeyListener(main);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
		
		this.requestFocus();
	}





}
