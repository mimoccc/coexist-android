package com.externc.coexist.api;

import java.util.List;

import com.externc.coexist.ui.fields.FieldSection;

public class SyncBuilder {

	private Sync sync = new Sync();
	
	public SyncBuilder() {
		
	}
	
	public Sync getSync(){
		return this.sync;
	}
	
	private MissingRows getMissingRow(String table){
		MissingRows rows = getSync().get(table);
		if(rows == null){
			rows = new MissingRows();
			getSync().put(table, rows);
		}
		return rows;
	}
	
	public void addRow(String table, Row row){
		MissingRows rows = getMissingRow(table);
		rows.add(row);
	}
	
	public void addRow(String table, List<FieldSection> sections){
		MissingRows rows = getMissingRow(table);
		Row row = new Row();
		for(FieldSection section : sections){
			row.put(section.getColumn(), section.getEntry());
		}
		rows.add(row);
	}

}
