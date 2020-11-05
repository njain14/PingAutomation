package org.ping.migrate.pf.objects;

import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.*;

public class OidcPolicy {

	static String accessTokenManager;
	static String dataStoreSysId;
	static HttpsURLConnection con;
	static JSONObject OIDCPolicy;
	static final Logger LOGGER = Logger.getLogger(OidcPolicy.class);

	public static HttpsURLConnection getOIDCPolicyConnection(String sourcePFAPIURL, String sourcePolicy)
			throws IOException, JSONException {

		String getUrl = sourcePFAPIURL + Constants.PF_API_ENDPOINT + Constants.PF_OIDCPOLICY_ENDPOINT;
		con = GetConnection.initiateGETWithID(getUrl, sourcePolicy);
		return con;
	}

	public static HttpsURLConnection createOIDCPolicyConnection(HttpsURLConnection con, String sourcePFAPIURL,
			String destPFAPIURL, String destPolicy) throws JSONException, IOException {

		OIDCPolicy = new JSONObject(ConnectionStream.getConnectionStream(con).toString());
		LOGGER.debug("Original JSON");
		LOGGER.debug(OIDCPolicy);
		OIDCPolicy.put("id", destPolicy);
		OIDCPolicy.put("name", destPolicy);
		accessTokenManager = OIDCPolicy.getJSONObject("accessTokenManagerRef").getString("id");
		OIDCPolicy.getJSONObject("accessTokenManagerRef").put("location", destPFAPIURL + Constants.PF_API_ENDPOINT
				+ Constants.PF_ACCESSTOKEN_MANAGER_LOCATION + "/" + accessTokenManager);

		JSONArray attrSourceArray = OIDCPolicy.getJSONObject("attributeMapping").getJSONArray("attributeSources");

		for (int i = 0; i < attrSourceArray.length(); i++) {

			JSONObject attrSource = attrSourceArray.getJSONObject(i);
			dataStoreSysId = attrSource.getJSONObject("dataStoreRef").getString("id");
			String dataStoreID = LdapDataStore.getLdapDataStore(sourcePFAPIURL, destPFAPIURL, dataStoreSysId);

			if (dataStoreID != null) {

				attrSource.getJSONObject("dataStoreRef").put("id", dataStoreID);
				attrSource.getJSONObject("dataStoreRef").put("location",
						destPFAPIURL + Constants.PF_API_ENDPOINT + Constants.PF_DATASTORE_LOCATION + "/" + dataStoreID);
			}
		}

		LOGGER.debug("Updated OIDC Policy JSON");
		LOGGER.debug(OIDCPolicy);

		String oidcPostUrl = destPFAPIURL + Constants.PF_API_ENDPOINT + Constants.PF_OIDCPOLICY_ENDPOINT;

		con = PostConnection.initiatePOSTALL(oidcPostUrl);
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(OIDCPolicy.toString());
		wr.flush();
		return con;
	}
}
