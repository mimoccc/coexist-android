package com.externc.coexist.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;



public class Metamodel implements Iterable<Form>, Writable{
	
	ArrayList<Form> forms;
	ArrayList<View> views;

	public Metamodel() {
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
	
	public List<View> getViews(){
		return this.views;
	}

	public byte[] getBytes() {
		DebugLogger.log(this, Level.LOW, "Saving metamodel with "+forms.size()+" forms and "+views.size()+" views");
		Serializer s = new Serializer();
		return s.encode(this).getBytes();
	}

	
	
}
