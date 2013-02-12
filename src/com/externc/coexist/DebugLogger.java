package com.externc.coexist;



/**
 * <p>This class should aid with debugging. To view logs on android, after
 * you have downloaded and setup your environment, all you have to do is
 * execute "adb logcat" with a device plugged in (or a VM). adb comes
 * with the android sdk. You may want to try something like
 * </p>
 *  
 * <pre>
 * adb logcat | grep "Networkmaine"
 * </pre>
 * 
 * <p>
 * Its best to add Debug messages as you create classes, and pay attention
 * to the level. Assign HIGH to something like printing the contents of every
 * item in an array (something verbose). Assign LOW to everything else.
 * </p>
 * 
 * @author Anthony Naddeo
 *
 */
public class DebugLogger {

	public static final boolean ENABLED = true;
	public static final String TAG = "Coexist";	
	private static final Level level = Level.HIGH;
	
	public enum Level{
		OFF, LOW, HIGH
	}
	
	private DebugLogger() {}
	
	/**
	 * Declare a log message. The class name will be prepended to the message
	 * so you know where it came from. The level will be printed if it is less
	 * than or equal to level.
	 * @param c The class that declares the message.
	 * @param level A Level corresponding to its verbosity.
	 * @param message A message to log.
	 */
	public static void log(Class<? extends Object> c, Level level, String message){
		if(!ENABLED)
			return;
		
		switch(DebugLogger.level){
		case HIGH:
			print(c,message);
			break;
		case LOW:
			if(level != Level.HIGH)
				print(c,message);
			break;
		default:
			break;
		
		}
		
		
	}
	
	/**
	 * Convenience method for passing in objects instead of classes, so
	 * it can be called using <tt>this</tt>.
	 * @param caller The calling object. 
	 * @param level A Level corresponding to its verbosity.
	 * @param message A message to log.
	 */
	public static void log(Object caller, Level level, String message){
		log(caller.getClass(), level, message);
	}
	
	/**
	 * This function does the actual printing. Currently, Log.d is used.
	 * @param c
	 * @param message
	 */
	private static void print(Class <? extends Object > c, String message){
		android.util.Log.d( TAG + "/" + c.toString() + ": ", message);
	}

}
