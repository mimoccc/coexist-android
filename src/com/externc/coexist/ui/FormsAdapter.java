package com.externc.coexist.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.externc.coexist.Config;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Metamodel;
import com.externc.coexist.api.Form;

public class FormsAdapter extends BaseAdapter implements OnItemClickListener {

	private Metamodel create;
	private Context context;
	
	public FormsAdapter(Context context) throws FileNotFoundException, IOException {
		DebugLogger.log(this, Level.LOW, "Creating a FormAdapter and getting Create from file system.");
		this.context = context;
		create = new Config(context).getCreate();
	}

	public int getCount() {
		return create.getForms().size();
	}

	public Object getItem(int position) {
		return create.getForms().get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			DebugLogger.log(this, Level.HIGH, "old view was null, creating a new one for position " +position);
		
			convertView = View.inflate(context, android.R.layout.simple_list_item_1, null);
			TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
			tv.setText(((Form)getItem(position)).getLabel());
		}else{
			DebugLogger.log(this, Level.HIGH, "using the old view for position "+position);
		}
		
		return convertView;
	}


	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		DebugLogger.log(this, Level.LOW, "Starting new activity for "+create.getForms().get(position).getLabel());
		
		Intent intent = new Intent(context, AddFragment.class);
		intent.putExtra("form", create.getForms().get(position));
		context.startActivity(intent);
	}


}






