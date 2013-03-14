package com.externc.coexist.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.externc.coexist.Database;
import com.externc.coexist.R;
import com.externc.coexist.api.Field;
import com.externc.coexist.api.Form;
import com.externc.coexist.api.Sync;
import com.externc.coexist.api.SyncBuilder;
import com.externc.coexist.base.BaseFragment;
import com.externc.coexist.ui.fields.FieldSection;

public class AddFragment extends BaseFragment implements SyncCreator, FormLogger{

	private Form form;
	private LinearLayout layout;
	private List<FieldSection> sections;

	
	/**
	 * This fragment will retrieve the Form object that it represents
	 * from its parent activity, which should have been given a Form
	 * as an extra in its intent.
	 */
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

		layout = (LinearLayout)inflater.inflate(R.layout.form,null);
		
		sections = new ArrayList<FieldSection>();
		for(Field field : form){
			FieldSection section = new FieldSection(getActivity(), field, this);
			sections.add(section);
			layout.addView(section.getView());
		}
		
		fillRecents();
		return layout;
	}


	
	public Sync getSync() {
		SyncBuilder sb = new SyncBuilder();
		sb.addRow(form.getTable(), sections);
		return sb.getSync();
	}


	@Override
	protected void refresh() {
		
	}


	private void fillRecents(){
		Database db = Database.getDatabase(getActivity());
		List<String> values = db.getRecentValues(form.getTable());
		if(values == null)
			return;
		Iterator<String> recents = values.iterator();
		Iterator<FieldSection> fields = sections.iterator();
		
		while(recents.hasNext())
			fields.next().setEntry(recents.next().toString());
	}
	
	@Override
	public void log() {
		List<String> values = new ArrayList<String>();
		for(FieldSection f : sections)
			values.add(f.getEntry());
		
		Database db = Database.getDatabase(getActivity());
		db.setRecentValues(form.getTable(), values);
	}



	@Override
	public FragmentManager getManager() {
		return getActivity().getSupportFragmentManager();
	}

	
}











