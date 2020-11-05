package org.ping.migrate.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.ping.migrate.pa.objects.Application;
import org.ping.migrate.params.Params;

@WebServlet("/PAObjects")
public class PAObjects extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String sourceenv;
	private String destinationenv;
	private String sourceApplication;
	private String[] virtualHosts;
	private String[] sites;
	private String destinationObjectName;
	private String region;
	private HttpsURLConnection conn;
	private String wsClientid;
	private String wsAud; 
	static final Logger LOGGER = Logger.getLogger(PAObjects.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		sourceenv = request.getParameter("sourceenv");
		destinationenv = request.getParameter("destenv");
		sourceApplication = request.getParameter("sourceapp");
		virtualHosts = request.getParameterValues("virtualhosts[]");
		region = request.getParameter("region");
		destinationObjectName = request.getParameter("destobjname");
		sites = request.getParameterValues("site[]");
		wsClientid = request.getParameter("clientid");
		wsAud = request.getParameter("aud");
		
		Params.setDestinationenv(destinationenv);
		Params.setSourceenv(sourceenv);
		Params.setSourceApplication(sourceApplication);
		Params.setVirtualHosts(virtualHosts);
		Params.setSites(sites);
		Params.setDestinationObjectName(destinationObjectName);
		Params.setRegion(region);
		Params.setWsAud(wsAud);
		Params.setWsClientid(wsClientid);
			
		try {
			LOGGER.info("Inside Try");
			Application.getApplications();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public StringBuffer errorStream() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		br.close();

		return response;

	}

}
