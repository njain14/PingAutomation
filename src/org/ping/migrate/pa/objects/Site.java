package org.ping.migrate.pa.objects;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Environment;
import org.ping.migrate.params.Params;
import org.ping.migrate.pf.objects.ConnectionStream;
import org.ping.migrate.pf.objects.GetConnection;
import org.ping.migrate.pf.objects.PostConnection;

public class Site {

	static JSONObject site;
	static HttpsURLConnection conn;

	public static int getSite(int siteid) throws IOException, JSONException {
		String siteURL = Environment.getPingAccEnvironment(Params.getSourceenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_SITE_ENDPOINT;
		site = GetConnection.initiateGETByID(siteURL, siteid);
		site.remove("id");
		site.put("name", Params.getDestinationObjectName());
		List<String> list = Arrays.asList(Params.getSites());
		site.put("targets", list);
		System.out.println("Updated Site");
		System.out.println(site);
		return postSite();
	}

	public static int postSite() throws IOException, JSONException {

		String url = Environment.getPingAccEnvironment(Params.getDestinationenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_SITE_ENDPOINT;
		conn = PostConnection.initiatePOSTALL(url);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(site.toString());
		wr.flush();
		StringBuffer webBuffer = ConnectionStream.getConnectionStream(conn);
		JSONObject jsonResponse = new JSONObject(webBuffer.toString());
		return jsonResponse.getInt("id");
	}

}
