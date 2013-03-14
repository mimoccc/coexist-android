package com.externc.coexist.ui.components;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class DatePickerFragment extends SherlockDialogFragment implements
		DatePickerDialog.OnDateSetListener {

	private EditText edit;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void setEditText(EditText edit){
		this.edit = edit;
	}

	@Override
	public void onDateSet(android.widget.DatePicker view, int year,
			int monthOfYear, int dayOfMonth) {
		if(edit == null)
			throw new IllegalStateException("Must set an EditText before selecting date.");
		
		edit.setText(year + "-" +(monthOfYear+1)+"-"+dayOfMonth);
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
}