package com.externc.coexist.api;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;

@SuppressWarnings("serial")
public class MissingRows extends ArrayList<Row> implements Parcelable{

	protected MissingRows() {
		super();
	}

	public void writeToParcel(Parcel out, int flags) {
		DebugLogger.log(this, Level.HIGH, "Parcelilzing with "+size()+" rows");
		out.writeInt(size());
		for(Row row : this){
			DebugLogger.log(this, Level.HIGH, "writing "+row);
			out.writeParcelable(row, flags);
		}
	}

	private MissingRows(Parcel in) {
		super();
		int size = in.readInt();
		DebugLogger.log(this, Level.HIGH, "Deparcelilzing with "+size+" rows");
		for(int i = 0; i < size; i++){
			Row row = Row.CREATOR.createFromParcel(in);
			DebugLogger.log(this, Level.HIGH, "reading "+row);
			add(row);
		}
	}
	
    public static final Parcelable.Creator<MissingRows> CREATOR
            = new Parcelable.Creator<MissingRows>() {
        public MissingRows createFromParcel(Parcel in) {
            return new MissingRows(in);
        }

        public MissingRows[] newArray(int size) {
            return new MissingRows[size];
        }
    };

	public int describeContents() {
		return 0;
	}

//	public String toString(){
//		StringBuilder sb = new StringBuilder("["+get(0));
//		for(int i = 1; i < size(); i++){
//			sb.append(", "+get(i));
//		}
//		sb.append("]");
//		return sb.toString();
//	}
	
}
