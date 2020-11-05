package org.ping.migrate.pa.objects;

import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Params;
import org.ping.migrate.pf.objects.ConnectionStream;
import org.ping.migrate.pf.objects.GetConnection;
import org.ping.migrate.pf.objects.PostConnection;
import org.ping.migrate.params.Environment;

public class Application {

	static JSONObject getApplication;
	static JSONArray applicationsArray;
	static JSONObject application;
	static int applicationID;
	static int siteID;
	static int webSessionID;
	static int identityMappingID;
	static int newApplicationID;
	static int newWebessionID;
	static int newsiteID;
	static int newidentityMappingID;
	static int newvirtualHosts[];
	static HttpsURLConnection conn;

	public static void getApplications() throws IOException, JSONException {

		String applicationURL = Environment.getPingAccEnvironment(Params.getSourceenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_APPLICATION_ENDPOINT + "?name="
				+ Params.getSourceApplication();
		getApplication = GetConnection.initiateGETALL(applicationURL);
		applicationsArray = getApplication.getJSONArray("items");

		for (int i = 0; i < applicationsArray.length(); i++) {
			application = applicationsArray.getJSONObject(i);
			System.out.println("Original Application JSON");
			System.out.println(application);

			webSessionID = application.getInt("webSessionId");
			siteID = application.getInt("siteId");
			identityMappingID = application.getJSONObject("identityMappingIds").getInt("Web");
			applicationID = application.getInt("id");

			newWebessionID = WebSession.getWebSession(webSessionID);
			newsiteID = Site.getSite(siteID);
			newidentityMappingID = IdentityMapping.getIdentityMapping(identityMappingID);
			newvirtualHosts = VirtualHost.getVirtualHosts();

			application.remove("id");
			application.put("name", Params.getDestinationObjectName());
			application.put("webSessionId", newWebessionID);
			application.put("siteId", newsiteID);
			application.getJSONObject("identityMappingIds").put("Web", newidentityMappingID);
			application.put("virtualHostIds", newvirtualHosts);

			if (application.getJSONObject("policy").getJSONArray("Web").isNull(0)) {
				JSONArray policyArray;
				policyArray = application.getJSONObject("policy").getJSONArray("Web");

				for (int j = 0; j < policyArray.length(); j++) {
					policyArray.remove(j);
				}
			}

			System.out.println("Updated Application JSON");
			System.out.println(application);
			newApplicationID = postApplication();
			Resources.getApplicationResource(applicationID, newApplicationID);
		}
	}

	public static int postApplication() throws IOException, JSONException {

		String url = Environment.getPingAccEnvironment(Params.getDestinationenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_APPLICATION_ENDPOINT;
		conn = PostConnection.initiatePOSTALL(url);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(application.toString());
		wr.flush();
		StringBuffer webBuffer = ConnectionStream.getConnectionStream(conn);
		JSONObject jsonResponse = new JSONObject(webBuffer.toString());
		return jsonResponse.getInt("id");

	}

}
