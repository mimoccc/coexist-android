package com.externc.coexist.api;

import android.os.Parcel;
import android.os.Parcelable;

public class View extends Form implements Parcelable{

//	private String label;
//	private String table;
	private String sql;
	
	private View() {
		super();
	}

	private View(Parcel in){
		setLabel(in.readString());
		setTable(in.readString());
		this.sql = in.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getLabel());
		dest.writeString(getTable());
		dest.writeString(sql);
	}
	
	public static final Parcelable.Creator<View> CREATOR = new Parcelable.Creator<View>() {
		public View createFromParcel(Parcel in) {
			return new View(in);
		}

		public View[] newArray(int size) {
			return new View[size];
		}
	};

	@Override
	public int size() {
		return 0;
	};

	public String getSql() {
		return sql;
	}
	
	

}
