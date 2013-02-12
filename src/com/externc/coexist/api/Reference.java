package com.externc.coexist.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Reference implements Parcelable{

	private String table;
	private String column;
	
	public Reference() {
		// TODO Auto-generated constructor stub
	}
	
	public void writeToParcel(Parcel out, int flags) {
        out.writeString(table);
        out.writeString(column);
    }

	 private Reference(Parcel in) {
		 this.table = in.readString();
		 this.column = in.readString();
	 }
	
    public static final Parcelable.Creator<Reference> CREATOR
            = new Parcelable.Creator<Reference>() {
        public Reference createFromParcel(Parcel in) {
            return new Reference(in);
        }

        public Reference[] newArray(int size) {
            return new Reference[size];
        }
    };

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
    
   

}
