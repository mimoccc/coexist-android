package com.externc.coexist.ui;

import java.util.List;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.externc.coexist.R;
import com.externc.coexist.api.Field;

public class FieldSection {

	private Context context;
	private Field field;
	private List<FieldButton> buttons;
	
	private RelativeLayout layout;
	private TextView label;
	private EditText entry;
	
	private static int ids = 0;
	private static int getId(){
		return ++ids;
	}
	
	public FieldSection(Context context, Field field) {
		this.context = context;
		this.field = field;
		
		this.layout = (RelativeLayout)View.inflate(context, R.layout.field, null);
		
		
		
		label = (TextView)layout.findViewById(R.id.field_label);
		label.setText(field.getLabel());
		this.entry = (EditText)layout.findViewById(R.id.field_entry);
		this.entry.setId(getId());
		
		setInputType();
		
		
		LinearLayout buttonLayout = (LinearLayout)layout.findViewById(R.id.field_button_layout);
		this.buttons = FieldButtonFactory.getFieldButtons(context, entry, field.getType());
		for(FieldButton button : buttons){
			buttonLayout.addView(button.getButton());
		}
	}
	
	private void setInputType(){
		if(this.field.getType().equals("integer"))
			this.entry.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
	}
	
	public View getView(){
		return this.layout;
	}
	
	public String getColumn(){
		return field.getColumn();
	}
	
	public String getEntry(){
		return entry.getText().toString();
	}

}
