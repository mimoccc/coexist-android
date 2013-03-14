package com.externc.coexist.ui;

import android.app.Activity;
import android.os.Bundle;

import com.externc.coexist.Database;
import com.externc.coexist.api.Form;
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
		refresh();
	}
	

	@Override
	public void onStart() {
		super.onStart();
		getListView().setFastScrollEnabled(true);
		getListView().setHeaderDividersEnabled(true);
	}
	
	@Override
	public void refresh(){
		setListAdapter(new BrowseAdapter(getActivity(), form, Database.getDatabase(getActivity())));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		((BrowseAdapter)getListAdapter()).close();
	}
	
}
