/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.base;

import org.gamepad4j.ButtonID;
import org.gamepad4j.IButton;
import org.gamepad4j.IController;
import org.gamepad4j.util.Log;

/**
 * Abstract base class for button wrappers.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class BaseButton implements IButton {
	
	/** Button pressing flag. */
	private boolean isPressed = false;

	/** Stores the deviceID of this button. */
	protected ButtonID ID = ButtonID.UNKNOWN;
	
	/** Stores the label of this button. */
	protected String label = null;

	/** Stores the label resource bundle key of this button. */
	protected String labelKey = null;

	/** Stores the last pressed state. */
	protected boolean lastPressed = false;

	/** The numeric code of the controller button. */
	protected int code = -1;
	
	/** Stores the controller to which this button belongs. */
	protected IController controller = null;
	
	/**
	 * Creates a button wrapper.
	 * 
	 * @param code The numeric code of the button.
	 * @param isAnalog Set to true if this is an analog button.
	 * @param label The text label (may be null).
	 * @param labelKey The text label key (may be null).
	 */
	public BaseButton(IController controller, int code, String label, String labelKey) {
		this.controller = controller;
		this.code = code;
		this.label = label;
		this.labelKey = labelKey;
	}

	
	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IButton#getID()
	 */
	@Override
	public ButtonID getID() {
		return this.ID;
	}

	/**
	 * Sets the ID of this button as defined by the mapping.
	 * 
	 * @param ID The ID for this button.
	 */
	public void setID(ButtonID ID) {
		this.ID = ID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gamepad4j.IButton#getIndex()
	 */
	@Override
	public int getCode() {
		return this.code;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.base.BaseButton#isPressed()
	 */
	@Override
	public boolean isPressed() {
		return isPressed;
	}

	/**
	 * Sets the pressed state of this button.
	 * 
	 * @param isPressed True if the button is pressed.
	 */
	public void setPressed(boolean isPressed) {
		if(isPressed != this.isPressed) {
			if(Log.debugEnabled) {
				Log.logger.debug("Button press change: " + isPressed + "/ code: " + this.code + " / ID: "
						+ this.ID + " / label: " + this.label + " / key: " + this.labelKey);
			}
		}
		this.isPressed = isPressed;
		// TODO: Implement listener for buttons
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IButton#isPressedOnce()
	 */
	@Override
	public boolean isPressedOnce() {
		boolean pressed = isPressed();
		if(pressed != lastPressed) {
			lastPressed = pressed;
			return pressed;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IButton#getLabelKey()
	 */
	@Override
	public String getLabelKey() {
		return this.labelKey;
	}

	/**
	 * @param label the label to set
	 */
	public void setDefaultLabel(String label) {
		this.label = label;
	}


	/**
	 * @param labelKey the labelKey to set
	 */
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}


	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IButton#getDefaultLabel()
	 */
	@Override
	public String getDefaultLabel() {
		return this.label;
	}

}
