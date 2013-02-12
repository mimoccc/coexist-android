package com.externc.coexist.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.MainActivity;
import com.externc.coexist.R;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.API;
import com.externc.coexist.api.Form;

public class FormActivity extends SherlockFragmentActivity {

	private ViewPager pager;
	private FragmentStatePagerAdapter adapter;
	private Form form;
	private SyncCreator creator;

	protected static FragmentManager manager;
	
	public static String getFinishedSyncAction(Context context){
		return context.getResources().getString(R.string.action_finishedsync);
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(intent.getAction() == getFinishedSyncAction(FormActivity.this)){
				DebugLogger.log(this, Level.LOW, "Got broadcast, refreshing the main window.");
				runOnUiThread(new Runnable() {
					public void run() {
						refresh();
					}
				});
			}
		}
	};
	
	
	public FormActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		form = getIntent().getParcelableExtra("form");
		getSherlock().getActionBar().setTitle(form.getLabel());

		setContentView(R.layout.pager);
		adapter = new CrudPager(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setCurrentItem(1);
		
		
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
	protected void onResume() {
		super.onResume();
		manager = getSupportFragmentManager();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		manager = null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_form, menu);
		return true;
	}

	protected void setCreator(SyncCreator creator) {
		this.creator = creator;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_create:
			create();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void create() {
		new API().create(this, creator.getSync());
	}
	
	private void refresh(){
		((CrudPager)adapter).reset();
	}

}
