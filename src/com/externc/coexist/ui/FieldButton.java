package com.externc.coexist.ui;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public abstract class FieldButton implements OnClickListener{

	private Context context;
	private  EditText edit;
	
	public FieldButton(Context context, EditText edit){
		this.context = context;
		this.edit = edit;
	}
	
	
	
	protected Context getContext() {
		return context;
	}



	protected void setContext(Context context) {
		this.context = context;
	}



	protected EditText getEdit() {
		return edit;
	}



	protected void setEdit(EditText edit) {
		this.edit = edit;
	}



	public abstract View getButton();
	
	public abstract String getFieldFill();

	

	
}
