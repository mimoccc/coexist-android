package com.externc.coexist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Create;
import com.externc.coexist.api.Serializer;
import com.externc.coexist.R;

public class Config{

	private Context context;
	
	private final String createFile = "create.json";
	
	public Config(Context context) {
		this.context = context;
	}
	
	private Context getContext(){
		return this.context;
	}
	
	private SharedPreferences getPrefs(){
		String name = getContext().getResources().getString(R.string.app_name);
		return getContext().getSharedPreferences(name, Activity.MODE_PRIVATE);
	}

	public int getVersion() {
		return getPrefs().getInt("version", 0);
	}

	public void setVersion(int version) {
		DebugLogger.log(this, Level.LOW, "Updating the schema version to " + version);
		getPrefs().edit().putInt("version", version).commit();
	}
	
	public Create getCreate() throws FileNotFoundException, IOException{
		DebugLogger.log(this, Level.LOW, "Retreiving the Create object from the file system.");
		FileInputStream fis = getContext().openFileInput(createFile);
		Serializer s = new Serializer();
		Create create = s.decode(fis, Create.class);
		
		return create;
	}
	
	public void setCreate(Create create) throws FileNotFoundException, IOException   {
		DebugLogger.log(this, Level.LOW, "Saving "+create+" as the new Create.");
		FileOutputStream fos = getContext().openFileOutput(createFile, Context.MODE_PRIVATE);
		fos.write(create.getBytes());
	}

}
