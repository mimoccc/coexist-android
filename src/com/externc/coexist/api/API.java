package com.externc.coexist.api;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.services.CreateService;
import com.externc.coexist.services.CrudService;
import com.externc.coexist.services.SchemaService;
import com.externc.coexist.services.SyncService;

/**
 * The API interface for this application. Luckily, the API is
 * simple!
 * @author Anthony Naddeo
 *
 */
public class API{

	
	public API(){
		
	}
	
	/**
	 * Trigger a call to /api/schema to update the schema on this device.
	 * @param context A Context
	 */
	public void schema(Context context){
		Intent intent = new Intent(context, SchemaService.class);
		context.startService(intent);
	}
	
	/**
	 * Trigger a call to /api/sync to get the missing rows from the server.
	 * If there is a version conflict then the SyncService that gets launched
	 * will drop the database and launch a SchemaService, which will intern
	 * launch another SyncService to update the new database.
	 * @param context A Context.
	 */
	public void sync(Context context){
		Intent intent = new Intent(context, SyncService.class);
		context.startService(intent);
	}
	
	public void crud(Context context){
		Intent intent = new Intent(context, CrudService.class);
		context.startService(intent);
	}

	public void create(Context context, Sync sync){
		DebugLogger.log(this, Level.LOW, "Staring Create with this sync: "+sync);
		Intent intent = new Intent(context, CreateService.class);
		intent.putExtra("sync", sync);
		context.startService(intent);
	}
	
	public static void writeNullableString(String toWrite, Parcel dest, int flags) {
		if(toWrite == null){
			dest.writeInt(0);
		}else{
			dest.writeInt(1);
			dest.writeString(toWrite);
		}
	}

	public static String readNullableString(Parcel in){
		boolean wasNull = in.readInt() == 0;
		if(wasNull){
			return null;
		}else{
			return in.readString();
		}
	}
	
	public static void writeNullableParcelable(Parcelable toWrite, Parcel dest, int flags) {
		if(toWrite == null){
			dest.writeInt(0);
		}else{
			dest.writeInt(1);
			toWrite.writeToParcel(dest, flags);
			/**
			 * dest.writeParcelable clearly was not meant to be used
			 * the like toWrite.writeToParcel, it took forever to identify it
			 * as a discreet bug. I'll leave it here to remind myself of the horrors.
			 */
//			dest.writeParcelable(toWrite, flags);
		}
	}

	public static <T> T readNullableparcelable(Parcel in, Creator<T> c){
		boolean wasNull = in.readInt() == 0;
		if(wasNull){
			return null;
		}else{
			return c.createFromParcel(in);
		}
	}
	



}
