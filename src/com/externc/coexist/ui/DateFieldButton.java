package com.externc.coexist.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.externc.coexist.R;
import com.externc.coexist.ui.components.DatePickerFragment;


public class DateFieldButton extends FieldButton{

	Button button;

	public DateFieldButton(Context context, EditText edit) {
		super(context,edit);
		
	}

	@Override
	public String getFieldFill() {
		
		return "2013-2-2";
	}

	@Override
	public View getButton() {
		if(button == null){
			button = (Button)View.inflate(getContext(), R.layout.field_button, null);
			button.setText("date");
			button.setOnClickListener(this);
		}
		
		return button;
	}

	@Override
	public void onClick(View v) {
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.setEditText(getEdit());
//	    newFragment.show(FormActivity.manager, "datePicker");
	}

	

}
