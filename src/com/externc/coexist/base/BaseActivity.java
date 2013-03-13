package com.externc.coexist.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.R;

public abstract class BaseActivity extends SherlockFragmentActivity {

	public static String getFinishedSyncAction(Context context){
		return context.getResources().getString(R.string.action_finishedsync);
	}
	
	/**
	 * This broadcast receiver will allow activities to receive broadcasts from the 
	 * services when syncs have been completed.
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction() == getFinishedSyncAction(BaseActivity.this)){
				DebugLogger.log(this, Level.LOW, "Got broadcast, refreshing the main window.");
				runOnUiThread(new Runnable() {
					public void run() {
						refresh();
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
	
	/**
	 * Every onCreate, Activities will register themselves as broadcast
	 * receivers.
	 */
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		DebugLogger.log(this, Level.LOW, "Called onCreate");
		
		IntentFilter filter = new IntentFilter();
        filter.addAction(getFinishedSyncAction(this));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
	}
	
	/**
	 * Activities will unregister themselves as broadcast receivers when
	 * they are stopped.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		DebugLogger.log(this, Level.LOW, "Called stop");
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
