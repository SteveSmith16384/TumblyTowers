/*
 * @Copyright: Marcel Schoen, Switzerland, 2014, All Rights Reserved.
 */

package org.gamepad4j.desktop;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.gamepad4j.ButtonID;
import org.gamepad4j.IController;
import org.gamepad4j.StickID;
import org.gamepad4j.TriggerID;
import org.gamepad4j.util.Log;
import org.gamepad4j.util.PlatformUtil;

/**
 * Handles the mapping of digital buttons and analog axes to
 * triggers, d-pad, analog sticks and buttons.
 *
 * @author Marcel Schoen
 * @version $Revision: $
 */
public class Mapping {

	public static enum MappingType {
		BUTTON,
		TRIGGER_AXIS,
		DPAD_AXIS,
		STICK_AXIS
	}

	/** Stores digital button mappings. */
	private static Map<Long, Map<Integer, String>> buttonMapId = new HashMap<Long, Map<Integer, String>>();
	
	/** Stores trigger mappings. */
	private static Map<Long, Map<Integer, String>> triggerAxisMapId = new HashMap<Long, Map<Integer, String>>();
	
	/** Stores d-pad mappings. */
	private static Map<Long, Map<Integer, String>> dpadAxisMapId = new HashMap<Long, Map<Integer, String>>();
	
	/** Stores stick mappings. */
	private static Map<Long, Map<Integer, String>> stickAxisMapId = new HashMap<Long, Map<Integer, String>>();

	/** Stores the default button text labels. */
	private static Properties defaultLabels = new Properties();

	/** Stores the default label for each button of each device type. */
	private static Map<Long, Map<ButtonID, String>> defaultButtonLabelMap = new HashMap<Long, Map<ButtonID, String>>();

	/** Stores the default label for each trigger of each device type. */
	private static Map<Long, Map<TriggerID, String>> defaultTriggerLabelMap = new HashMap<Long, Map<TriggerID, String>>();
	
	/** Stores the label key for each button of each device type. */
	private static Map<Long, Map<ButtonID, String>> buttonLabelKeyMap = new HashMap<Long, Map<ButtonID, String>>();
	
	/** Stores the label key for each trigger of each device type. */
	private static Map<Long, Map<TriggerID, String>> triggerLabelKeyMap = new HashMap<Long, Map<TriggerID, String>>();

	/** Lazy label initialization flag. */
	private static boolean labelsInitialized = false;

	private static String getMappingFileName (String vendorHex, String productHex) {
		return "/mappings/" + PlatformUtil.getPlatform().name() + "/"
				+ "0x" + vendorHex + "-0x" + productHex + "-gamepad4j-mapping.properties";
	}
	
	/**
	 * Loads the mapping for the given controller (if not available yet).
	 */
	public static void loadMapping(DesktopController controller) {
		try {
			
			if(!labelsInitialized) {
				// Read the default text labels
				InputStream in = Mapping.class.getResourceAsStream("/mappings/default-labels.properties");
				defaultLabels.load(in);
				in.close();
			}
			
			// Check if mappings already exist for that controller
			if(defaultButtonLabelMap.get(controller.getDeviceTypeIdentifier()) == null) {
				// If not, load them now
				String vendorHex = Integer.toHexString(controller.getVendorID()).toUpperCase();
				String productHex = Integer.toHexString(controller.getProductID()).toUpperCase();
				String mappingFileName = getMappingFileName(vendorHex, productHex);
				if(Log.debugEnabled) {
					Log.logger.debug("Vendor ID: " + controller.getVendorID() + " / Product ID: " + controller.getProductID());
					Log.logger.debug("Load mapping from resource: " + mappingFileName);
				}

				InputStream propIn = null;
				try {
					propIn = Mapping.class.getResourceAsStream(mappingFileName);
					if(propIn != null) {
						Properties mappingProps = new Properties();
						mappingProps.load(propIn);
						addMappings(mappingProps);
						
						if(mappingProps.getProperty("deadzone") != null) {
							float deadZone = floatFromString(mappingProps.getProperty("deadzone"));
							controller.setDefaultDeadZone(deadZone);
						}
					} else {
						if(Log.debugEnabled) {
							Log.logger.debug("WARNING: Mapping does not exist: " + mappingFileName);
						}
					}
				} finally {
					try {
						propIn.close();
					} catch(Exception ex) {
						// ignore
					}
				}
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IllegalStateException("Failed to process mappings from resources: " + ex);
		}
	}

	/**
	 * 
	 * @param type
	 * @param properties
	 */
	private static void addMappings(Properties properties) {
		long deviceTypeIdentifier = extractDeviceIdentifier(properties);
		
		defaultButtonLabelMap.put(deviceTypeIdentifier, new HashMap<ButtonID, String>());
		buttonLabelKeyMap.put(deviceTypeIdentifier, new HashMap<ButtonID, String>());
		triggerLabelKeyMap.put(deviceTypeIdentifier, new HashMap<TriggerID, String>());
		defaultTriggerLabelMap.put(deviceTypeIdentifier, new HashMap<TriggerID, String>());
		
		Enumeration<Object> keys = properties.keys();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String namePart = key.substring(key.indexOf(".") + 1);
			String value = properties.getProperty(key);
			if(key.startsWith("button.")) {
				addMapping(MappingType.BUTTON, namePart, value, deviceTypeIdentifier);
			} else if(key.startsWith("stick.")) {
				addMapping(MappingType.STICK_AXIS, namePart, value, deviceTypeIdentifier);
			} else if(key.startsWith("trigger.")) {
				addMapping(MappingType.TRIGGER_AXIS, namePart, value, deviceTypeIdentifier);
			} else if(key.startsWith("dpad.")) {
				addMapping(MappingType.DPAD_AXIS, namePart, value, deviceTypeIdentifier);
			} else if(key.startsWith("buttonlabel.")) {
				Map<ButtonID, String> buttonMap = getOrCreateMapForButton(defaultButtonLabelMap, deviceTypeIdentifier);
				buttonMap.put(getButtonIDfromPropertyKey(key), value);
			} else if(key.startsWith("triggerlabel.")) {
				Map<TriggerID, String> triggerMap = getOrCreateMapForTrigger(defaultTriggerLabelMap, deviceTypeIdentifier);
				triggerMap.put(getTriggerIDfromPropertyKey(key), value);
			} else if(key.startsWith("buttonlabelkey.")) {
				Map<ButtonID, String> buttonMap = getOrCreateMapForButton(buttonLabelKeyMap, deviceTypeIdentifier);
				buttonMap.put(getButtonIDfromPropertyKey(key), value);
			} else if(key.startsWith("triggerlabelkey.")) {
				Map<TriggerID, String> triggerMap = getOrCreateMapForTrigger(triggerLabelKeyMap, deviceTypeIdentifier);
				triggerMap.put(getTriggerIDfromPropertyKey(key), value);
			} 
		}
	}

	/**
	 * Get the mapping for a given button, trigger, dpad or stick axis.
	 * 
	 * @param controller The controller for which to retrieve the mapping.
	 * @param type The mapping type.
	 * @param value The number of the button or axis.
	 * @return The mapping string, or null, if none was found.
	 */
	public static String getMapping(IController controller,MappingType type, int value) {
		Map<Integer, String> idMap = null;
		if(type == MappingType.BUTTON) {
			idMap = buttonMapId.get(controller.getDeviceTypeIdentifier());
		} else if(type == MappingType.DPAD_AXIS) {
			idMap = dpadAxisMapId.get(controller.getDeviceTypeIdentifier());
		} else if(type == MappingType.TRIGGER_AXIS) {
			idMap = triggerAxisMapId.get(controller.getDeviceTypeIdentifier());
		} else if(type == MappingType.STICK_AXIS) {
			idMap = stickAxisMapId.get(controller.getDeviceTypeIdentifier());
		}
		if(idMap != null) {
			return idMap.get(value);
		}
		return null;
	}

	/**
	 * Adds a mapping for a certain component of the controller.
	 * 
	 * @param type The type (button, axis, trigger...)
	 * @param namePart Name part of property name (name of button, axis etc.)
	 * @param value The number of the axis or button.
	 * @param deviceTypeIdentifier
	 */
	private static void addMapping(MappingType type, String namePart, String value, long deviceTypeIdentifier) {
		if(type == MappingType.BUTTON) {
			Map<Integer, String> buttonMap = getOrCreateMapForDevice(buttonMapId, deviceTypeIdentifier);
			if(Log.debugEnabled) {
				Log.logger.debug("Add mapping for BUTTON: " + namePart + "=" + value);
			}
			buttonMap.put(intFromString(value), namePart);
		} else if(type == MappingType.TRIGGER_AXIS) {
			Map<Integer, String> triggerMap = getOrCreateMapForDevice(triggerAxisMapId, deviceTypeIdentifier);
			if(Log.debugEnabled) {
				Log.logger.debug("Add mapping for TRIGGER: " + namePart + "=" + value);
			}
			triggerMap.put(intFromString(value), namePart);
		} else if(type == MappingType.DPAD_AXIS) {
			Map<Integer, String> dpadMap = getOrCreateMapForDevice(dpadAxisMapId, deviceTypeIdentifier);
			if(Log.debugEnabled) {
				Log.logger.debug("Add mapping for DPAD: " + namePart + "=" + value);
			}
			dpadMap.put(intFromString(value), namePart);
		} else if(type == MappingType.STICK_AXIS) {
			Map<Integer, String> stickMap = getOrCreateMapForDevice(stickAxisMapId, deviceTypeIdentifier);
			if(Log.debugEnabled) {
				Log.logger.debug("Add mapping for STICK: " + namePart + "=" + value);
			}
			stickMap.put(intFromString(value), namePart);
		}
	}

	/**
	 * Converts a string value to a Float.
	 * 
	 * @param value The string value.
	 * @return The Float.
	 * @throws IllegalArgumentException If the given string was not a numerical value.
	 */
	private static Float floatFromString(String value) {
		try {
			return Float.parseFloat(value);
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Not a valid numeric value: " + value);
		}
	}

	/**
	 * Converts a string value to an Integer.
	 * 
	 * @param value The string value.
	 * @return The Integer.
	 * @throws IllegalArgumentException If the given string was not a numerical value.
	 */
	private static Integer intFromString(String value) {
		try {
			return Integer.parseInt(value);
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Not a valid numeric value: " + value);
		}
	}

	/**
	 * Returns the hash map for the given device and mapping type, if it exists.
	 * If not, it is created.
	 *  
	 * @param idMap The map in which to look for the requested map.
	 * @param deviceTypeIdentifier The device type identifier.
	 * @return The newly created, or already existing map.
	 */
	private static Map<Integer, String> getOrCreateMapForDevice(Map<Long, Map<Integer, String>> idMap, long deviceTypeIdentifier) {
		Map<Integer, String> map = idMap.get(deviceTypeIdentifier);
		if(map == null) {
			map = new HashMap<Integer, String>();
			Log.logger.debug("===> CREATE MAP FOR ID: " + deviceTypeIdentifier);
			idMap.put(deviceTypeIdentifier, map);
		}
		return map;
	}

	/**
	 * Returns the hash map for the given device and mapping type, if it exists.
	 * If not, it is created.
	 *  
	 * @param idMap The map in which to look for the requested map.
	 * @param deviceTypeIdentifier The device type identifier.
	 * @return The newly created, or already existing map.
	 */
	private static Map<ButtonID, String> getOrCreateMapForButton(Map<Long, Map<ButtonID, String>> idMap, long deviceTypeIdentifier) {
		Map<ButtonID, String> map = idMap.get(deviceTypeIdentifier);
		if(map == null) {
			map = new HashMap<ButtonID, String>();
			idMap.put(deviceTypeIdentifier, map);
		}
		return map;
	}

	/**
	 * Returns the hash map for the given device and mapping type, if it exists.
	 * If not, it is created.
	 *  
	 * @param idMap The map in which to look for the requested map.
	 * @param deviceTypeIdentifier The device type identifier.
	 * @return The newly created, or already existing map.
	 */
	private static Map<TriggerID, String> getOrCreateMapForTrigger(Map<Long, Map<TriggerID, String>> idMap, long deviceTypeIdentifier) {
		Map<TriggerID, String> map = idMap.get(deviceTypeIdentifier);
		if(map == null) {
			map = new HashMap<TriggerID, String>();
			idMap.put(deviceTypeIdentifier, map);
		}
		return map;
	}

	/**
	 * Allows to check if there is a mapping for the given controller.
	 *
	 * @param controller The controller for which a mapping is required.
	 * @return True if a mapping exists, false if not.
	 */
	public static boolean hasMapping(DesktopController controller) {
		long deviceTypeIdentifier = controller.getDeviceTypeIdentifier();
		Log.logger.debug("===> HAS MAP FOR ID: " + deviceTypeIdentifier);
		if(buttonMapId.get(deviceTypeIdentifier) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Creates the device identifier by building a long value based on
	 * the vendor ID and the product ID in the given properties.
	 * 
	 * @param properties The mapping properties.
	 * @return The device identifier.
	 */
	private static long extractDeviceIdentifier(Properties properties) {
		int vendorID = -1, productID = -1;
		try {
			vendorID = Integer.parseInt(properties.getProperty("vendor.id", ""), 16);
			productID = Integer.parseInt(properties.getProperty("product.id", ""), 16);
		} catch(NumberFormatException ex) {
			// ignore
			ex.printStackTrace();
		}
		if(vendorID == -1) {
			throw new IllegalArgumentException("Invalid/missing vendor ID propery ('vendor.id') in mapping.");
		}
		if(productID == -1) {
			throw new IllegalArgumentException("Invalid/missing product ID propery ('product.id') in mapping.");
		}
		return (vendorID << 16) + productID;
	}
	
	/**
	 * Returns the number of triggers on the given controller, as
	 * defined by the mapping.
	 * 
	 * @param controller The controller.
	 * @return The number of triggers.
	 */
	public static int getNumberOfTriggers(IController controller) {
		Map<Integer, String> map = triggerAxisMapId.get(controller.getDeviceTypeIdentifier());
		if(map != null) {
			return map.size();
		}
		return 0;
	}
	
	/**
	 * Returns the number of analog sticks on the given controller, as
	 * defined by the mapping.
	 * 
	 * @param controller The controller.
	 * @return The number of sticks.
	 */
	public static int getNumberOfSticks(IController controller) {
		Map<Integer, String> map = stickAxisMapId.get(controller.getDeviceTypeIdentifier());
		if(map != null) {
			return map.size() / 2;
		}
		return 0;
	}
	
	/**
	 * Returns the default text for the label for the given button.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param buttonID The ID of the button.
	 * @return The default text, or null, if none was defined.
	 */
	public static String getButtonLabel(IController controller, ButtonID buttonID) {
		Map<ButtonID, String> idMap = defaultButtonLabelMap.get(controller.getDeviceTypeIdentifier());
		if(idMap != null) {
			return idMap.get(buttonID);
		}
		return null;
	}
	
	/**
	 * Returns the resource key for the label for the given button.
	 * 
	 * @param controller The controller to which the button belongs.
	 * @param buttonID The ID of the button.
	 * @return The resource key, or null, if none was defined.
	 */
	public static String getButtonLabelKey(IController controller, ButtonID buttonID) {
		Map<ButtonID, String> idMap = buttonLabelKeyMap.get(controller.getDeviceTypeIdentifier());
		if(idMap != null) {
			return idMap.get(buttonID);
		}
		return null;
	}
	
	/**
	 * Returns the default text for the label for the given trigger.
	 * 
	 * @param controller The controller to which the trigger belongs.
	 * @param triggerID The ID of the trigger.
	 * @return The default text, or null, if none was defined.
	 */
	public static String getTriggerLabel(IController controller, TriggerID triggerID) {
		Map<TriggerID, String> idMap = defaultTriggerLabelMap.get(controller.getDeviceTypeIdentifier());
		if(idMap != null) {
			return idMap.get(triggerID);
		}
		return null;
	}
	
	/**
	 * Returns the resource key for the label for the given trigger.
	 * 
	 * @param controller The controller to which the trigger belongs.
	 * @param triggerID The ID of the trigger.
	 * @return The resource key, or null, if none was defined.
	 */
	public static String getTriggerLabelKey(IController controller, TriggerID triggerID) {
		Map<TriggerID, String> idMap = triggerLabelKeyMap.get(controller.getDeviceTypeIdentifier());
		if(idMap != null) {
			return idMap.get(triggerID);
		}
		return null;
	}
	
	/**
	 * Returns the default text label for the given trigger.
	 * 
	 * @param triggerID The ID of the trigger.
	 * @return The default label, or null.
	 */
	public static String getDefaultTriggerLabel(TriggerID triggerID) {
		return defaultLabels.getProperty("triggerlabel." + triggerID.name());
	}
	
	/**
	 * Returns the default text label for the given button.
	 * 
	 * @param buttonID The ID of the button.
	 * @return The default label, or null.
	 */
	public static String getDefaultButtonLabel(ButtonID buttonID) {
		return defaultLabels.getProperty("buttonlabel." + buttonID.name());
	}

	/**
	 * @param key
	 */
	public static ButtonID getButtonIDfromPropertyKey(String key) {
		String buttonIDvalue = key.substring(key.indexOf(".") + 1);
		ButtonID id = ButtonID.getButtonIDfromString(buttonIDvalue);
		if(id == null) {
			throw new IllegalArgumentException("Invalid button ID in property key '" + key + "'");
		}
		return id;
	}

	/**
	 * @param key
	 */
	public static TriggerID getTriggerIDfromPropertyKey(String key) {
		String triggerIDvalue = key.substring(key.indexOf(".") + 1);
		TriggerID id = TriggerID.getTriggerIDfromString(triggerIDvalue);
		if(id == null) {
			throw new IllegalArgumentException("Invalid trigger ID in property key '" + key + "'");
		}
		return id;
	}

	/**
	 * @param key
	 */
	public static StickID getStickIDfromPropertyKey(String key) {
		String stickIDvalue = key.substring(key.indexOf(".") + 1, key.lastIndexOf("."));
		StickID id = StickID.getStickIDfromString(stickIDvalue);
		if(id == null) {
			throw new IllegalArgumentException("Invalid stick ID '" + stickIDvalue + "' in property key '" + key + "'");
		}
		return id;
	}
	
}
