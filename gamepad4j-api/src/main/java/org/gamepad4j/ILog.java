/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j;

import org.gamepad4j.util.Log;

/**
 * Interface for logger implementations. This way, any application using
 * this API can use a custom implementation in order to let this API 
 * log everything with the desired log API (Log4J, Commons Logging, Logback...).
 * <p>
 * Set the system property {@link Log#GAMEPAD4J_LOGGER_CLASS} with the fully
 * qualified name of the class which implements this interface in order to use
 * a custom logger implementation.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public interface ILog {

	/**
	 * Writes a debug log message to the log.
	 * 
	 * @param message The debug log message text.
	 */
	void debug(String message);

	/**
	 * Writes an info log message to the log.
	 * 
	 * @param message The info log message text.
	 */
	void info(String message);
	
	/**
	 * Writes an error log message to the log.
	 * 
	 * @param message The error log message text.
	 */
	void error(String message);
}
