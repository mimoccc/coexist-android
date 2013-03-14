package com.externc.coexist.ui.fields;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.R;
import com.externc.coexist.api.Field;
import com.externc.coexist.ui.FormLogger;
import com.externc.coexist.ui.components.DatePickerFragment;

public class FieldSection implements OnFocusChangeListener{

	
	private final List<FieldButton> buttons;
	private final Field field;
	private final FormLogger logger;
	private final TextView label;
	private final EditText entry;
	private final RelativeLayout layout;
	
	
	private static int ids = 0;
	private static int getId(){
		return ++ids;
	}
	
	public FieldSection(Context context, Field field, FormLogger logger) {
		this.field = field;
		this.logger = logger;
		
		this.layout = (RelativeLayout)View.inflate(context, R.layout.field, null);
		
		label = (TextView)layout.findViewById(R.id.field_label);
		label.setText(field.getLabel());
		this.entry = (EditText)layout.findViewById(R.id.field_entry);
		this.entry.setId(getId());
		this.entry.setOnFocusChangeListener(this);
		
		setInputType();
		
		
		this.buttons = new ArrayList<FieldButton>();
		

	}
	
	private void setInputType(){
		if(this.field.getType().equals("integer")){
			this.entry.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
		
		}else if(this.field.getType().equals("date")){
			this.entry.setInputType(InputType.TYPE_NULL);
//			this.entry.setFocusable(false);
			this.entry.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DatePickerFragment newFragment = new DatePickerFragment();
					newFragment.setEditText(entry);
					newFragment.show(logger.getManager(), "datePicker");					
				}
			});
			this.entry.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						DatePickerFragment newFragment = new DatePickerFragment();
						newFragment.setEditText(entry);
						newFragment.show(logger.getManager(), "datePicker");
					}else
						logger.log();
				}
			});

		}else if(this.field.getType().equals("reference")){
			this.entry.setInputType(InputType.TYPE_NULL);
//			this.entry.setFocusable(false);
			this.entry.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogListFragment f = new DialogListFragment();
					f.setEdit(entry);
					f.setField(field);
					f.show(logger.getManager(), "reference");					
				}
			});
			this.entry.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						DialogListFragment f = new DialogListFragment();
						f.setEdit(entry);
						f.setField(field);
						f.show(logger.getManager(), "reference");
					}else
						logger.log();
				}
			});
		}
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
	
	public void setEntry(String text){
		entry.setText(text);
	}
	
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		DebugLogger.log(this, Level.LOW, "Saving the form values.");
		logger.log();
	}

}
