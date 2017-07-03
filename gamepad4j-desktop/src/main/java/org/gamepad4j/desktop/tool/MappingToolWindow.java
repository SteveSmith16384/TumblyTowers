/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.desktop.tool;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.gamepad4j.ButtonID;
import org.gamepad4j.Controllers;
import org.gamepad4j.IButton;
import org.gamepad4j.IController;
import org.gamepad4j.IControllerListener;
import org.gamepad4j.StickID;
import org.gamepad4j.util.Log;

/**
 * Shows the mapping tool window.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class MappingToolWindow extends JFrame implements IControllerListener {

	/** Stores ImageIcon instances for various pads. */
	public static Map<Long, ImageIcon> padImageMap = new HashMap<Long, ImageIcon>();

	private static int numberOfPads = 0;
	
	public MappingToolWindow() {
		
		// Initial gamepad detection
		Log.initialize(Log.LogLevel.DEBUG);
		Controllers.initialize();
		
		setTitle("Gamepad4J Test Program");
	    setSize(400,500);  
	    addWindowListener(new TestWindowListener());
	    getContentPane().setLayout(new FlowLayout());
		try {
			InputStream in = MappingToolWindow.class.getResourceAsStream("/image-filenames.properties");
			Properties filenameProps = new Properties();
			filenameProps.load(in);
			
			// Store controller-specific images
			Enumeration keys = filenameProps.keys();
			while(keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String imageFilename = filenameProps.getProperty(key);
				String id = key.substring(key.indexOf("-") + 3, key.indexOf("_"));
				int vendorID = Integer.parseInt(id, 16);
				id = key.substring(key.indexOf("_") + 3);
				int productID = Integer.parseInt(id, 16);
				long deviceTypeIdentifier = (vendorID << 16) + productID;
				Log.logger.debug(">> load image: " + imageFilename);
			    InputStream input = MappingToolWindow.class.getResourceAsStream(imageFilename);
			    ImageInputStream imageIn = ImageIO.createImageInputStream(input);
			    BufferedImage image = ImageIO.read(imageIn);
			    ImageIcon padImage = new ImageIcon(image);
			    padImageMap.put(deviceTypeIdentifier, padImage);
			}

			// Store default image
		    InputStream input = MappingToolWindow.class.getResourceAsStream("/controller_xbox_360.png");
		    ImageInputStream imageIn = ImageIO.createImageInputStream(input);
		    BufferedImage image = ImageIO.read(imageIn);
		    ImageIcon padImage = new ImageIcon(image);
		    padImageMap.put(0L, padImage);
		    
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		Controllers.instance().addListener(this);
		
		// Build window content
//		updateWindow();
	}
	
	private void updateWindow() {
		System.out.println("--- controller change, update window ---");
		getContentPane().removeAll();
//		Controllers.checkControllers();
		IController[] controllers = Controllers.getControllers();
		numberOfPads = controllers.length;
		for(int i = 0; i < numberOfPads; i++) {
			getContentPane().add(new ControllerPanel(controllers[i]));
		}
		pack();
	}
	
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IControllerListener#connected(org.gamepad4j.IController)
	 */
	@Override
	public void connected(IController controller) {
		System.out.println(">> Gamepad connected.");
		updateWindow();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IControllerListener#disConnected(org.gamepad4j.IController)
	 */
	@Override
	public void disConnected(IController controller) {
		System.out.println(">> Gamepad disconnected.");
		updateWindow();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IControllerListener#buttonDown(org.gamepad4j.IController, org.gamepad4j.IButton, org.gamepad4j.ButtonID)
	 */
	@Override
	public void buttonDown(IController controller, IButton button,
			ButtonID buttonID) {
		System.out.println(">> button down");
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IControllerListener#buttonUp(org.gamepad4j.IController, org.gamepad4j.IButton, org.gamepad4j.ButtonID)
	 */
	@Override
	public void buttonUp(IController controller, IButton button,
			ButtonID buttonID) {
		System.out.println(">> button up");
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IControllerListener#moveStick(org.gamepad4j.IController, org.gamepad4j.StickID)
	 */
	@Override
	public void moveStick(IController controller, StickID stick) {
		System.out.println(">> move stick");
	}


	class TestWindowListener extends WindowAdapter {
	    public void windowClosing(WindowEvent e) {
			GamepadCheck.running = false;
			Controllers.shutdown();
			e.getWindow().dispose();
	    }           
	}
}
