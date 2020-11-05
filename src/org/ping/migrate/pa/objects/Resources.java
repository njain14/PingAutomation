package org.ping.migrate.pa.objects;

import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Environment;
import org.ping.migrate.params.Params;
import org.ping.migrate.pf.objects.ConnectionStream;
import org.ping.migrate.pf.objects.GetConnection;
import org.ping.migrate.pf.objects.PostConnection;

public class Resources {

	static JSONObject appResources;
	static JSONObject resources;
	static JSONArray appResourcesArray;
	static JSONArray updatedAppResourcesArray;
	static JSONArray policyArray;
	static JSONObject rules;
	static HttpsURLConnection conn;
	static int ruleID;

	public static void getApplicationResource(int ApplicationId, int newApplicationId)
			throws IOException, JSONException {
		String resourceURL = Environment.getPingAccEnvironment(Params.getSourceenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_APPLICATION_ENDPOINT + "/" + ApplicationId
				+ Constants.PA_RESOURCE_ENDPOINT;
		appResources = GetConnection.initiateGETALL(resourceURL);
		appResourcesArray = appResources.getJSONArray("items");
		for (int i = 0; i < appResourcesArray.length(); i++) {
			resources = appResourcesArray.getJSONObject(i);
			resources.remove("id");
			resources.put("applicationId", newApplicationId);

			if (resources.getJSONObject("policy").getJSONArray("Web").isNull(0)) {
				policyArray = resources.getJSONObject("policy").getJSONArray("Web");

				for (int j = 0; j < policyArray.length(); j++) {
					policyArray.remove(j);
				}
				System.out.println(resources);
				postApplicationResource(newApplicationId);
			}

		}
	}

	public static void postApplicationResource(int ApplicationId) throws IOException, JSONException {

		String resourceurl = Environment.getPingAccEnvironment(Params.getDestinationenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_APPLICATION_ENDPOINT + "/" + ApplicationId
				+ Constants.PA_RESOURCE_ENDPOINT;
		System.out.println(resourceurl);
		conn = PostConnection.initiatePOSTALL(resourceurl);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(resources.toString());
		wr.flush();
		StringBuffer webBuffer = ConnectionStream.getConnectionStream(conn);
		JSONObject jsonResponse = new JSONObject(webBuffer.toString());
		System.out.println(jsonResponse);
	}

}
