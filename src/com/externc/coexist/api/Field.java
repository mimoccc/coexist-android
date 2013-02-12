package com.externc.coexist.api;


import android.os.Parcel;
import android.os.Parcelable;

public class Field implements Parcelable{
	
	private String label;
	private String type;
	
	
	//optional parameters with default values
	private String column = label;
	private boolean required = false;
	private Reference references;
	

	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(label);
		dest.writeString(type);
		dest.writeString(column);
		dest.writeInt(required ? 1 : 0);
		API.writeNullableParcelable(references, dest, flags);
	}
	
	private Field(Parcel in) {
		this.label = in.readString();
		this.type = in.readString();
		this.column = in.readString();
		this.required = in.readInt() == 1;
		this.references = API.readNullableparcelable(in, Reference.CREATOR);
	}

	public static final Parcelable.Creator<Field> CREATOR = new Parcelable.Creator<Field>() {
		public Field createFromParcel(Parcel in) {
			return new Field(in);
		}

		public Field[] newArray(int size) {
			return new Field[size];
		}
		
		
	};
	
	public Field() {

	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getColumn() {
		return column;
	}


	public void setColumn(String column) {
		this.column = column;
	}


	public boolean isRequired() {
		return required;
	}


	public void setRequired(boolean required) {
		this.required = required;
	}


	public Reference getReferences() {
		return references;
	}


	public void setReferences(Reference references) {
		this.references = references;
	}


	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	

	
	
}
