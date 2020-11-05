package org.ping.migrate.pf.objects;

import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Environment;
import org.ping.migrate.params.Params;

public class SpConnection {
	static JSONObject spConnectionObj;
	static JSONObject spConnection;
	static JSONObject acsEndpoint;
	static JSONObject attributeSources;
	static JSONObject adapterMappings;
	static String dataStoreRef;
	static String idpAdapterRefloc;
	static String signingKeyPairID;
	static String signingKeyPairLoc;
	static JSONArray encryptCert;
	static HttpsURLConnection conn;
	static final Logger LOGGER = Logger.getLogger(SpConnection.class);

	public static HttpsURLConnection setSPConnection(String isEncryption) throws IOException, JSONException {
		String Url = Environment.getPingFedEnvironment(Params.getSourceenv()) + Constants.PF_API_ENDPOINT
				+ Constants.PF_SP_CONNECTION_ENDPOINT + "?entityId=" + Params.getSourcespid();
		spConnectionObj = GetConnection.initiateGETALL(Url);
		spConnection = spConnectionObj.getJSONArray("items").getJSONObject(0);
		System.out.println("Original SP Connection JSON");
		System.out.println(spConnection);

		spConnection.remove("id");
		spConnection.remove("name");
		spConnection.put("name", Params.getDestinationspconn());
		spConnection.remove("entityId");
		spConnection.put("entityId", Params.getDestspid());
		String[] acsArray = Params.getDestacs();

		for (int i = 0; i < spConnection.getJSONObject("spBrowserSso").getJSONArray("ssoServiceEndpoints")
				.length(); i++) {
			acsEndpoint = spConnection.getJSONObject("spBrowserSso").getJSONArray("ssoServiceEndpoints")
					.getJSONObject(i);
			acsEndpoint.remove("url");
			acsEndpoint.put("url", acsArray[i]);
		}

		for (int j = 0; j < spConnection.getJSONObject("spBrowserSso").getJSONArray("adapterMappings").length(); j++) {
			adapterMappings = spConnection.getJSONObject("spBrowserSso").getJSONArray("adapterMappings")
					.getJSONObject(j);
			for (int k = 0; k < adapterMappings.getJSONArray("attributeSources").length(); k++) {
				attributeSources = adapterMappings.getJSONArray("attributeSources").getJSONObject(k);
				dataStoreRef = attributeSources.getJSONObject("dataStoreRef").getString("id");
				String dataStoreID = LdapDataStore.getLdapDataStore(
						Environment.getPingFedEnvironment(Params.getSourceenv()),
						Environment.getPingFedEnvironment(Params.getDestinationenv()), dataStoreRef);
				if (dataStoreID != null) {

					attributeSources.getJSONObject("dataStoreRef").put("id", dataStoreID);
					attributeSources.getJSONObject("dataStoreRef").put("location",
							Environment.getPingFedEnvironment(Params.getDestinationenv()) + Constants.PF_API_ENDPOINT
									+ Constants.PF_DATASTORE_LOCATION + "/" + dataStoreID);
				}

				idpAdapterRefloc = Environment.getPingFedEnvironment(Params.getDestinationenv())
						+ Constants.PF_API_ENDPOINT + Constants.PF_IDP_ADAPTER + "/"
						+ adapterMappings.getJSONObject("idpAdapterRef").getString("id");

				adapterMappings.getJSONObject("idpAdapterRef").put("location", idpAdapterRefloc);
			}
		}

		signingKeyPairID = Params.getSignCertID();
		signingKeyPairLoc = Environment.getPingFedEnvironment(Params.getDestinationenv()) + Constants.PF_API_ENDPOINT
				+ Constants.PF_KEYPAIR_SIGNING + "/" + signingKeyPairID;
		spConnection.getJSONObject("credentials").getJSONObject("signingSettings").getJSONObject("signingKeyPairRef")
				.put("id", signingKeyPairID);
		spConnection.getJSONObject("credentials").getJSONObject("signingSettings").getJSONObject("signingKeyPairRef")
				.put("location", signingKeyPairLoc);

		if (isEncryption.equalsIgnoreCase("Yes")) {

			encryptCert = spConnection.getJSONObject("credentials").getJSONArray("certs");

			if (encryptCert.length() > 0) {
				for (int i = 0; i < encryptCert.length(); i++) {
					spConnection.getJSONObject("credentials").getJSONArray("certs").remove(i);
				}
			}
			encryptCert.put(Certificate.getSPCertificate(Params.getCertFile()));
			spConnection.getJSONObject("credentials").put("blockEncryptionAlgorithm", "AES_128");
			spConnection.getJSONObject("credentials").put("keyTransportAlgorithm", "RSA_OAEP");
			spConnection.getJSONObject("spBrowserSso").getJSONObject("encryptionPolicy").put("encryptAssertion", true);
		}

		else if (isEncryption.equalsIgnoreCase("No")) {
			if (spConnection.getJSONObject("credentials").getJSONArray("certs").length() > 0) {
				spConnection.getJSONObject("credentials").getJSONArray("certs").remove(0);
				spConnection.getJSONObject("credentials").remove("blockEncryptionAlgorithm");
				spConnection.getJSONObject("credentials").remove("keyTransportAlgorithm");
				spConnection.getJSONObject("spBrowserSso").getJSONObject("encryptionPolicy").put("encryptAssertion",
						false);
			}
		}

		else {
			for (int i = 0; i < spConnection.getJSONObject("credentials").getJSONArray("certs").length(); i++) {
				spConnection.getJSONObject("credentials").getJSONArray("certs").getJSONObject(i)
						.getJSONObject("certView").remove("id");
			}
		}
		System.out.println("Updated SP connection");
		System.out.println(spConnection);

		String spConnectionPostUrl = Environment.getPingFedEnvironment(Params.getDestinationenv())
				+ Constants.PF_API_ENDPOINT + Constants.PF_SP_CONNECTION_ENDPOINT;

		conn = PostConnection.initiatePOSTALL(spConnectionPostUrl);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(spConnection.toString());
		wr.flush();
		System.out.println(conn.getResponseCode());
		return conn;
	}

}
