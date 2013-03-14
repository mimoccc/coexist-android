package com.externc.coexist;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Form;
import com.externc.coexist.api.Row;
import com.externc.coexist.api.Schema;
import com.externc.coexist.api.Sync;
import com.externc.coexist.ui.fields.FieldSection;

/**
 * This class will be used to interface with the database. Only read operations will
 * be used from the GUI application. The services will use create and sync.
 * @author Anthony Naddeo
 *
 */
public class Database {

//	private static Database dbSingleton;
//	private static int connections = 0;
	public static Database getDatabase(Context context){
//		if(dbSingleton == null)
//			dbSingleton = new Database(context);
//		connections++;
//		return dbSingleton;
		return new Database(context);
	}
	
	
	private Context context;
//	private SQLiteDatabase database;
	
	private Database(Context context){
		this.context = context;
	}
	
	/**
	 * @return A Context t this database was created with.
	 */
	private Context getContext(){
		return this.context;
	}
	
	/**
	 * @return a Resources object from the context.
	 */
	private Resources getResources(){
		return this.context.getResources();
	}

	public void close(){
//		connections--;
//		if(this.database != null && connections == 0)
//			this.database.close();
	}
	
	/**
	 * One of the fundamental API functions. Using a Schema that was
	 * retrieved from the back end server, this function will execute the
	 * sql within it to create the database and all of the tables.
	 */
	public void create(Schema schema) throws SQLiteException{
		DebugLogger.log(this, Level.LOW, "Starting to create the database.");
		erase();
		
		SQLiteDatabase db = open();
		db.beginTransaction();
		
		DebugLogger.log(this, Level.LOW, "Executing the server's sql");
		for(String sql : schema){
			DebugLogger.log(this, Level.HIGH, sql);
			db.execSQL(sql);
		}
		
		db.execSQL("CREATE TABLE Recents( " +
				"form VARCHAR(30) PRIMARY KEY," +
				"recents VARCHAR(255))");
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
		Config conf = new Config(getContext());
		conf.setVersion(schema.getVersion());
		
		DebugLogger.log(this, Level.LOW, "Done, releasing the database reference.");
		db.close();
	}

	/**
	 * This function will simply delete the database file. It is called within create.
	 */
	public void erase() {
		String path = getPath();
		DebugLogger.log(this, Level.LOW, "Erasing the database at " + path);
		getContext().deleteDatabase(path);
	}

	public void sync(Sync diff) {
		DebugLogger.log(this, Level.LOW, "Importing the sync.");
		SQLiteDatabase db = open();
		db.beginTransaction();
		try{
			for(String table : diff){
				if(diff.tableIsEmpty(table))
					continue;
				
				String sql = "INSERT OR REPLACE INTO "+table+"("+diff.getColumnString(table)+") values("+diff.getColumnBindString(table)+")";
				
				DebugLogger.log(this, Level.HIGH, "Executing: "+sql);

				for(Row row : diff.getTables().get(table)){
					db.execSQL(sql, row.getValues());
				}
			}
			db.setTransactionSuccessful();
		}catch(SQLiteException e){
			DebugLogger.log(this, Level.LOW, "Could not import, "+e.getMessage());
		}finally{
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * Returns the path of the database file. The path will be a database dir within 
	 * the data dir of this application, and the file itself is defined as a string
	 * resource app_database. The path may look something like 
	 * /data/data/net.networkmaine.inventory/databases/dbname.
	 * @return The path of the database file.
	 */
	private String getPath(){
		File dbDir = new File(getContext().getApplicationInfo().dataDir + "/databases");
		if(!dbDir.exists())
			dbDir.mkdirs();
		
		return dbDir.getPath() +"/"+ getResources().getString(R.string.app_name);
	}
	
	
	/**
	 * Opens a database connection and returns it. The database will be created if 
	 * it does not exist, and the path will be retreived from getpath().
	 * @return An open database connection.
	 */
	private SQLiteDatabase open(){
//		if(this.database == null){
			String path = getPath();
			DebugLogger.log(this, Level.LOW, "Opening a database connection to " + path);
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path, null);
//		}
		
		return database;
	}
	

	
	public List<String> getRecentValues(String form){
		SQLiteDatabase db = open();
		
		List<String> recents = new ArrayList<String>();
		Cursor c = db.rawQuery(
				String.format("select recents from Recents where form='%s'",form),null);
		
		String recent = null;
		while(c.moveToNext()){
			recent = c.getString(0);
		}

		try{
			for(String s : recent.split(","))
				recents.add(s);
			
			if(c != null)
				c.close();
			return recents;
		}catch(NullPointerException e){
			return null;
		}finally{
			db.close();
		}
		
	}
	
	public void setRecentValues(String form, List<String> values){
		SQLiteDatabase db = open();
		try{
			db.beginTransaction();
			db.execSQL(String.format("INSERT OR REPLACE INTO Recents values('%s','%s')", form, join(values)));
			
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch(SQLiteException e){
			DebugLogger.log(this, Level.LOW, "Could not import, "+e.getMessage());
		}finally{
			db.close();	
		}
	}
	

	public List<String> getTables() {
		DebugLogger.log(this, Level.LOW, "Getting all of the tables.");
		SQLiteDatabase db = open();
		
		List<String> tables = new ArrayList<String>();
		Cursor c = db.rawQuery("select name " +
				"from sqlite_master " +
				"where type='table' and name not like 'android%' " +
				"and name not like 'sqlite%' and name <> 'Recents'" +
				"order by name",null);
		
		while(c.moveToNext()){
			tables.add(c.getString(0));
		}
		
		if(c != null)
			c.close();
		db.close();
		return tables;
	}

	public List<String> getModts(List<String> tables) {
		DebugLogger.log(this, Level.LOW, "Getting the maximum time stamps.");
		SQLiteDatabase db = open();
		
		List<String> modts = new ArrayList<String>();
		Cursor c = null;
		for(String table : tables){
			DebugLogger.log(this, Level.HIGH, "Getting timestamp for " + table);
			
			c = db.rawQuery("select max(mod_ts) from "+table,null);
			
			while(c.moveToNext()){
				String ts = c.getString(0);
				modts.add(ts == null ? "0000-00-00 00:00:00" : ts);
			}
		}
		
		if(c != null)
			c.close();
		db.close();
		return modts;
	}
	
	
	public Cursor getTableRows(Form form){
		String table = form.getTable();
		DebugLogger.log(this, Level.LOW, "Getting rows for "+table);
		SQLiteDatabase db = open();
		
		Cursor c = db.rawQuery("SELECT "+form.getFieldsAsSql()+" FROM "+table, null);
		return c;
	}

	private String printf(String s, Object ...objects ){
		return String.format(s,objects);
	}
	
	public String join(List<String> strings){
		Iterator<String> iter = strings.iterator();
		if(!iter.hasNext())
			return "";
		StringBuilder sb = new StringBuilder(printf("%s", iter.next().toString()));
		while(iter.hasNext())
			sb.append(printf(",%s",iter.next().toString()));
		return sb.toString();
	}
}





