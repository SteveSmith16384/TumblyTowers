package com.scs.trickytowers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

	public BufferStrategy BS;

	public MainWindow(Main_TumblyTowers main) {
		if (Statics.FULL_SCREEN) {
			this.setUndecorated(true);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
			device.setFullScreenWindow(this);

		} else {
			this.setSize(Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		}
		Statics.WORLD_WIDTH_LOGICAL = (int)(this.getWidth() / Statics.LOGICAL_TO_PIXELS);
		Statics.WORLD_HEIGHT_LOGICAL = (int)(this.getHeight() / Statics.LOGICAL_TO_PIXELS);


		this.setVisible(true);
		this.addKeyListener(main);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}





}
