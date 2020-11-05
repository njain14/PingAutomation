package org.ping.migrate.pf.objects;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Params;

public class OidcClient {

	static JSONObject oidcClient;
	static String accessTokenManager;
	static HttpsURLConnection con;
	static final Logger LOGGER = Logger.getLogger(OidcClient.class);

	public static HttpsURLConnection getOIDCClientConnection(String sourcePFAPIURL, String sourceClient)
			throws IOException, JSONException {
		String getUrl = sourcePFAPIURL + Constants.PF_API_ENDPOINT + Constants.PF_OIDC_CLIENT_ENDPOINT;
		LOGGER.debug(getUrl);
		con = GetConnection.initiateGETWithID(getUrl, sourceClient);
		return con;

	}

	public static HttpsURLConnection createOIDCClient(HttpsURLConnection con, String sourceClient, String destClient,
			String destPFAPIURL) throws JSONException, IOException {

		oidcClient = new JSONObject(ConnectionStream.getConnectionStream(con).toString());
		LOGGER.debug("Original OIDC Client JSON" + " - " + sourceClient);
		LOGGER.debug(oidcClient);

		oidcClient.getJSONObject("oidcPolicy").remove("pairwiseIdentifierUserType");
		oidcClient.remove("requireProofKeyForCodeExchange");
		oidcClient.put("clientId", destClient);
		oidcClient.put("name", destClient);
		oidcClient.getJSONObject("clientAuth").put("secret", Params.getClientSecret());
		accessTokenManager = oidcClient.getJSONObject("defaultAccessTokenManagerRef").getString("id");
		oidcClient.getJSONObject("defaultAccessTokenManagerRef").put("location", destPFAPIURL
				+ Constants.PF_API_ENDPOINT + Constants.PF_ACCESSTOKEN_MANAGER_LOCATION + "/" + accessTokenManager);
		oidcClient.getJSONObject("oidcPolicy").getJSONObject("policyGroup").put("id",
				Params.getDestinationoidcPolicy());
		oidcClient.getJSONObject("oidcPolicy").getJSONObject("policyGroup").put("location",
				destPFAPIURL + Constants.PF_API_ENDPOINT + Constants.PF_OIDCPOLICY_ENDPOINT + "/"
						+ Params.getDestinationoidcPolicy());
		if (Params.getRedirectURL() != null) {
			for (int i = 0; i < oidcClient.getJSONArray("redirectUris").length(); i++) {
				oidcClient.getJSONArray("redirectUris").remove(i);
			}			
			
			for (int j = 0; j < Params.getRedirectURL().length; j++) {
				List<String> list = Arrays.asList(Params.getRedirectURL());
				oidcClient.put("redirectUris", list);
			}
		}

		LOGGER.debug("Updated OIDC Client JSON");
		LOGGER.debug(oidcClient);

		String oidcPostUrl = destPFAPIURL + Constants.PF_API_ENDPOINT + Constants.PF_OIDC_CLIENT_ENDPOINT;

		con = PostConnection.initiatePOSTALL(oidcPostUrl);
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(oidcClient.toString());
		wr.flush();
		return con;
	}
}
