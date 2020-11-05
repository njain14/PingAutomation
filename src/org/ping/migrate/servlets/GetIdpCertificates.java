package org.ping.migrate.servlets;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Environment;
import org.ping.migrate.pf.objects.ConnectionStream;

@WebServlet("/GetIDPCertificates")
public class GetIdpCertificates extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String env;
	String basicAuth;
	String credentials;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		env = request.getParameter("env");

		String apiurl = Environment.getPingFedEnvironment(env) + "/pf-admin-api/v1" + request.getParameter("path");
		URL url = new URL(apiurl);

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		credentials = Constants.PF_API_USERNAME + ":" + Constants.PF_API_PASSWORD; 
		basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("X-XSRF-Header", "PingFederate");
		con.setDoOutput(true);

		try {
			JSONObject allSigningCerts = new JSONObject(ConnectionStream.getConnectionStream(con).toString());
			response.getWriter().print(allSigningCerts);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
