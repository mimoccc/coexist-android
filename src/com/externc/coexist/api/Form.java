package com.externc.coexist.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;

import android.os.Parcel;
import android.os.Parcelable;

public class Form implements Parcelable, Iterable<Field>{

	private String label;
	private String table;
	private List<Field> fields = new ArrayList<Field>();

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(label);
		out.writeString(table);
		out.writeTypedList(fields);
	}

	private Form(Parcel in) {
		this.label = in.readString();
		this.table = in.readString();
		in.readTypedList(fields, Field.CREATOR);
    }
	
    public static final Parcelable.Creator<Form> CREATOR
            = new Parcelable.Creator<Form>() {
        public Form createFromParcel(Parcel in) {
            return new Form(in);
        }

        public Form[] newArray(int size) {
            return new Form[size];
        }
    };
    

	
	protected Form() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public List<Field> getFields() {
		return fields;
	}

	public String getFieldsAsSql(){
		
		Iterator<Field> iter = fields.iterator();
		
		StringBuilder sb = new StringBuilder(iter.next().getColumn());
		while(iter.hasNext()){
			Field f = iter.next();
			DebugLogger.log(this, Level.HIGH, String.format("FIELD: %s, %s, %s",f.getLabel(),f.getColumn(),f.getType()));
			sb.append(","+f.getColumn());
			
		}

		DebugLogger.log(this, Level.LOW, "Create a sql string for "+getLabel()+": "+sb.toString());
		return sb.toString();
	}
	
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}
	
	public int size(){
		return this.fields.size();
	}
	
	public Field get(int index){
		return this.fields.get(index);
	}
	
	@Override
	public String toString(){
		return this.label;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Iterator<Field> iterator() {
		return getFields().iterator();
	}


}
