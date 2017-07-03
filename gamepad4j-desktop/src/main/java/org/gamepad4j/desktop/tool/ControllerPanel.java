/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.desktop.tool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gamepad4j.IAxis;
import org.gamepad4j.IController;

/**
 * Panel which shows the status of one gamepad.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class ControllerPanel extends JPanel {

	public ControllerPanel(IController controller) {
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//	    setBorder(BorderFactory.createLineBorder(Color.GREEN));
//	    setBorder(BorderFactory.createLoweredBevelBorder());
//	    setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
	    
	    ImageIcon padImage = MappingToolWindow.padImageMap.get(controller.getDeviceTypeIdentifier());
	    if(padImage == null) {
		    padImage = MappingToolWindow.padImageMap.get(0L);
	    }
	    int mainPanelWidth = padImage.getIconWidth();
	    int mainPanelHeight = padImage.getIconHeight();
	    JPanel imagePanel = new JPanel();
	    JLabel label = new JLabel(padImage, JLabel.CENTER);
	    imagePanel.add(label);
	    imagePanel.setPreferredSize(new Dimension(mainPanelWidth, mainPanelHeight));
	    add(imagePanel);

	    JLabel axisLabel = new JLabel("Axes");
	    add(axisLabel);

	    IAxis[] axes = controller.getAxes();
	    int numberOfHorizontalBoxes = mainPanelWidth / ColorBoxPanel.SIZE.width;
	    int numberOfVerticalBoxes = axes.length / numberOfHorizontalBoxes + 1;
	    
	    JPanel subPanel = new JPanel(new GridBagLayout());
	    subPanel.setBackground(Color.DARK_GRAY);
	    GridBagConstraints grid = new GridBagConstraints();
	    grid.weightx = 0.5f;
	    grid.fill = GridBagConstraints.HORIZONTAL;
	    add(subPanel);
	    
	    int x = 0, y = 0;
	    for(IAxis axis : axes) {
	    	grid.gridx = x;
	    	grid.gridy = y;
		    subPanel.add(new AxisPanel(axis), grid);
		    x++;
		    if(x == numberOfHorizontalBoxes) {
		    	x = 0;
		    	y++;
		    }
	    }
	}

}
