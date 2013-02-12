package com.externc.coexist.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Serializer {
	
	
	public enum Mode{
		XML, JSON
	}

	
	private Gson gson;
	private Mode mode = Mode.JSON;
	
	public Serializer() {
		DebugLogger.log(this,Level.LOW,"Making a serializer");
		this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS ")
				.serializeSpecialFloatingPointValues().create();
	}

	public  <T> T decode(InputStream data, Class<T> clazz){
		return decode(Serializer.streamToString(data),clazz);
//		return decode(Serializer.convertStreamToString(data),clazz);
	}
	
	public  <T> T decode(String data, Class<T> clazz){
		return getMode() == Mode.JSON ? decodeJSON(data, clazz) : decodeXML(data, clazz);
	}
	
	public  String encode(Object data){
		return getMode() == Mode.JSON ? encodeJSON(data) : encodeXML(data);
	}
	
	private  <T> T decodeJSON(String data, Class<T> clazz){
		DebugLogger.log(this,Level.LOW,"Decoding the json data into a " + clazz);
		DebugLogger.log(this,Level.HIGH,data);
		return this.gson.fromJson(data, clazz);
	}
	
	private String encodeJSON(Object data){
		String json = gson.toJson(data);
		DebugLogger.log(this,Level.HIGH,"Encoding " + data + " into JSON:" + json);
		return json;
	}
	
	
	private  <T> T decodeXML(String data, Class<T> clazz){
		DebugLogger.log(this, Level.LOW, "No XML support implemented yet.");
		return null;
	}
	
	private  String encodeXML(Object data){
		DebugLogger.log(this,Level.LOW,"No XML support implemented yet.");
		return null;
	}
	
	public void setMode(Mode mode){
		this.mode = mode;
	}
	
	public Mode getMode(){
		return this.mode;
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}
	
	public static String streamToString(InputStream is){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while( (line = reader.readLine()) != null){
				builder.append(line);
			}
			return builder.toString();
		}catch(Exception e){
			return "";
		}
	}
	
}







