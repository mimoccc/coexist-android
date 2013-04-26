package com.externc.coexist.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpResponse;

import android.content.Intent;

import com.externc.coexist.Config;
import com.externc.coexist.Database;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Schema;
import com.externc.coexist.api.Serializer;

/**
 * This service is responsible for calling /api/schema on the
 * server. It will use the Schema object that it receives to create
 * the local database.
 * @author Anthony Naddeo
 *
 */
public class SchemaService extends BaseService {
	

	public SchemaService() {
		super("schema");
		DebugLogger.log(this,Level.LOW,"Created schema service");
	}

	
	/**
	 * Get the target server-side api of this service. This may look
	 * something like "schema" or "sync"; the end of the url after
	 * the word "api" in the url: "http://domain.com/api/schema". It is
	 * used in url building.
	 * @return The end of the url for the api section that this service
	 * utilizes. 
	 */
	@Override
	protected String targetApi() {
		return "schema";
	}

	@Override
	protected String getUpdateMessage() {
		return "Downloading the schema information";
	}
	
	/**
	 * Use the schema response from the server to create the local database.
	 * @param schema The Schema object that was returned as JSON from the 
	 * server.
	 */
	private void createDatabase(Schema schema) throws IOException, FileNotFoundException{
		DebugLogger.log(this,Level.LOW,"Creating the database.");
		Database db = Database.getDatabase(this);
		Config config = new Config(this);
		db.create(schema, config.getMetamodel());
	}

	/**
	 * This is where the downloading and synchronization is done. The server
	 * will respond with a Schema object in JSON. This object will be deserialized
	 * with a Serializer and passed to createDatabase();
	 * @param intent Currently not used.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		DebugLogger.log(this,Level.LOW,"Executing the schema service");
		sendStartSync();

		//TODO add error check for codes from server
		
		try {
			sendServiceProgressBroadcast();
			addParameter("db", "sqlite");
			HttpResponse response = execute(getUrl());

			DebugLogger.log(this,Level.LOW,"Just made the HttpResponse");
			DebugLogger.log(this,Level.LOW,"Making the Schema");
			
			String data = Serializer.convertStreamToString(response.getEntity().getContent());
			DebugLogger.log(this,Level.HIGH,"Got this from server: " + data);
			Schema schema = getSerializer().decode(data, Schema.class);
			createDatabase(schema);

			DebugLogger.log(this,Level.LOW,"Finished the schema udpate, starting sync.");
			
//			getAPI().metamodel(this);
			getAPI().sync(this);

		} catch (IOException e) {
			DebugLogger.log(this,Level.LOW,"Encountered a network error, printing stacktrace.");
			DebugLogger.log(this,Level.LOW, e.getMessage());
			e.printStackTrace();
			sendEndSync(true,"");
		}

		
	}


	


	

}
