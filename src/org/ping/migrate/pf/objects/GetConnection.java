package org.ping.migrate.pf.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;

public class GetConnection {

	static URL url;
	static String credentials;
	static String basicAuth;

	public static HttpsURLConnection initiateGETWithID(String api_url, String id) throws IOException, JSONException {

		url = new URL(api_url + "/" + id);

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		credentials = Constants.PF_API_USERNAME + ":" + Constants.PF_API_PASSWORD; 
		basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("X-XSRF-Header", "PingFederate");
		con.setDoOutput(true);
		return (con);
	}

	public static JSONObject initiateGETALL(String api_url) throws IOException, JSONException {

		url = new URL(api_url);

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		credentials = Constants.PF_API_USERNAME + ":" + Constants.PF_API_PASSWORD; 
		basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("X-XSRF-Header", "PingAccess");
		con.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JSONObject allJsonObject = new JSONObject(response.toString());
		return (allJsonObject);
	}
	
	public static JSONObject initiateGETByID(String api_url, int i) throws IOException, JSONException {

		url = new URL(api_url+"/"+i);

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		credentials = Constants.PF_API_USERNAME + ":" + Constants.PF_API_PASSWORD;
		basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("X-XSRF-Header", "PingAccess");
		con.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JSONObject allJsonObject = new JSONObject(response.toString());
		return (allJsonObject);
	}
}
