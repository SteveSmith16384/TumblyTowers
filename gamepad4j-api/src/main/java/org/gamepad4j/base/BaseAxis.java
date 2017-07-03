/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.base;

import java.util.ArrayList;
import java.util.List;

import org.gamepad4j.AxisID;
import org.gamepad4j.IAxis;
import org.gamepad4j.IAxisListener;

/**
 * Holder for values of one axis.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class BaseAxis implements IAxis {

	/** Stores the ID of this axis. */
	private AxisID ID = null;
	
	/** Stores the float value of this axis. */
	private float value = 0f;
	
	/** Stores the number of the axis. */
	private int number = -1;
	
	/** Stores the previous float value of this axis. */
	private float previousValue = 0f;

	/** Default deadzone range. */
	private float deadZone = 0.1f;
	private float deadZoneNegative = -this.deadZone;

	/** List of axis event listeners. */
	private List<IAxisListener> listeners = null;
	
	/**
	 * Creates a new base axis instance.
	 * 
	 * @param ID The ID of the axis (important only for multi-axes components like sticks).
	 * @param The number of the axis.
	 */
	public BaseAxis(AxisID ID, int number) {
		this.ID = ID;
		this.number = number;
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IAxis#getNumber()
	 */
	@Override
	public int getNumber() {
		return this.number;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IAxis#addAxisListener(org.gamepad4j.IAxisListener)
	 */
	@Override
	public void addAxisListener(IAxisListener listener) {
		if(this.listeners == null) {
			this.listeners = new ArrayList<IAxisListener>();
		}
		this.listeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.IAxis#getID()
	 */
	@Override
	public AxisID getID() {
		return this.ID;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.IAxis#getValue()
	 */
	@Override
	public float getValue() {
		// Check if value is inside deadzone
		if(this.value > this.deadZoneNegative && this.value < this.deadZone) {
			// If yes, signal nothing.
			return 0f;
		}
		return this.value;
	}

	/**
	 * Sets the deadzone range for this axis.
	 * 
	 * @param deadZone The deadzone range value.
	 */
	public void setDeadZone(float deadZone) {
		this.deadZone = deadZone;
		this.deadZoneNegative = -this.deadZone;
	}
	
	/**
	 * Sets the float value of this axis.
	 * 
	 * @param value The new float value.
	 */
	public void setValue(float value) {
		if(value < -1.0f || value > 1.0f) {
			throw new RuntimeException("ILLEGAL AXIS VALUE: " + value);
		}
		this.previousValue = this.value;
		this.value = value;
		if(this.value != this.previousValue && this.listeners != null) {
			for(IAxisListener listener : this.listeners) {
				listener.moved(this.value);
			}
		}
	}
}
