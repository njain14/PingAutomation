package org.ping.migrate.pa.objects;

import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Environment;
import org.ping.migrate.params.Params;
import org.ping.migrate.pf.objects.ConnectionStream;
import org.ping.migrate.pf.objects.PostConnection;

public class VirtualHost {

	static JSONObject virtualHost;
	static HttpsURLConnection conn;
	static int [] vHost ;
	static String [] vH;

	public static int[] getVirtualHosts() throws IOException, JSONException {
		virtualHost = new JSONObject();
		vH = Params.getVirtualHosts();
		vHost = new int[vH.length];
		int i = 0; 
		for (i = 0; i < vH.length; i++) {
			System.out.println();
			virtualHost.put("host", vH[i]);
			virtualHost.put("port", 443);
			virtualHost.put("agentResourceCacheTTL", 900);
			virtualHost.put("keyPairId", 0);
			virtualHost.put("trustedCertificateGroupId", 0);
			
			vHost[i] = setVirtualHosts();
		}
		return vHost;

	}

	public static int setVirtualHosts() throws IOException, JSONException {

		String url = Environment.getPingAccEnvironment(Params.getDestinationenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_VIRTUALHOST_ENDPOINT;
		conn = PostConnection.initiatePOSTALL(url);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(virtualHost.toString());
		wr.flush();
		StringBuffer webBuffer = ConnectionStream.getConnectionStream(conn);
		JSONObject jsonResponse = new JSONObject(webBuffer.toString());
		System.out.println(jsonResponse);
		return jsonResponse.getInt("id");
	}

}
