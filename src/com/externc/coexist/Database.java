package com.externc.coexist;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.R.string;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Field;
import com.externc.coexist.api.Form;
import com.externc.coexist.api.Row;
import com.externc.coexist.api.Schema;
import com.externc.coexist.api.Sync;

/**
 * This class will be used to interface with the database. Only read operations will
 * be used from the GUI application. The services will use create and sync.
 * @author Anthony Naddeo
 *
 */
public class Database {

	/**
	 * Connection handling can be tricky. Forcing this pattern in
	 * case I need to use singleton (or something else) in the future.
	 * @param context 
	 * @return A new database.
	 */
	public static Database getDatabase(Context context){
		return new Database(context);
	}

	/**
	 * A helper class to encapsulate a SqliteDatabase cursor. I need
	 * to hand Cursors out to adapters and I can't close the database
	 * until they are done with the cursor. Instead, I'll hand out these
	 * new cursors and they can call close() on them without worrying about
	 * the database connection.
	 * @author Anthony Naddeo
	 *
	 */
	public class JointCursor {
		final private Cursor c;
		final private SQLiteDatabase d;
		public JointCursor(android.database.Cursor c, SQLiteDatabase d){
			this.c = c; this.d = d;
		}
		public void close(){c.close(); d.close();}
		public Cursor cursor(){return c;}
	}
	
	private Context context;
	
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

	
	/**
	 * One of the fundamental API functions. Using a Schema that was
	 * retrieved from the back end server, this function will execute the
	 * sql within it to create the database and all of the tables.
	 */
	public void create(Schema schema) throws SQLiteException{
		DebugLogger.log(this, Level.LOW, "Starting to create the database.");
		erase();
		
		SQLiteDatabase db = open();
		
		try{
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
		}catch(SQLiteException e){
			DebugLogger.log(this, Level.LOW, 
					"Could not create the tables that the server returned.\n" + e.getMessage());
		}finally{
			DebugLogger.log(this, Level.LOW, "Done, releasing the database reference.");
			db.close();
		}
		
	}

	/**
	 * This function will simply delete the database file. It is called within create
	 * to reset the database before syncing with new metadata. This would happen when
	 * the server side version is changed and the client needs to resync it self and all
	 * metadata.
	 */
	public void erase() {
		String path = getPath();
		DebugLogger.log(this, Level.LOW, "Erasing the database at " + path);
		getContext().deleteDatabase(path);
	}

	/**
	 * Sync the local database with the remove server. The server will respond to
	 * a sync request (initiated in the SyncService) with a JSON object that maps
	 * to the Sync object in this project. The SyncService will hand that sync 
	 * object over to this database and this method will import or replace
	 * all of the tuples from the server.
	 * @param diff A Sync object given by the remove server.
	 */
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
			db.endTransaction();
		}catch(SQLiteException e){
			DebugLogger.log(this, Level.LOW, "Could not import, "+e.getMessage());
		}finally{
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
		String path = getPath();
		DebugLogger.log(this, Level.LOW, "Opening a database connection to " + path);
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path, null);
		
		return database;
	}
	

	/**
	 * Gets the most recent values for a particular form so they can be
	 * reinserted into the EditTexts of the add page so you don't have to
	 * start from scratch each time you open it up.
	 * @param form The name of a form / table.
	 * @return A list of strings that should be inserted into each of the 
	 * FieldSections in an add page.
	 */
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

	/**
	 * Set the recent values for a form. This is how the values of
	 * the EditTexts are saved in forms.
	 * @param form The table name of a form in the database.
	 * @param values A list of strings that will be saved in the database
	 * and reloaded into the input fields every time the add page is visited.
	 */
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
	

	/**
	 * Get a list of tables in the database, not including the sqlite, android,
	 * and Recent values tables. This is displayed on the home page.
	 * @return A list of table names that represent forms in the database.
	 */
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

	/**
	 * Get the amximum modification timestamps for each of the tables. This 
	 * is used in sync requests to determine all of the rows that are missing
	 * on the server from the local cache. This simply gets the contents of the
	 * mod_ts TIMESTAMP field from each table (which is requried by coexist).
	 * @param tables A list of tables to get timestamps from.
	 * @return a list of tiemstamp strings, ultimately sent in a sync request.
	 */
	public List<String> getModts(List<String> tables) {
		DebugLogger.log(this, Level.LOW, "Getting the maximum time stamps.");
		SQLiteDatabase db = open();
		
		try{
			List<String> modts = new ArrayList<String>();
			Cursor c = null;
			//TODO convert this to a UNION instead of multiple queries.
			for(String table : tables){
				DebugLogger.log(this, Level.HIGH, "Getting timestamp for " + table);
				
				c = db.rawQuery("select max(mod_ts) from "+table,null);
				while(c.moveToNext()){
					String ts = c.getString(0);
					modts.add(ts == null ? "0000-00-00 00:00:00" : ts);
				}
				c.close();
			}
			return modts;
		}catch(NullPointerException e){
			DebugLogger.log(this, Level.LOW, "One of the cursors was null.\n"+e.getMessage());
			return null;
		}catch(SQLiteException e){
			DebugLogger.log(this, Level.LOW, "That was a SQL error. Do all tables have a mod_ts?\n"+e.getMessage());
			return null;
		}finally{
			db.close();
		}
		
	}
	
	
	/**
	 * Get a cursor for the contents of a table, which represents a Form.
	 * Remember, you need to call close on this yourself!
	 * @param form The form to get the contents of.
	 * @return A Cursor for browsing the contents of a form.
	 */
	public JointCursor getTableRows(Form form){
		String table = form.getTable();
		String sql = "SELECT "+form.getFieldsAsSql()+" FROM "+table;
		DebugLogger.log(this, Level.LOW, "Getting rows for "+sql);
		SQLiteDatabase db = open();
		
		Cursor c = db.rawQuery(sql, null);
		JointCursor jc = new JointCursor(c, db);
		return jc;
	}
	
	
	/**
	 * Get all values for a particular field. That field must have
	 * a value of "reference" for its type attribute, and it must have
	 * a valid Reference object for its references attribute. This will
	 * be used on the add form. When a user clicks on a FieldSection that
	 * encapsulates a reference Field (a foreign key in SQL), then a list
	 * of options will be presented to them, instead of being allowed to
	 * type values in.
	 * @param field A field that is a reference type.
	 * @return A String list of possible references.
	 */
	public List<String> getReferences(Field field){
		String table = field.getReferences().getTable();
		String column = field.getReferences().getColumn();
		
		DebugLogger.log(this, Level.LOW, "Getting references for "+field);
		
		Cursor c = null;
		SQLiteDatabase db = open();
		try{
			c = db.rawQuery(printf("SELECT DISTINCT %s from %s",column,table), null);
			
			List<String> values = new ArrayList<String>();
			while(c.moveToNext())
				values.add(c.getString(0));
			
			return values;
		}catch(SQLiteException e){
			DebugLogger.log(this, Level.LOW, e.getMessage());
			return null;
		}finally{
			c.close();
			db.close();
		}
		
	}

	/**
	 * Small helper function for using String.format (too verbose)
	 * @param s A string
	 * @param objects Objects to be placed into the string.
	 * @return A formatted string.
	 */
	private String printf(String s, Object ...objects ){
		return String.format(s,objects);
	}
	
	/**
	 * Helper function for converting a String list into a single csv String.
	 * This is used mostly to record values in the Recents table, which has only
	 * two columns: form and recents (it was the simplest structure that worked).
	 * That table is used to log and fill the values of add forms so you see the
	 * values that you left in them last, instead of having to fill everything out
	 * from scratch (which is annoying when adding in bulk).
	 * @param strings The strings to join.
	 * @return A String csv.
	 */
	private String join(List<String> strings){
		Iterator<String> iter = strings.iterator();
		if(!iter.hasNext())
			return "";
		StringBuilder sb = new StringBuilder(printf("%s", iter.next().toString()));
		while(iter.hasNext())
			sb.append(printf(",%s",iter.next().toString()));
		return sb.toString();
	}
}





