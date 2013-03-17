package com.externc.coexist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.externc.coexist.DebugLogger.Level;

/**
 * This class can be used to send notifications to the tray that
 * alerts the user when syncs have started and ended. For now, it
 * is being used solely for syncs. This class is used in the NMService
 * abstract class and allows the services to call sendStartSync() and
 * sendEndSync() to send thier notifications.
 * @author Anthony Naddeo
 *
 */
public class Notifications{

	
	//completely arbitrary message ID, it is used to modify or remove
	//the notification in the notification tray.
	private final int id = 234; 
	private Context context;
	
	public Notifications(Context context){
		this.context = context;
	}
	
	/**
	 * Use the context to retrieve a NotificationManager. This will be used to create
	 * notifications.
	 * @return a NotificationManager.
	 */
	private NotificationManager getNmanager() {
		return (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	/**
	 * Create a PendingIntent that when executed, will launch this application.
	 * This is what will be used when users click on the sync notification
	 * in the notification tray. Originally, clicking the notification
	 * was going to do nothing, but older versions of android would crash
	 * if there was no intent, so for now it will launch the application.
	 * @return A PendingIntent that launches this application.
	 */
	private PendingIntent getPendingIntent(){
		Intent intent =  getContext().getPackageManager().getLaunchIntentForPackage(getContext().getApplicationInfo().packageName);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		PendingIntent pending = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		return pending;
	}
	
	/**
	 * Gets a NotificationCompat.Builder object that is pre-configured with
	 * a sync icon, a title, and a PendingIntent from getPendingIntent. 
	 * @param message The message to display in the notification. Probably
	 * one of getStartMessage() or getEndMessage().
	 * @return A NotificationCompat.Builder object that build() can be 
	 * called on and used to send a notification.
	 */
	private NotificationCompat.Builder getBuilder(String message){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
		builder.setContentTitle(getTitle());
		builder.setContentText(message);
		builder.setAutoCancel(true);
		builder.setContentIntent(getPendingIntent()); //neccessary for backwards compatibility
		return builder;
	}
	
	/**
	 * Calling this will send a notification to the phone, letting the user
	 * know that a sync has begun. 
	 */
	public void startSync(String message){
		DebugLogger.log(this, Level.LOW, "Sending sync start notification.");
		NotificationManager nm = getNmanager();
		//should be using build() instead of getNotification() but Actionbar Sherlock
		//it using an older support library.
		nm.notify(getMessageId(), getBuilder(message).setSmallIcon(getStartIcon()).setProgress(100, 0, true).build());
	}
	
	/**
	 * Calling this will send a notification to the phone, letting the user
	 * know that a sync has ended, and whether or not it was successful.
	 * @param error If true, the notification will show that an error occured.
	 */
	public void endSync(boolean error, String message){
		DebugLogger.log(this, Level.LOW, "Sending sync end notification, " +  (error?"one":"no") + " error");
		NotificationManager nm = getNmanager();
		nm.notify(getMessageId(), getBuilder(message).setSmallIcon(getEndIcon()).build());
	}
	
	
	/**
	 * Gets the context that was used to create this object.
	 * @return A Context.
	 */
	private Context getContext(){
		return this.context;
	}
	
	/**
	 * Get the ID used for the sync notifications. This is a completely arbitrary
	 * number that can be used to modify or cancel that notification. 
	 * @return Some arbitrary int, defined as a final in source code.
	 */
	public int getMessageId(){
		return this.id;
	}
	
	/**
	 * Gets the title of the notification. It will be the same whether it is
	 * starting or ending.
	 * @return The title of the notification, defined as a string resource.
	 */ 
	private String getTitle(){
		return getContext().getResources().getString(R.string.app_name);
	}

	/**
	 * Gets the icon that should be displayed in the notification. This is
	 * defined as a resource.
	 * @return The icon drawable that should be displayed in the notification.
	 */
	private int getStartIcon(){
		return android.R.drawable.stat_sys_download;
	}

	private int getEndIcon(){
		return R.drawable.notification;
	}
	
}
