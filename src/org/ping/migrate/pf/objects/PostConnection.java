package org.ping.migrate.pf.objects;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;

import org.ping.migrate.params.Constants;

public class PostConnection {
	
	static URL url;
	static String credentials;
	static String basicAuth;
	
	public static HttpsURLConnection initiatePOSTALL(String api_url) throws IOException {

		url = new URL(api_url);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		credentials = Constants.PF_API_USERNAME + ":" + Constants.PF_API_PASSWORD; 
		basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("X-XSRF-Header", "PingAccess");
		con.setDoOutput(true);
		return (con);	
	}
	
}
