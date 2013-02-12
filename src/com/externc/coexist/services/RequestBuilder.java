package com.externc.coexist.services;

import android.net.Uri;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.R;

public class RequestBuilder {

	private BaseService service;
	private Uri.Builder builder = null;
	
	protected RequestBuilder(BaseService service) {
		this.service = service;
		
		
	}
	
	/**
	 * Coupling work around.... cannot instanitate in constructor atm
	 */
	private void init(){
		if(builder == null){
			String baseUrl =  service.getResources().getString(R.string.api_baseurl);
			builder = Uri.parse(baseUrl + service.targetApi()).buildUpon();
		}
	}
	
	
	public RequestBuilder addParameter(String key, Object value){
		init();
		String jsonValue = service.getSerializer().encode(value);
		DebugLogger.log(this, Level.LOW, "Adding "+key+"="+jsonValue);
		builder.appendQueryParameter(key, jsonValue);
		return this;
	}

	public String getUrl(){
		init();
		return builder.build().toString();
	}
	
}
