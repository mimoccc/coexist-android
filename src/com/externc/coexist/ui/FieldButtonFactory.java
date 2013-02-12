package com.externc.coexist.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.EditText;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;

public class FieldButtonFactory {

	public FieldButtonFactory() {

	}
	
	
	public static List<FieldButton> getFieldButtons(Context context, EditText edit, String type){
		DebugLogger.log(FieldButtonFactory.class, Level.LOW, "Getting field buttons for type "+type);
		
		List<FieldButton>list = new ArrayList<FieldButton>();
		
		if(type.equals("date")){
			list.add(new DateFieldButton(context, edit));
		}
		
		return list;
	}

}