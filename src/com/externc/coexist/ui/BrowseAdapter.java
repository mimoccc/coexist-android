package com.externc.coexist.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.externc.coexist.Database;
import com.externc.coexist.Database.JointCursor;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Form;
import com.externc.coexist.api.Row;
import com.externc.coexist.ui.components.BrowseRow;

/**
 * This is the adapter that is used in the BrowseFragment for
 * displaying the contents of a single table / form.
 * @author Anthony Naddeo
 *
 */
public class BrowseAdapter extends BaseAdapter {

	private Form form;
	private JointCursor cursor;
	private Context context;
	
	private final int disabledRows = 2;
	
	public BrowseAdapter(Context context, Form form, Database db) {
		DebugLogger.log(this, Level.LOW, "Created with context: "+context);
		this.context = context;
		this.form = form;
		this.cursor = db.getTableRows(form);
	}

	public int getCount() {
		return cursor.cursor().getCount()+disabledRows;
	}

	public Cursor getCursor(int position){
		this.cursor.cursor().moveToPosition(position-disabledRows);
		return this.cursor.cursor();
	}
	
	public Object getItem(int position) {
		Cursor cursor = getCursor(position);
		Object o = Row.fromCursor(cursor);
		return o;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * Right now, item 0 is the column names, 1 is a small separator and
	 * the rest are data.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {
			if(convertView == null)
				return BrowseRow.getHeader(context, form);
			else{
				BrowseRow.writeHeader((BrowseRow)convertView, form);
				return convertView;
			}
		
		}else if(position == 1){
			if(convertView == null){
				BrowseRow br = new BrowseRow(context);
				br.setSeparator(true);
				return br;
			}else{
				((BrowseRow)convertView).setSeparator(true);
				return convertView;
			}
		}else {
			Cursor cursor = getCursor(position);
			if(convertView == null){
				return BrowseRow.getRow(context, cursor);
			}else{
				BrowseRow.writeRow((BrowseRow)convertView, cursor);
				return convertView;
			}
		}
	}

	
	@Override
	public boolean isEnabled(int position) {
		return position >= disabledRows;
	}
	
	/**
	 * Close the cursor and database connection. This is called by the 
	 * parent fragment when it's onDestroy() is called.
	 */
	public void close(){
		this.cursor.close();
	}
	
}





