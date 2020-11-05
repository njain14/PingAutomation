package org.ping.oidc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class DiscoveryDocument {
	public static JSONObject discoveryJson;

	public static JSONObject getAuthServerObject() throws IOException {
		URI issuerURI = null;
		try {
			issuerURI = new URI("https://authgateway1-dev.entiam.uhg.com");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		URL providerConfigurationURL = issuerURI.resolve("/.well-known/openid-configuration").toURL();
		try {
			discoveryJson = new JSONObject(convertStreamToString(providerConfigurationURL.openStream()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return discoveryJson;
	}

	public static String convertStreamToString(java.io.InputStream is) {
		@SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
