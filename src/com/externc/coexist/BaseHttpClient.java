package com.externc.coexist;

import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.content.Context;

/**
 * This class allows us to use self signed SSL keys in Android
 * @author Anthony Naddeo
 *
 */
public class BaseHttpClient extends DefaultHttpClient{

	Context context;
	
	public BaseHttpClient(Context context) {
		this.context = context;
		
	}
	
	/**
	 * This class will be used to validate host names for SSL
	 */
	final public static X509HostnameVerifier DO_NOT_VERIFY = new X509HostnameVerifier() {

		public boolean verify(String host, SSLSession session) {
			// TODO Auto-generated method stub
			return false;
		}

		public void verify(String host, SSLSocket ssl) throws IOException {
			// TODO Auto-generated method stub
			
		}

		public void verify(String host, X509Certificate cert)
				throws SSLException {
			// TODO Auto-generated method stub
			
		}

		public void verify(String host, String[] cns, String[] subjectAlts)
				throws SSLException {
			// TODO Auto-generated method stub
			
		}
		
	};
  
	@Override 
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(
            new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(
        		new Scheme("https", createSslSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), registry);
      }
	
	
	protected SSLSocketFactory createSslSocketFactory() {
	    try {
	        final KeyStore trustedKeys = KeyStore.getInstance("BKS");

//	        final InputStream in = context.getResources().openRawResource( R.raw.keystore);  
//	        try {
//	            trustedKeys.load(in, context.getString( R.string.keystore_password ).toCharArray());
//	        } finally {
//	            in.close();
//	        }

	        SSLSocketFactory ssl = new SSLSocketFactory(trustedKeys);
	        ssl.setHostnameVerifier(DO_NOT_VERIFY);
	        return ssl;

	    } catch( Exception e ) {
	        throw new RuntimeException(e);
	    }
	}

}
