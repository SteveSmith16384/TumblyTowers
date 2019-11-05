/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.desktop.tool;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gamepad4j.IAxis;

/**
 * Small panel which displays the number and state of an axis.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class AxisPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/** Red/green color bar panel. */
	private ColorBoxPanel colorPanel = null;

	/**
	 * Creates a panel for one axis.
	 * 
	 * @param axis The axis for which this panel is created.
	 */
	public AxisPanel(IAxis axis) {
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    setBorder(BorderFactory.createLoweredBevelBorder());

	    // Upper panel with number text label
	    JPanel noPanel = new JPanel();
	    noPanel.setPreferredSize(new Dimension(50, 20));
	    JLabel number = new JLabel(String.valueOf(axis.getNumber()));
	    noPanel.add(number);
	    add(noPanel);

	    // Lower panel with green/red color bar box.
	    colorPanel = new ColorBoxPanel(axis);
	    add(colorPanel);
	}
}
