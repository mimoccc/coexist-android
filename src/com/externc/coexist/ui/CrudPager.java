package com.externc.coexist.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;

public class CrudPager extends FragmentStatePagerAdapter {

	
	public CrudPager(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 1:
			return new BrowseFragment();
		case 0:
			return new AddFragment();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
	public CharSequence getPageTitle(int index) {
		switch (index) {
		case 1:
			return "Browse";
		case 0:
			return "Add";
		default:
			return null;
		}
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
	

	
}
