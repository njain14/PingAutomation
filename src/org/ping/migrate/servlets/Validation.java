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
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Environment;

@WebServlet("/Validation")
public class Validation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String env;
	String spid;
	String basicAuth;
	String credentials;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		env = request.getParameter("env");
		spid = request.getParameter("spid");
		
		String apiurl = Environment.getPingFedEnvironment(env) + "/pf-admin-api/v1" + request.getParameter("path") + "?entityId=" + spid;
		URL url = new URL(apiurl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		credentials = Constants.PF_API_USERNAME + ":" + Constants.PF_API_PASSWORD; 
		basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", basicAuth);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("X-XSRF-Header", "PingFederate");
		con.setDoOutput(true);

		if(con.getResponseCode() == 200) {
			response.getWriter().print("Success");
		}
		else {
			response.getWriter().print("Fail");
		}		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
