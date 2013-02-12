package com.externc.coexist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.externc.coexist.api.Form;

public class HomeFragment extends SherlockListFragment{

	private FormsAdapter adapter;
	
	public HomeFragment() {}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			adapter = new FormsAdapter(getSherlockActivity());
			setListAdapter(adapter);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), FormActivity.class);
		intent.putExtra("form", ((Form)adapter.getItem(position)));
		startActivity(intent);
	}

}
