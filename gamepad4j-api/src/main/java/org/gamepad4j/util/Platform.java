/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.util;

/**
 * Enum for platform types.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public enum Platform {

	ouya(""),
	macos("libgamepad4j.jnilib"),
	windows("libgamepad4j.dll"),
	linux("libgamepad4j.so"),
	unknown("");

	/**
	 * Creates a platform type holder.
	 * 
	 * @param libraryName The name of the native shared library file.
	 */
	Platform(String libraryName) {
		this.libraryName = libraryName;
	}
	
	/**
	 * Returns the name of the shared library file.
	 * 
	 * @return The library filename.
	 */
	public String getLibraryName() {
		return libraryName;
	}
	
	/** Stores the name of the native library file. */
	private String libraryName = null;

}
