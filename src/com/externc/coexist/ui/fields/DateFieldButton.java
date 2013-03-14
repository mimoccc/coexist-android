package com.externc.coexist.ui.fields;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.externc.coexist.R;
import com.externc.coexist.ui.components.DatePickerFragment;


public class DateFieldButton extends FieldButton{


	public DateFieldButton(EditText edit) {
		super(edit);
	}

	@Override
	public String getFieldFill() {
		return "2013-2-2";
	}

	@Override
	public View getButton(Context context) {
		Button button = (Button)View.inflate(context, R.layout.field_button, null);
		button.setText("date");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		
		return button;
	}

	@Override
	public void onClick(EditText edit) {
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.setEditText(edit);
//	    newFragment.show(FormActivity.manager, "datePicker");
	}

	

}
