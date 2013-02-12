package com.externc.coexist.ui.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.widget.LinearLayout;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.R;
import com.externc.coexist.api.Form;

public class BrowseRow extends LinearLayout implements Iterable<BrowseColumn> {

	public static int MAX_ROWS = 10;

	public static BrowseRow getHeader(Context context, Form form){
		return getHeader(new BrowseRow(context), form);
	}

	public static BrowseRow getHeader(BrowseRow row, Form form){
		row.setSeparator(false);
		for(int i = 0; i < form.size() && i < MAX_ROWS; i++){
			row.addColumn(form.get(i).getLabel());
		}
		return row;
	}
	
	public static BrowseRow getRow(Context context, Cursor cursor){
		return getRow(new BrowseRow(context), cursor);
	}
	
	public static BrowseRow getRow(BrowseRow row, Cursor cursor){
		row.setSeparator(false);
		for(int i = 0; i < cursor.getColumnCount() && i < MAX_ROWS; i++){
			row.addColumn(cursor.getString(i));
		}
		
		return row;
	}
	
	public static void writeHeader(BrowseRow row, Form form){
		if(row.getChildCount() == 0){
			getHeader(row, form);
			return;
		}
		
		for(int i = 0; i < row.size() && i < MAX_ROWS; i++){
			row.setColumn(i, form.getFields().get(i).getLabel());
		}
	}

	public static void writeRow(BrowseRow row, Cursor cursor){
		if(row.getChildCount() == 0){
			getRow(row, cursor);
			return;
		}
		
		// TODO we have to guarantee this is the same order as the columns
		DebugLogger.log(BrowseRow.class, Level.LOW, "re writing row: row size is "+row.size()+", cursor size is "+cursor.getColumnCount());
		for (int i = 0; i < cursor.getColumnCount() && i < MAX_ROWS; i++) {
			row.setColumn(i, cursor.getString(i));
		}
	}
	
	List<BrowseColumn> views = new ArrayList<BrowseColumn>();
	
	public BrowseRow(Context context) {
		super(context);
	}

	public void setSeparator(boolean sep){
		if(sep){
			setBackgroundColor(getResources().getColor(R.color.app_color));
			removeAllViews();
			setPadding(2, 2, 2, 2);
		}else{
			setPadding(0, 0, 0, 0);
			setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
		
	}
	
	public void addColumn(String text){
		BrowseColumn column = new BrowseColumn(getContext());
		column.setText(text);
		views.add(column);
		this.addView(column.getView());
	}
	
	public void setColumn(int index, String text){
		BrowseColumn column = views.get(index);
		column.setText(text);
	}
	
	public int size(){
		return views.size();
	}

	public Iterator<BrowseColumn> iterator() {
		return views.iterator();
	}
	
}
