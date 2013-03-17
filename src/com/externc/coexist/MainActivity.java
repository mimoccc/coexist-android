package com.externc.coexist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.API;
import com.externc.coexist.base.BaseActivity;
import com.externc.coexist.ui.HomeFragment;

public class MainActivity extends BaseActivity{

	
	protected void refresh(){
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
