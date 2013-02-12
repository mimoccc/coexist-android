package com.externc.coexist.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class Create implements Iterable<Form>, Writable{
	
	ArrayList<Form> forms;

	public Create() {
		// TODO Auto-generated constructor stub
	}

	public Iterator<Form> iterator() {
		return this.forms.iterator();
	}
	
	public Form get(int index){
		return forms.get(index);
	}
	
	public List<Form> getForms(){
		return this.forms;
	}

	public byte[] getBytes() {
		Serializer s = new Serializer();
		return s.encode(this).getBytes();
	}

	
	
}
