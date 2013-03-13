package com.externc.coexist.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Form;
import com.externc.coexist.base.BaseActivity;
import com.externc.coexist.base.BaseListFragment;

public class BrowseFragment extends BaseListFragment {

	private Form form;
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		form = getActivity().getIntent().getParcelableExtra("form");
		if(form == null){
			throw new IllegalStateException("The activity should have been given an intent with a Form in it.");
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new BrowseAdapter(getActivity(), form));
		
	}
	

	@Override
	public void onStart() {
		super.onStart();
		getListView().setFastScrollEnabled(true);
		getListView().setHeaderDividersEnabled(true);
	}
	
	public void refresh(){
		setListAdapter(new BrowseAdapter(getActivity(), form));
	}
	
}
