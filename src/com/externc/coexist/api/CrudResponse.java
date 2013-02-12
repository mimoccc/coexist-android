package com.externc.coexist.api;


public class CrudResponse extends Response {

	private Create create;
	private int version;
	
	private CrudResponse() {

	}
	
	public Create getCreate(){
		return this.create;
	}
	
	public int getVersion(){
		return this.version;
	}
	

}
