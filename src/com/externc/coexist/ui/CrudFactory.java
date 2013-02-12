package com.externc.coexist.ui;

import android.support.v4.app.Fragment;

public class CrudFactory {

	
	public CrudFactory() {
	}
	
	public static Fragment getFragment(int index){
		
		switch (index) {
		case 1:
			return new BrowseFragment();
//		case 1:
//			return new SearchFragment();
		case 0:
			return new AddFragment();
		default:
			return null;
		}
		
	}
	
	public static String getTitle(int index){
		switch (index) {
		case 1:
			return "Browse";
//		case 1:
//			return "Search";
		case 0:
			return "Add";
		default:
			return null;
		}
	}
	
	public static int getSize(){
		return 2;
	}

	public static int getStartIndex(){
		return 1;
	}
	
}
