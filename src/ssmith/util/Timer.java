package ssmith.util;

/**
 * We can't just use currentTimeMillis as when the game is paused, it will still continue.
 *
 */
public class Timer {
	
	private long interval;
	private long remaining;
	
	public Timer(long _interval) {
		super();
		
		remaining = _interval;
		interval = _interval;
	}
	
	
	public boolean hasHit(long amt) {
		remaining -= amt;
		boolean result = false;
		if (remaining <= 0) {
			remaining = interval;
			result = true;
		}
		return result;
	}
	
	
	public long getTimeRemaining() {
		return remaining;
	}

}
