package com.externc.coexist.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;


public class Sync extends Response implements Iterable<String>, Parcelable{

	private LinkedHashMap<String, MissingRows> tables = new LinkedHashMap<String, MissingRows>();
	
	protected Sync(){
		super();
	}
	
	public void writeToParcel(Parcel out, int flags) {
		DebugLogger.log(this, Level.HIGH, "Parcelilzing with "+tables.size()+" tuples");
		out.writeInt(tables.size());
		for(String key : tables.keySet()){
			DebugLogger.log(this, Level.HIGH, "writing "+key+" : "+tables.get(key));
			out.writeString(key);
			out.writeTypedList(tables.get(key));
		}
	}

	private Sync(Parcel in) {
		super();
		int size = in.readInt();
		DebugLogger.log(this, Level.HIGH, "Deparcelilzing with "+size+" tuples");
		for(int i = 0; i < size; i++){
			String key = in.readString();
			DebugLogger.log(this, Level.HIGH, "reading "+key);
			//treat the MissingRows as a typed arraylist instead of a parcelable.
			//Very weird behavior otherwise... something must be wrong with the
			//parcelable implementation in MissingRows.
			ArrayList<Row> value = new MissingRows();
			in.readTypedList(value, Row.CREATOR);
			DebugLogger.log(this, Level.HIGH, "reading "+value);
			tables.put(key, (MissingRows)value);
		}
	}
	
    public static final Parcelable.Creator<Sync> CREATOR
            = new Parcelable.Creator<Sync>() {
        public Sync createFromParcel(Parcel in) {
            return new Sync(in);
        }

        public Sync[] newArray(int size) {
            return new Sync[size];
        }
    };
	
	
	protected MissingRows get(String table){
		return tables.get(table);
	}

	protected void put(String table, MissingRows rows){
		this.tables.put(table, rows);
	}
	
	public Iterator<String> iterator() {
		return tables.keySet().iterator();
	}
	
	public Map<String, MissingRows> getTables(){
		return this.tables;
	}
	
	public boolean tableIsEmpty(String table){
		return getTables().get(table).isEmpty();
	}
	
	public int numRows(){
		int numRows = 0;
		for(MissingRows rows : getTables().values()){
			numRows += rows.size();
		}
		return numRows;
	}
	
	public String getColumnString(String table){
		DebugLogger.log(this, Level.LOW, "Getting a string of columns for the sql query, for "+table);
		Row row = getTables().get(table).get(0);
		
		String columnString = "";
		Iterator<String> iter = row.iterator();
		while(iter.hasNext()){
			columnString += iter.next() + (iter.hasNext() ? ",":"");
		}
		DebugLogger.log(this, Level.LOW, "Done.");
		return columnString;
	}

	public String getColumnBindString(String table){
		DebugLogger.log(this, Level.LOW, "Getting a string of bind args for the sql query, for "+table);
		Row row = getTables().get(table).get(0);

		String columnString = "";
		Iterator<String> iter = row.iterator();
		while(iter.hasNext()){
			iter.next();
			columnString += "?" + (iter.hasNext() ? ",":"");
		}
		DebugLogger.log(this, Level.LOW, "Done.");
		return columnString;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString(){
		return tables.toString();
	}
}
