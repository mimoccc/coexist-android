package com.externc.coexist.api;


public class MetamodelResponse extends Response {

	private Metamodel create;
	private int version;
	
	private MetamodelResponse() {

	}
	
	public Metamodel getCreate(){
		return this.create;
	}
	
	public int getVersion(){
		return this.version;
	}
	

}
