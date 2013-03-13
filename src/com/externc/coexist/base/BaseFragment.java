package com.externc.coexist.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.actionbarsherlock.app.SherlockFragment;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;

public abstract class BaseFragment extends SherlockFragment {

	/**
	 * This broadcast receiver will allow activities to receive broadcasts from the 
	 * services when syncs have been completed.
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction() == BaseActivity.getFinishedSyncAction(BaseFragment.this.getActivity())){
				DebugLogger.log(this, Level.LOW, "Got broadcast, refreshing the main window.");
				AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
					@Override
					protected void onPostExecute(Void result) {
						refresh();
					};
					@Override
					protected Void doInBackground(String... params) {
						return null;
					}
				};
				task.execute();
			}
		}
	};
	
	
	/**
	 * This method is called when a sync is completed. It should contain whatever
	 * code is necessary for the activity to refresh its fragments / views.
	 */
	abstract protected void refresh();
	
	
	/**
	 * Every onCreate, Activities will register themselves as broadcast
	 * receivers.
	 */
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		DebugLogger.log(this, Level.LOW, "Called onCreate");
		
		IntentFilter filter = new IntentFilter();
        filter.addAction(BaseActivity.getFinishedSyncAction(getActivity()));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
	}
	
	/**
	 * Unregister the fragment from broadcasts when stopped.
	 */
	@Override
	public void onStop() {
		super.onStop();
		DebugLogger.log(this, Level.LOW, "Called stop");
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
	}
}
