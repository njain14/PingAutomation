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

public class IdentityMapping {

	static JSONObject identityMapping;
	static HttpsURLConnection conn;

	public static int getIdentityMapping(int identityMappingId) throws IOException, JSONException {
		String identyMappingURL = Environment.getPingAccEnvironment(Params.getSourceenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_IDENTITYMAPPING_ENDPOINT;
		identityMapping = GetConnection.initiateGETByID(identyMappingURL, identityMappingId);
		identityMapping.remove("id");
		identityMapping.put("name", Params.getDestinationObjectName());
		return postIdentityMapping();
	}

	public static int postIdentityMapping() throws IOException, JSONException {

		String url = Environment.getPingAccEnvironment(Params.getDestinationenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_IDENTITYMAPPING_ENDPOINT;
		conn = PostConnection.initiatePOSTALL(url);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(identityMapping.toString());
		wr.flush();
		StringBuffer webBuffer = ConnectionStream.getConnectionStream(conn);
		JSONObject jsonResponse = new JSONObject(webBuffer.toString());
		System.out.println(jsonResponse);
		return jsonResponse.getInt("id");
	}

}
