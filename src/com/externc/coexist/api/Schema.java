package com.externc.coexist.api;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used to deserialize the responses from the server
 * from /api/schema using Gson.
 * @author Anthony Naddeo
 *
 */
public class Schema extends Response implements Iterable<String> {
	
	private int version;
	private ArrayList<String> sql;
	
	
	public Schema() {}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Iterator<String> iterator() {
		return sql.iterator();
	}
	
	
}
