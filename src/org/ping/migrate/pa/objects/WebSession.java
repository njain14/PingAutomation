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
import org.ping.migrate.pf.objects.GetConnection;
import org.ping.migrate.pf.objects.PostConnection;

public class WebSession {

	static JSONObject webSession;
	static HttpsURLConnection conn;
	static int newWebSessionId;

	public static int getWebSession(int webSessionid) throws IOException, JSONException {
		System.out.println("Inside WebSession" + webSessionid);
		String webSessionURL = Environment.getPingAccEnvironment(Params.getSourceenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_WEBSESSION_ENDPOINT;
		webSession = GetConnection.initiateGETByID(webSessionURL, webSessionid);
		System.out.println(webSession);
		webSession.remove("id");
		webSession.put("name", Params.getDestinationObjectName());
		webSession.put("audience", Params.getWsAud());
		webSession.getJSONObject("clientCredentials").put("clientId", Params.getWsClientid());
		webSession.getJSONObject("clientCredentials").getJSONObject("clientSecret").remove("encryptedValue");
		webSession.getJSONObject("clientCredentials").getJSONObject("clientSecret").put("value", "123456789");
		System.out.println(webSession);
		return postWebSession();
	}

	public static int postWebSession() throws IOException, JSONException {

		String url = Environment.getPingAccEnvironment(Params.getDestinationenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_WEBSESSION_ENDPOINT;
		conn = PostConnection.initiatePOSTALL(url);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(webSession.toString());
		wr.flush();
		StringBuffer webBuffer = ConnectionStream.getConnectionStream(conn);
		JSONObject jsonResponse = new JSONObject(webBuffer.toString());
		System.out.println(jsonResponse);
		return jsonResponse.getInt("id");
	}
}
