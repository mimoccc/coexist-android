package com.externc.coexist.ui.components;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.externc.coexist.R;

public class BrowseColumn {

	private TextView view;
	private Context context;
	
	private static final int COLUMN_LAYOUT = R.layout.text;
	public static final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT, 1f);
	
	public BrowseColumn(Context context) {
		this.context = context;
		view = (TextView)View.inflate(getContext(), COLUMN_LAYOUT, null);
		view.setLayoutParams(params);
	}

	private Context getContext(){
		return this.context;
	}
	
	public void setText(String text){
		this.view.setText(text);
	}
	
	public View getView(){
		return this.view;
	}

}
