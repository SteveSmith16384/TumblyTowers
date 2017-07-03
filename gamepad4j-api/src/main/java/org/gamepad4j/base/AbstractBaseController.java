/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.base;

import java.util.HashMap;
import java.util.Map;

import org.gamepad4j.AxisID;
import org.gamepad4j.ButtonID;
import org.gamepad4j.DpadDirection;
import org.gamepad4j.IAxis;
import org.gamepad4j.IButton;
import org.gamepad4j.IController;
import org.gamepad4j.IStick;
import org.gamepad4j.ITrigger;
import org.gamepad4j.StickID;
import org.gamepad4j.TriggerID;
import org.gamepad4j.util.Log;

/**
 * Base class for controller wrappers. Handles creation of buttons,
 * sticks, triggers and d-pad based on mapping configuration.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public abstract class AbstractBaseController implements IController {

	/** Stores the deviceID of this controller. */
	protected int deviceID = -1;

	/** Stores the vendorID of this controller. */
	protected int vendorID = -1;

	/** Stores the productID of this controller. */
	protected int productID = -1;

	/** Stores the description of this controller. */
	protected String description = "";

	/** Stores the last d-pad direction. */
	protected DpadDirection lastDirection = null;
	
	// ----------------------- d-pad -----------------------------

	/** Stores a map for the axes used by an analog d-pad */
	protected Map<AxisID, BaseAxis> dpadAxisMap = new HashMap<AxisID, BaseAxis>(); 

	// ----------------------- buttons ---------------------------
	
	/** Lookup map for buttons based on their type. */
	protected Map<ButtonID, IButton> buttonMap = new HashMap<ButtonID, IButton>();

	/** Lookup map for button aliases. */
	protected Map<ButtonID, IButton> buttonAliasMap = new HashMap<ButtonID, IButton>();
	
	/** Stores the buttons of this controller. */
	protected BaseButton[] buttons = null;

	// ----------------------- triggers ---------------------------
	
	/** Lookup map for buttons based on their type. */
	protected Map<TriggerID, ITrigger> triggerMap = new HashMap<TriggerID, ITrigger>();
	
	/** Stores the buttons of this controller. */
	protected BaseTrigger[] triggers = null;

	// ----------------------- sticks ---------------------------
	
	/** Lookup map for sticks based on their type. */
	protected Map<StickID, IStick> stickMap = new HashMap<StickID, IStick>();
	
	/** Stores the sticks of this controller. */
	protected BaseStick[] sticks = null;

	// ----------------------- axes (for triggers AND sticks) ---------------------------
	
	/** Stores the axes of this controller. */
	protected BaseAxis[] axes = null;
	
	/**
	 * Creates a controller wrapper.
	 * 
	 * @param id The deviceID of the controller.
	 */
	protected AbstractBaseController(int deviceID) {
		this(deviceID, "");
	}
	
	/**
	 * Creates a controller wrapper.
	 * 
	 * @param id The deviceID of the controller.
	 */
	protected AbstractBaseController(int deviceID, String description) {
		this.deviceID = deviceID;
		if(this.deviceID < 0) {
			throw new IllegalArgumentException("Device ID must be positive integer value.");
		}
		if(description != null) {
			this.description = description;
		}
	}

	/**
	 * Adds a button to the map of buttons.
	 * 
	 * @param button The button to add.
	 */
	protected void addButton(IButton button) {
		this.buttonMap.put(button.getID(), button);
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getButton(int)
	 */
	@Override
	public IButton getButton(int buttonCode) {
		return this.buttons[buttonCode];
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Sets the description for this controller.
	 * 
	 * @param description The description text.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getID()
	 */
	@Override
	public int getDeviceID() {
		return this.deviceID;
	}

	/**
	 * @param deviceID the deviceID to set
	 */
	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gamepad4j.IController#getVendorID()
	 */
	@Override
	public int getVendorID() {
		return vendorID;
	}

	/**
	 * @param vendorID the vendorID to set
	 */
	public void setVendorID(int vendorID) {
		if(Log.debugEnabled) {
			Log.logger.debug("Set controller vendor ID: " + Integer.toHexString(vendorID));
		}
		this.vendorID = vendorID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gamepad4j.IController#getProductID()
	 */
	@Override
	public int getProductID() {
		return productID;
	}

	/**
	 * @param productID the productID to set
	 */
	public void setProductID(int productID) {
		if(Log.debugEnabled) {
			Log.logger.debug("Set controller product ID: " + Integer.toHexString(productID));
		}
		this.productID = productID;
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getDeviceIdentifier()
	 */
	@Override
	public long getDeviceTypeIdentifier() {
		if(this.vendorID == -1) {
			throw new IllegalArgumentException("Vendor ID not set for controller " + this.deviceID + " / " + this.description);
		}
		if(this.productID == -1) {
			throw new IllegalArgumentException("Product ID not set for controller " + this.deviceID + " / " + this.description);
		}
		return (vendorID << 16) + productID;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getDpadDirectionOnce()
	 */
	@Override
	public DpadDirection getDpadDirectionOnce() {
		DpadDirection current = getDpadDirection();
		if(current != lastDirection) {
			lastDirection = current;
			return current;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#isButtonPressedOnce(org.gamepad4j.util.ButtonID)
	 */
	@Override
	public boolean isButtonPressedOnce(ButtonID buttonID) {
		return getButton(buttonID).isPressedOnce();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getButtons()
	 */
	@Override
	public IButton[] getButtons() {
		return this.buttons;
	}

	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getDpadDirection()
	 */
	@Override
	public DpadDirection getDpadDirection() {
		int value = 0;
		if(this.dpadAxisMap.size() > 0) {
			// It's an analog axes d-pad
			BaseAxis xAxis = this.dpadAxisMap.get(AxisID.D_PAD_X);
			if(xAxis != null) {
				if(xAxis.getValue() == -1) {
					value += DpadDirection.LEFT.getValue();
				} else if(xAxis.getValue() == 1) {
					value += DpadDirection.RIGHT.getValue();
				}
			}
			BaseAxis yAxis = this.dpadAxisMap.get(AxisID.D_PAD_Y);
			if(yAxis != null) {
				if(yAxis.getValue() == -1) {
					value += DpadDirection.UP.getValue();
				} else if(yAxis.getValue() == 1) {
					value += DpadDirection.DOWN.getValue();
				}
			}
		} else {
			// It's a digital button d-pad
			IButton dpadUp = this.buttonMap.get(ButtonID.D_PAD_UP);
			IButton dpadRight = this.buttonMap.get(ButtonID.D_PAD_RIGHT);
			IButton dpadDown = this.buttonMap.get(ButtonID.D_PAD_DOWN);
			IButton dpadLeft = this.buttonMap.get(ButtonID.D_PAD_LEFT);
			if(dpadUp != null && dpadUp.isPressed()) {
				value += DpadDirection.UP.getValue();
			}
			if(dpadDown != null && dpadDown.isPressed()) {
				value += DpadDirection.DOWN.getValue();
			}
			if(dpadLeft != null && dpadLeft.isPressed()) {
				value += DpadDirection.LEFT.getValue();
			}
			if(dpadRight != null && dpadRight.isPressed()) {
				value += DpadDirection.RIGHT.getValue();
			}
		}
		return DpadDirection.fromIntValue(value);
	}
	
	

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getTriggers()
	 */
	@Override
	public ITrigger[] getTriggers() {
		return this.triggers;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getTrigger(org.gamepad4j.TriggerID)
	 */
	@Override
	public ITrigger getTrigger(TriggerID triggerID) {
		return this.triggerMap.get(triggerID);
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getButton(org.gamepad4j.util.ButtonID)
	 */
	@Override
	public IButton getButton(ButtonID buttonID) {
		IButton button = this.buttonMap.get(buttonID);
		if(button == null) {
			button = this.buttonAliasMap.get(buttonID);
		}
		return button;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#isButtonPressed(org.gamepad4j.util.ButtonID)
	 */
	@Override
	public boolean isButtonPressed(ButtonID buttonID) {
		return this.buttonMap.get(buttonID).isPressed();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IController#getAnalogButtonPressure(org.gamepad4j.util.ButtonID)
	 */
	@Override
	public float getTriggerPressure(TriggerID buttonID)
			throws IllegalArgumentException {
		return this.triggerMap.get(buttonID).analogValue();
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getSticks()
	 */
	@Override
	public IStick[] getSticks() {
		return this.sticks;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IController#getStick(org.gamepad4j.StickID)
	 */
	@Override
	public IStick getStick(StickID stick) throws IllegalArgumentException {
		return stickMap.get(stick);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gamepad4j.IController#getAxes()
	 */
	@Override
	public IAxis[] getAxes() {
		if(this.axes == null) {
			this.axes = new BaseAxis[0];
		}
		return this.axes;
	}
}
