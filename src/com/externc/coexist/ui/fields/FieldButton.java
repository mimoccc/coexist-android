package com.externc.coexist.ui.fields;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

public abstract class FieldButton{

	private EditText edit;
	
	public FieldButton(EditText edit){
		this.edit = edit;
	}


	protected EditText getEdit() {
		return edit;
	}


	public abstract View getButton(Context context);
	
	public abstract String getFieldFill();

	public abstract void onClick(EditText edit);

	
}
