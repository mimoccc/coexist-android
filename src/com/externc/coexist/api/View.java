package com.externc.coexist.api;

import android.os.Parcel;
import android.os.Parcelable;

public class View implements Parcelable{

	private String label;
	private String table;
	private String sql;
	
	public View() {
		
	}

	private View(Parcel in){
		this.label = in.readString();
		this.table = in.readString();
		this.sql = in.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(label);
		dest.writeString(table);
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

}
