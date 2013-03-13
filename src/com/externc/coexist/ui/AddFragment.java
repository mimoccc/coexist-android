package com.externc.coexist.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.R;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Field;
import com.externc.coexist.api.Form;
import com.externc.coexist.api.Sync;
import com.externc.coexist.api.SyncBuilder;

public class AddFragment extends SherlockFragment implements SyncCreator{

	private Form form;
	private List<FieldSection> sections = new ArrayList<FieldSection>();
	private LinearLayout layout = null;
	
	public AddFragment() {	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		form = getActivity().getIntent().getParcelableExtra("form");
		if(form == null){
			throw new IllegalStateException("The activity should have been given an intent with a Form in it.");
		}
		
		((FormActivity)activity).setCreator(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		DebugLogger.log(this, Level.LOW, "Called create view");
		
		if(layout != null){
			((ViewGroup)layout.getParent()).removeAllViews();
			return layout;
		}

		layout = (LinearLayout)inflater.inflate(R.layout.form,null);
		
		if(sections.size() == 0){
			for(Field field : form){
				FieldSection section = new FieldSection(getActivity(), field);
				sections.add(section);
				layout.addView(section.getView());
			}
		}else{
			for(FieldSection section : sections){
				layout.addView(section.getView());
			}
		}
		
		return layout;
	}


	
	public Sync getSync() {
		SyncBuilder sb = new SyncBuilder();
		sb.addRow(form.getTable(), sections);
		return sb.getSync();
	}

	
}











