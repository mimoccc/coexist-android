package com.externc.coexist.services;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.content.Intent;

import com.externc.coexist.Config;
import com.externc.coexist.Database;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.DebugLogger.Level;
import com.externc.coexist.api.Sync;

public class SyncService extends BaseService{

	
	public SyncService() {
		super("sync");
		DebugLogger.log(this, Level.LOW,"Created sync service");
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
		return "sync";
	}
	
	@Override
	protected String getUpdateMessage() {
		return "Syncing the database.";
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		sendStartSync();
		
		Config conf = new Config(this);
		Database db = Database.getDatabase(this);
		List<String> tables = db.getTables();
		
		addParameter("version", conf.getVersion());
		addParameter("tables", tables);
		addParameter("mod_ts", db.getModts(tables));
		
		String url = getUrl();
		DebugLogger.log(this, Level.LOW, "Generaetd sync url: " + url);
		
		try {
			sendServiceProgressBroadcast();
			DebugLogger.log(this, Level.LOW, "Starting the request.");
			HttpResponse response = execute(url);
			Sync sync = getSerializer().decode(response.getEntity().getContent(), Sync.class);
			
			
			if(sync.getStatus() != 200){
				DebugLogger.log(this, Level.HIGH, sync.getStatus() +": "+ sync.getMessage());
				DebugLogger.log(this, Level.LOW, "Tried to sync, but an update is required.");
				getAPI().schema(this);
			}else{
				DebugLogger.log(this, Level.LOW, "Got the sync, passing it to the database.");
				db.sync(sync);
				sendEndSync(false,"Updated "+sync.numRows()+" rows");
				
				//broadcast that progess has been made.
				sendFinishedSyncBroadcast(true);
			}
			
		} catch (HttpHostConnectException e) {
			sendFinishedSyncBroadcast(false);
			DebugLogger.log(this, Level.LOW, e.getMessage());
			e.printStackTrace();
			sendEndSync(true,"Could not connect to the server.");
		} catch (ClientProtocolException e) {
			sendFinishedSyncBroadcast(false);
			e.printStackTrace();
		} catch (UnknownHostException e){
			sendFinishedSyncBroadcast(false);
			e.printStackTrace();
			sendEndSync(true, "Could not reach host.");
		} catch (IOException e) {
			sendFinishedSyncBroadcast(false);
			e.printStackTrace();
		}
		
	}

	
	


}
