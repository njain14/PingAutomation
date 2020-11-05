package org.ping.oidc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class BackChannelAuth {
	static String AuthCode;
	static String tokenEndpoint;

	public static void getTokens(HttpServletRequest request, HttpServletResponse response, String code)
			throws IOException, JSONException, ServletException {
		ReadOidcProperties r = new ReadOidcProperties();
		AuthCode = code;
		String urlParameters = "?grant_type=" + "authorization_code" + "&redirect_uri="
				+ "http://localhost.uhc.com:8080/PingAutomation/Code" + "&code=" + AuthCode;
		tokenEndpoint = DiscoveryDocument.getAuthServerObject().getString("token_endpoint");
		String tokenUrl = tokenEndpoint + urlParameters;
		String authString = r.getPropValues().getProperty("client_id") + ":"
				+ r.getPropValues().getProperty("client_secret");
		URL url = new URL(tokenUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setDoOutput(true);
		String basicAuthEnc = "Basic " + new String(Base64.getEncoder().encode((authString).getBytes()));
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", basicAuthEnc);
		con.setRequestProperty("Content-Type", "application/json");
		con.getOutputStream();
		int responseCode = con.getResponseCode();
			
		if (responseCode == HttpURLConnection.HTTP_OK) {
			JSONObject jsonResponse = parseJson(con);
			System.out.println(jsonResponse.getString("access_token"));
			SessionManager.createSession(response, jsonResponse.getString("access_token"));
			DecodeJwt.testDecodeJWT(jsonResponse.getString("id_token"));
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);

		}
	}

	public static JSONObject parseJson(HttpsURLConnection con) throws IOException, JSONException {

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder response = new StringBuilder();
		String responseLine = null;
		while ((responseLine = br.readLine()) != null) {
			response.append(responseLine.trim());
		}
		br.close();
		JSONObject jsonResponse = new JSONObject(new String(response));
		return jsonResponse;
	}
}