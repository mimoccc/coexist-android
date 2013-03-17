package com.externc.coexist.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.R;

public abstract class BaseActivity extends SherlockFragmentActivity {

	public static String getFinishedSyncAction(Context context){
		return context.getResources().getString(R.string.action_finishedsync);
	}

	public static String getServiceProgressAction(Context context){
		return context.getResources().getString(R.string.action_serviceprogress);
	}
	
	
	/**
	 * This broadcast receiver will allow activities to receive broadcasts from the 
	 * services when syncs have been completed.
	 */
	private BroadcastReceiver reciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final Intent safeRef = intent;
			DebugLogger.log(BaseActivity.this, Level.LOW, "Got a broadcast of type %s.", intent.getAction());
			
			if(intent.getAction() == getFinishedSyncAction(BaseActivity.this)){
				DebugLogger.log(BaseActivity.this, Level.LOW, "Got finished sync broadcast, refreshing.");
				runOnUiThread(new Runnable() {
					public void run() {
						dismissProgress(safeRef.getExtras().getBoolean("success"));
						refresh();
					}
				});
				
			}
			
			if(intent.getAction() == getServiceProgressAction(BaseActivity.this)){
				DebugLogger.log(BaseActivity.this, Level.LOW, "Got service progress broadcast, refreshing.");
				runOnUiThread(new Runnable() {
					public void run() {
						servicePorgress(safeRef.getExtras().getString("message"));
					}
				});
			}
		}
	};
	
	
	/**
	 * This method is called when a sync is completed. It should contain whatever
	 * code is necessary for the activity to refresh its fragments / views.
	 */
	protected abstract void refresh();
	
	private ProgressDialog pd;
	
	/**
	 * This method is called when progress happens in any of the services. Its intended
	 * to enable activities to display progress dialogs that align with the notification tray.
	 */
	protected void servicePorgress(String message){
		if(pd == null)
			pd = ProgressDialog.show(this, "Syncing", message, true, false);
		else
			pd.setMessage(message);
	}
	
	protected void dismissProgress(boolean success){
		if(pd != null){
			pd.dismiss();
			pd = null;
			Toast.makeText(this, success?"Success":"Error", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * Every onResume, Activities will register themselves as broadcast
	 * receivers.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(getFinishedSyncAction(this));
		filter.addAction(getServiceProgressAction(this));
        LocalBroadcastManager.getInstance(this).registerReceiver(reciever, filter);
	}
	
	/**
	 * Activities will unregister themselves as broadcast receivers when
	 * they are stopped.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		DebugLogger.log(this, Level.LOW, "Called stop");
		LocalBroadcastManager.getInstance(this).unregisterReceiver(reciever);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		DebugLogger.log(this, Level.LOW, "Called start");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		DebugLogger.log(this, Level.LOW, "Called destroy");
	}

}
