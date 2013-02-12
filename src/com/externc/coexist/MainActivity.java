package com.externc.coexist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.API;
import com.externc.coexist.ui.HomeFragment;
import com.externc.coexist.R;

public class MainActivity extends SherlockFragmentActivity{

	
	public static String getFinishedSyncAction(Context context){
		return context.getResources().getString(R.string.action_finishedsync);
	}
	
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(intent.getAction() == getFinishedSyncAction(MainActivity.this)){
				DebugLogger.log(this, Level.LOW, "Got broadcast, refreshing the main window.");
				runOnUiThread(new Runnable() {
					public void run() {
						refresh();
					}
				});
			}
		}
	};
	
	
	public MainActivity(){} 
	
	private void refresh(){
		FragmentManager man = getSupportFragmentManager();
		FragmentTransaction t = man.beginTransaction();
		t.replace(R.id.frame, new HomeFragment());
		t.commit();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);
        
        FragmentManager man = getSupportFragmentManager();
        FragmentTransaction t = man.beginTransaction();
        t.add(R.id.frame, new HomeFragment());
        t.commit();
        
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(getFinishedSyncAction(this));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    } 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_sync:
    		sync();
    		return true;
    	case R.id.menu_resync:
    		schema();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    	
    }

    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	
    }

    
    public void sync(){
    	DebugLogger.log(this,Level.LOW, "Starting the sync service.");
    	new API().sync(this);
    }

    public void schema(){
    	DebugLogger.log(this,Level.LOW,"Startring the schema service.");
    	new API().schema(this);
    }
	
    public void crud(){
    	DebugLogger.log(this, Level.LOW, "Starting the crud service.");
    	new API().crud(this);
    }
    
    
}
