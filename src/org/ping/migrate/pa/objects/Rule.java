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

public class Rule {

	static JSONObject sourceEnvRule;
	static JSONObject destEnvRule;
	static HttpsURLConnection conn;

	public static int getRule(int ruleId) throws IOException, JSONException {

		String ruleURL = Environment.getPingAccEnvironment(Params.getSourceenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_RULE_ENDPOINT;
		sourceEnvRule = GetConnection.initiateGETByID(ruleURL, ruleId);

		String url = Environment.getPingAccEnvironment(Params.getDestinationenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_RULE_ENDPOINT + "?name=" + sourceEnvRule.getString("name");
		destEnvRule = GetConnection.initiateGETALL(url);
		return destEnvRule.getInt("id");
	}

	public static int postRule(int ruleId) throws IOException, JSONException {

		String ruleURL = Environment.getPingAccEnvironment(Params.getSourceenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_RULE_ENDPOINT;
		sourceEnvRule = GetConnection.initiateGETByID(ruleURL, ruleId);
		sourceEnvRule.remove("id");
		String url = Environment.getPingAccEnvironment(Params.getDestinationenv(), Params.getRegion())
				+ Constants.PA_API_ENDPOINT + Constants.PA_RULE_ENDPOINT;
		conn = PostConnection.initiatePOSTALL(url);
		
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(sourceEnvRule.toString());
		wr.flush();
		StringBuffer webBuffer = ConnectionStream.getConnectionStream(conn);
		JSONObject jsonResponse = new JSONObject(webBuffer.toString());
		System.out.println(jsonResponse);
		return jsonResponse.getInt("id");
	}

}
