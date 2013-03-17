package com.externc.coexist.ui.fields;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.externc.coexist.Database;
import com.externc.coexist.R;
import com.externc.coexist.api.Field;

public class DialogListFragment extends DialogFragment implements OnItemClickListener{

	private EditText edit;
	private Field field;
	private ListView lv;
	
	public interface DialogListFragmentCreator{
		public EditText getEdit();
		public Field getField();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(edit == null || field == null)
			throw new IllegalStateException("Must call both setEdit and setField before using this.");
		
		
		getDialog().setTitle(field.getLabel());
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_list_view, null);
		ListView lv = (ListView)layout.findViewById(R.id.list_view_list);
		List<String> values = Database.getDatabase(getActivity()).getReferences(field);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		this.lv = lv;
		
		
		return layout;
	}


	
	public void setEdit(EditText edit) {
		this.edit = edit;
	}



	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * This is a temporary work around. On orientation changes, the EditText is
	 * rebuilt, but this Dialog is still showing with the old EditText's reference.
	 * The way I have it setup, there is no easy way to get the new EditText 
	 * to this fragment, so I'll dismiss it on orientation change and
	 * make the user click again...for now.
	 */
	@Override
	public void onPause() {
		super.onPause();
		dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.edit.setText(lv.getAdapter().getItem(position).toString());
		this.dismiss();
	}
	
	
	

}
