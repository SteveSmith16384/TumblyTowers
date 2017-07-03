/*
 * @Copyright: Marcel Schoen, Switzerland, 2013, All Rights Reserved.
 */

package org.gamepad4j;

/**
 * Holder for pre-processed analog stick position data. This class
 * provides readily available information about the positioning
 * (degree and distance to the center) of an analog stick, so that
 * you don't have to calculate this by yourself based on the X- 
 * and Y-axis values.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class StickPosition {

	/** Stores the current X-axis value. */
	public float xAxis = -1;
	
	/** Stores the current Y-axis value. */
	public float yAxis = -1;
	
	/** Constant for marker value indicating no degree has been measured. */
	public static final float NO_DEGREE = -1f;

	/** Minimum distance from center for stick to not be centered. */
	private static final float minimumCenterDistance = 0.2f;

	/**
	 * Should be invoked by the stick implementation before
	 * returning this instance.
	 * 
	 * @param xAxis The current X-axis value of this stick.
	 * @param yAxis The current Y-axis value of this stick.
	 */
	public void update(float xAxis, float yAxis) {
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}
	
	/**
	 * Convenience method which returns the corresponding direction
	 * of a d-pad. With this method, the analog stick can easily be
	 * used instead of the d-pad, without having to perform any
	 * x- and y-axis calculations manually.
	 * 
	 * @return The direction of the stick, as if it were a d-pad.
	 */
	public DpadDirection getDirection() {
		float degree = getDegree();
		/*
		 * degree ranges from 0 (top) to 90 (right) to 180 (down) to 270 (left)
		 */
		if(degree == NO_DEGREE) {
			return DpadDirection.NONE;
		} else if(degree < 207.5f && degree > 152.5f) {
			return DpadDirection.DOWN;
		} else if(degree < 252.5f && degree > 207.5f) {
			return DpadDirection.DOWN_LEFT;
		} else if(degree < 297.5f && degree > 252.5f) {
			return DpadDirection.LEFT;
		} else if(degree < 342.5f && degree > 297.5f) {
			return DpadDirection.UP_LEFT;
		} else if(degree > 342.4f || degree < -342.4f) {
			return DpadDirection.UP;
		} else if(degree < 152.5f && degree > 107.5f) {
			return DpadDirection.DOWN_RIGHT;
		} else if(degree < 107.5f && degree > 62.5f) {
			return DpadDirection.RIGHT;
		} else if(degree < 62.5f && degree > 17.5f) {
			return DpadDirection.UP_RIGHT;
		}
		return DpadDirection.NONE;
	}
	
	/**
	 * Convenience method which returns the degree at which 
	 * the stick is currently being held. It starts at the top (0 degree),
	 * and increases clockwise to the right (90 degree), down (180 degree)
	 * and left (270 degree).
	 * 
	 * @return The degree in which the stick is being held, or NO_DEGREE if 
	 *         no value was measured.
	 */
	public float getDegree() {
		if(!isStickCentered()) {
	        float degree = (float) Math.toDegrees( Math.atan2(-this.xAxis, this.yAxis) );
	        return degree + 180;
		}
		return NO_DEGREE;
	}
	
	/**
	 * Convenience method which returns the distance at which
	 * the stick is currently being held from the center. This 
	 * can be used as a measure of power / force, for example 
	 * (e.g. the farther from the center, the faster the game
	 * character moves etc.).
	 * 
	 * @return The disctance from the center.
	 */
	public float getDistanceToCenter() {
		return (float) Math.sqrt(this.xAxis * this.xAxis + this.yAxis * this.yAxis);
	}

	/**
	 * Convenience method which allows to check if the stick
	 * is currently centered.
	 * 
	 * @return True if the stick is centered.
	 */
    public boolean isStickCentered() {
        float stickMag = getDistanceToCenter();
        return (stickMag < minimumCenterDistance);
    }
}
