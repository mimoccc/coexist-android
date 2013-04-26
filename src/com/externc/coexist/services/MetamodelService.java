package com.externc.coexist.services;


import org.apache.http.HttpResponse;

import android.content.Intent;

import com.externc.coexist.Config;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Metamodel;
import com.externc.coexist.api.MetamodelResponse;

public class MetamodelService extends BaseService {

	public MetamodelService() {
		super("crud");
		DebugLogger.log(this, Level.LOW,"Created crud service");
	}

	@Override
	protected String targetApi() {
		return "crud";
	}

	@Override
	protected String getUpdateMessage() {
		return "Downloading the form data.";
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		DebugLogger.log(this, Level.LOW, "Fetch crud from the api.");
		sendStartSync();
		Config config = new Config(this);
		
		try{
			sendServiceProgressBroadcast();
			HttpResponse response = execute(getUrl());
			DebugLogger.log(this, Level.LOW, "Sending request to: "+getUrl());
			MetamodelResponse crud = getSerializer().decode(response.getEntity().getContent(), MetamodelResponse.class);
			DebugLogger.log(this, Level.LOW, "Got the response.");
			
			
			Metamodel create = crud.getMetamodel();
			config.setCreate(create);
			
			
			getAPI().sync(this);
		}catch(Exception e){
			DebugLogger.log(this, Level.LOW, "Error: "+ e.toString());
			e.printStackTrace();
			sendEndSync(true, "unknown");
		}
		
		
	}

	
}
