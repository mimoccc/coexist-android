package com.externc.coexist.services;


import org.apache.http.HttpResponse;

import android.content.Intent;

import com.externc.coexist.Config;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Create;
import com.externc.coexist.api.CrudResponse;

public class CrudService extends BaseService {

	public CrudService() {
		super("crud");
		DebugLogger.log(this, Level.LOW,"Created crud service");
	}

	@Override
	protected String targetApi() {
		return "crud";
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		DebugLogger.log(this, Level.LOW, "Fetch crud from the api.");
		sendStartSync("Downloading the form data.");
		Config config = new Config(this);
		
		try{
			HttpResponse response = execute(getUrl());
			DebugLogger.log(this, Level.LOW, "Sending request to: "+getUrl());
			CrudResponse crud = getSerializer().decode(response.getEntity().getContent(), CrudResponse.class);
			DebugLogger.log(this, Level.LOW, "Got the response.");
			
			
			Create create = crud.getCreate();
			config.setCreate(create);
			

			getAPI().sync(this);
		}catch(Exception e){
			DebugLogger.log(this, Level.LOW, "Error: "+ e.toString());
			sendEndSync(true, "unknown");
		}
		
		
	}

}
