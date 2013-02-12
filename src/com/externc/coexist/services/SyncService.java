package com.externc.coexist.services;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.externc.coexist.Config;
import com.externc.coexist.Database;
import com.externc.coexist.DebugLogger;
import com.externc.coexist.MainActivity;
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
	protected void onHandleIntent(Intent intent) {
		String message = "Syncing the database.";
		sendStartSync(message);
		
		Config conf = new Config(this);
		Database db = new Database(this);
		List<String> tables = db.getTables();
		
		addParameter("version", conf.getVersion());
		addParameter("tables", tables);
		addParameter("mod_ts", db.getModts(tables));
		
		String url = getUrl();
		DebugLogger.log(this, Level.LOW, "Generaetd sync url: " + url);
		
		try {
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
				
				
				LocalBroadcastManager.getInstance(this).sendBroadcastSync(new Intent(MainActivity.getFinishedSyncAction(this)));
			}
			
		} catch (HttpHostConnectException e) {
			DebugLogger.log(this, Level.LOW, e.getMessage());
			e.printStackTrace();
			sendEndSync(true,"Could not connect to the server.");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}catch (UnknownHostException e){
			e.printStackTrace();
			sendEndSync(true, "Could not reach host.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	


}
