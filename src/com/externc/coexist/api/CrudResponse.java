package com.externc.coexist.api;


public class CrudResponse extends Response {

	private Metamodel create;
	private int version;
	
	private CrudResponse() {

	}
	
	public Metamodel getCreate(){
		return this.create;
	}
	
	public int getVersion(){
		return this.version;
	}
	

}
