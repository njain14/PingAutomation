package org.ping.migrate.pf.objects;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.ping.migrate.params.Constants;

public class ClusterManagement {

	static HttpsURLConnection conn;
	static String credentials;
	static String basicAuth;

	public static HttpsURLConnection replicateChanges(String destPFAPIURL) throws IOException, JSONException {
		String replication = destPFAPIURL + Constants.PF_API_ENDPOINT + Constants.PF_CLUSTER_REPLICATION_ENDPOINT;
		System.out.println(replication);
		URL url = new URL(replication);
		conn = (HttpsURLConnection) url.openConnection();
		credentials = Constants.PF_API_USERNAME + ":" + Constants.PF_API_PASSWORD;
		basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", basicAuth);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("X-XSRF-Header", "PingFederate");
		System.out.println(conn.getResponseCode());
		return conn;
	}

}
