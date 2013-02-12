package com.externc.coexist.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;

public class CrudPager extends FragmentStatePagerAdapter {

	BrowseFragment bf;
	
	public CrudPager(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		if(index == 1){
			bf = (BrowseFragment)CrudFactory.getFragment(index);
			return bf;
		}
		return CrudFactory.getFragment(index);
	}

	@Override
	public int getCount() {
		return CrudFactory.getSize();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return CrudFactory.getTitle(position);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		DebugLogger.log(this, Level.LOW, "The dataset has changed");
	}
	
	@Override
	public int getItemPosition(Object object) {
		DebugLogger.log(this, Level.LOW, "Getting item position.");
		return POSITION_NONE;
	}
	
	public void reset(){
		bf.reset();
	}
	
}
