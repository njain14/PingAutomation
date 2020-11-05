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

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Environment;
import org.ping.migrate.pf.objects.ConnectionStream;

@WebServlet("/GetClients")
public class GetClients extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String env;
	String basicAuth;
	String credentials;
	static final Logger LOGGER = Logger.getLogger(GetClients.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		env = request.getParameter("env");
		
		String apiurl = Environment.getPingFedEnvironment(env) + "/pf-admin-api/v1" + request.getParameter("path");
		LOGGER.debug("API URL - " + apiurl);
		URL url = new URL(apiurl);

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		credentials = Constants.PF_API_USERNAME + ":" + Constants.PF_API_PASSWORD; 
		basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("X-XSRF-Header", "PingAccess");
		con.setDoOutput(true);
		
		try {
			JSONObject allClients = new JSONObject(ConnectionStream.getConnectionStream(con).toString());
			LOGGER.debug("ALL OIDC Clients - " + allClients);
            response.getWriter().print(allClients);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
