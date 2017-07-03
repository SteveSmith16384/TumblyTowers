/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.desktop.tool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.gamepad4j.IAxis;
import org.gamepad4j.IAxisListener;

/**
 * Little panel which draws a green or red bar.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class ColorBoxPanel extends JPanel implements IAxisListener {
	
	public int percent = 23;
	public boolean positive = true;
	
	private IAxis axis = null;
	
	public static Dimension SIZE = new Dimension(50,10);

	private DecimalFormat df = new DecimalFormat("#.####");
	
	public ColorBoxPanel(IAxis axis) {
		this.axis = axis;
		this.axis.addAxisListener(this);
		setPreferredSize(SIZE);
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IAxisListener#moved(float)
	 */
	@Override
	public void moved(float value) {
		super.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        float value = this.axis.getValue();
        if(value < 0) {
        	this.positive = false;
        	this.percent = (int)((value * -1f) * 100f);
        } else {
        	this.positive = true;
        	this.percent = (int)(value * 100f);
        }
        
        int width = getSize().width;
        int drawWidth = (width * this.percent) / 100;

        g.setColor(Color.BLACK);  
        g.fillRect(0, 0, getSize().width, getSize().height); 

        if(this.positive) {
            g.setColor(Color.GREEN);  
        } else {
            g.setColor(Color.RED);  
        }
        g.fillRect(0, 0, drawWidth, getSize().height); 
    }
}
