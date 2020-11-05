package org.ping.migrate.pf.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.*;

public class LdapDataStore {

	public static HttpsURLConnection con;
	public static JSONObject dataStoreObjectID;
	public static String dataStoreID;
	public static String dataStorebyID;
	static final Logger LOGGER = Logger.getLogger(LdapDataStore.class);

	public static String getDestinationLdapDataStore(String pfAPIUrl, String dataStoreNanme)
			throws IOException, JSONException {
		String dataStoreUrl = pfAPIUrl + Constants.PF_API_ENDPOINT + Constants.PF_DATASTORE_LOCATION;
		LOGGER.debug(dataStoreUrl);
		JSONObject dataStoreObjectAll = GetConnection.initiateGETALL(dataStoreUrl);
		for (int i = 1; i < dataStoreObjectAll.getJSONArray("items").length(); i++) {
			dataStoreObjectID = dataStoreObjectAll.getJSONArray("items").getJSONObject(i);

			if (dataStoreObjectID.get("name").equals(dataStoreNanme) && dataStoreObjectID.get("type").equals("LDAP")) {
				dataStoreID = dataStoreObjectID.getString("id");
			}
		}
		return dataStoreID;
	}

	public static String getLdapDataStore(String sourcePfAPIUrl, String destPfAPIUrl, String id)
			throws IOException, JSONException {
		String dataStoreUrl = sourcePfAPIUrl + Constants.PF_API_ENDPOINT + Constants.PF_DATASTORE_LOCATION;
		con = GetConnection.initiateGETWithID(dataStoreUrl, id);
		JSONObject dataStoreObject = new JSONObject(inputStream(con).toString());
		String dsID = getDestinationLdapDataStore(destPfAPIUrl, dataStoreObject.getString("name"));
		if (dsID == null) {
			LOGGER.error("No DataSource found in Destination PF with name - " + dataStoreObject.getString("name"));
		}
		return dsID;
	}

	public static StringBuffer inputStream(HttpsURLConnection con) throws IOException {

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = inputReader.readLine()) != null) {
			response.append(inputLine);
		}
		inputReader.close();
		return response;

	}
}
