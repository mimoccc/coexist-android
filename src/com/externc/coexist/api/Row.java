package com.externc.coexist.api;

import java.util.Iterator;
import java.util.LinkedHashMap;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;

@SuppressWarnings("serial")
public class Row extends LinkedHashMap<String, String> implements Iterable<String>, Parcelable{

	public static Row fromCursor(Cursor c){
		Row row = new Row();
		for(int i = 0; i < c.getColumnCount(); i++){
			row.put(c.getColumnNames()[i], c.getString(i));
		}
		return null;
	}
	
	
	
	protected Row() {
		super();
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		DebugLogger.log(this, Level.HIGH, "Parcelilzing with "+size()+" tuples");
		dest.writeInt(size());
		for(String key : keySet()){
			DebugLogger.log(this, Level.HIGH, "writing "+key+" : "+get(key));
			dest.writeString(key);
			dest.writeString(get(key));
		}
	}
	
	private Row(Parcel in) {
		super();
	    int size = in.readInt();
	    DebugLogger.log(this, Level.HIGH, "Deparcelilzing with "+size+" tuples");
	    for(int i = 0; i < size; i++){
	    	String key = in.readString();
	    	String value = in.readString();
	    	DebugLogger.log(this, Level.HIGH, "reading "+key+" : "+value);
	    	put(key,value);
	    }
	}
	
    public static final Parcelable.Creator<Row> CREATOR
            = new Parcelable.Creator<Row>() {
        public Row createFromParcel(Parcel in) {
            return new Row(in);
        }

        public Row[] newArray(int size) {
            return new Row[size];
        }
    };

	public Iterator<String> iterator() {
		return super.keySet().iterator();
	}

	public String[] getValues(){
		String[] values = new String[size()];
		return values().toArray(values);
	}

	public int describeContents() {
		return 0;
	}


	
}
