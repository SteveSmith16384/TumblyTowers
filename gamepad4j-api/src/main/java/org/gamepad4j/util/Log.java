/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.util;

import org.gamepad4j.ILog;

/**
 * Logging utility class.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class Log implements ILog {

	/**
	 * Log level values.
	 */
	public enum LogLevel {
		DEBUG,
		INFO,
		ERROR,
		NONE
	}
	
	/** Constant for name of system property for logger implementation class. */
	public static final String GAMEPAD4J_LOGGER_CLASS = "gamepad4j.logger.class";
	
	/** Singleton logger instance; public access for max performance. */
	public static ILog logger = null;
	
	/** Singleton log level; public access for max performance. */
	public static LogLevel level = null;
	
	/** Debug logging flag. */
	public static boolean debugEnabled = true;
	
	/** Info logging flag. */
	public static boolean infoEnabled = true;
	
	/** Error logging flag. */
	public static boolean errorEnabled = true;
	
	/* (non-Javadoc)
	 * @see org.gamepad4j.util.ILog#debug(java.lang.String)
	 */
	@Override
	public void debug(String message) {
		System.out.println("[Gamepad4J] [DEBUG] " + message);
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.ILog#info(java.lang.String)
	 */
	@Override
	public void info(String message) {
		System.out.println("[Gamepad4J] [INFO] " + message);
	}

	/* (non-Javadoc)
	 * @see org.gamepad4j.util.ILog#error(java.lang.String)
	 */
	@Override
	public void error(String message) {
		System.err.println("[Gamepad4J] [ERROR] " + message);
	}

	/**
	 * Prepares the singleton logger implementation instance. Creates 
	 * a new one, if none exists yet.
	 * 
	 * @param logLevel The log level to be used by the API.
	 */
	public static void initialize(LogLevel logLevel) {
		if(Log.level == null) {
			Log.level = logLevel;
		}
		if(Log.level == LogLevel.DEBUG) {
			Log.debugEnabled = true;
			Log.infoEnabled = true;
			Log.errorEnabled = true;
		} else if(Log.level == LogLevel.INFO) {
			Log.infoEnabled = true;
			Log.errorEnabled = true;
		} else if(Log.level == LogLevel.ERROR) {
			Log.errorEnabled = true;
		} 
		if(logger == null) {
			String implClass = System.getProperty(GAMEPAD4J_LOGGER_CLASS, Log.class.getName());
			try {
				logger = (ILog)Class.forName(implClass).newInstance();
			} catch(Exception ex) {
				ex.printStackTrace();
				logger = new Log();
			}
		}
	}
}
