/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j.base;

import org.gamepad4j.AxisID;
import org.gamepad4j.IAxis;
import org.gamepad4j.IStick;
import org.gamepad4j.StickID;
import org.gamepad4j.StickPosition;

/**
 * Abstract base class for stick wrappers.
 * 
 * TODO: Pool instances of this class
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class BaseStick implements IStick {

	/** Stores the deviceID of this stick. */
	protected StickID ID = StickID.UNKNOWN;

	/** The stick position data holder. */
	protected StickPosition position = new StickPosition();
	
	/** Holds direct reference to the X-axis. */
	protected IAxis xAxis = null;
	
	/** Holds direct reference to the Y-axis. */
	protected IAxis yAxis = null;
	
	/** Stores the two axes in an array. */
	private IAxis[] axes = new IAxis[2];
	
	/**
	 * Creates a stick wrapper.
	 * 
	 * @param deviceID The deviceID of this stick.
	 */
	public BaseStick(StickID ID) {
		this.ID = ID;
		this.axes[0] = this.xAxis;
		this.axes[1] = this.yAxis;
	}

	/**
	 * Sets the ID and number of an axis for this stick.
	 * 
	 * @param ID The ID of the axis.
	 * @param axisNumber The number of the axis.
	 */
	public void setAxis(IAxis axis) {
		if(axis.getID() == AxisID.X) {
			this.xAxis = axis;
			this.axes[0] = this.xAxis;
		} else {
			this.yAxis = axis;
			this.axes[1] = this.yAxis;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IStick#getID()
	 */
	@Override
	public StickID getID() {
		return this.ID;
	}

	/**
	 * Convenience method which provides pre-processed status
	 * information about the stick, such as the degree in which
	 * it is currently held, and the distance to the center.
	 * 
	 * @return The stick position data holder.
	 */
	public StickPosition getPosition() {
		this.position.update(this.xAxis.getValue(), this.yAxis.getValue());
		return this.position;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IStick#getAxes()
	 */
	@Override
	public IAxis[] getAxes() {
		return this.axes;
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.IStick#getAxis(org.gamepad4j.util.AxisID)
	 */
	@Override
	public IAxis getAxis(AxisID ID) {
		if(ID == AxisID.X) {
			return this.xAxis;
		}
		return this.yAxis;
	}

}
