package com.externc.coexist.services;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.IntentService;

import com.externc.coexist.Notifications;
import com.externc.coexist.api.API;
import com.externc.coexist.api.Serializer;
import com.externc.coexist.base.BaseHttpClient;

public abstract class BaseService extends IntentService{

	private Notifications nm;
	private HttpClient client;
	private RequestBuilder builder;
	private Serializer serializer;
	private API api = null;
	
	public BaseService(String name) {
		super(name);
		nm = new Notifications(this);
		client = new BaseHttpClient(this);
		builder = new RequestBuilder(this);
		serializer = new Serializer();
	}
	
	protected Serializer getSerializer(){
		return this.serializer;
	}
	
	protected API getAPI(){
		if(this.api == null)
			api = new API();
		return api;
	}
	
	protected void addParameter(String key, Object value){
		this.builder.addParameter(key, value);
	}
	
	protected String getUrl(){
		return this.builder.getUrl();
	}
	
	protected void sendStartSync(String message){
		nm.startSync(message);
	}
	
	protected HttpClient getClient(){
		return this.client;
	}
	
	protected void sendEndSync(boolean error, String message){
		nm.endSync(error,message);
	}

	
	protected HttpResponse execute(String url) throws ClientProtocolException, IOException{
//		DebugLogger.log(this, Level.LOW, "Creating get requset for " + url);
		return this.client.execute(new HttpGet(url));
	}
	
	/**
	 * Get the target server-side api of this service. This may look
	 * something like "schema" or "sync"; the end of the url after
	 * the word "api" in the url: "http://domain.com/api/schema". It is
	 * used in url building.
	 */
	protected abstract String targetApi();
	

}
