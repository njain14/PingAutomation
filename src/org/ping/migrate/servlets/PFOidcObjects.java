package org.ping.migrate.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.ping.migrate.params.*;
import org.ping.migrate.pf.objects.ClusterManagement;
import org.ping.migrate.pf.objects.OidcClient;
import org.ping.migrate.pf.objects.OidcPolicy;

@WebServlet("/PFOidcObjects")
public class PFOidcObjects extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = Logger.getLogger(PFOidcObjects.class);
	private String sourceenv;
	private String destinationenv;
	private String sourceclientid;
	private String destinationclientid;
	private String sourceoidcPolicy;
	private String destinationoidcPolicy;
	private String clientSecret;
	private String[] redirectUrl;
	private HttpsURLConnection con;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		sourceenv = request.getParameter("sourceenv");
		destinationenv = request.getParameter("destenv");
		sourceclientid = request.getParameter("sourceclientid");
		destinationclientid = request.getParameter("destclientid");
		sourceoidcPolicy = request.getParameter("sourceoidcPolicy");
		destinationoidcPolicy = request.getParameter("destoidcPolicy");
		redirectUrl = request.getParameterValues("redirecturl[]");
		clientSecret = request.getParameter("clientsecret");

		Params.setDestinationclientid(destinationclientid);
		Params.setDestinationenv(destinationenv);
		Params.setDestinationoidcPolicy(destinationoidcPolicy);
		Params.setSourceclientid(sourceclientid);
		Params.setSourceenv(sourceenv);
		Params.setSourceoidcPolicy(sourceoidcPolicy);
		Params.setRedirectURL(redirectUrl);
		Params.setClientSecret(clientSecret);
		
		try {
			LOGGER.debug("Inside try");
			con = OidcPolicy.getOIDCPolicyConnection(Environment.getPingFedEnvironment(sourceenv), sourceoidcPolicy);
			if (con.getResponseCode() == 200) {
				con = OidcPolicy.createOIDCPolicyConnection(con, Environment.getPingFedEnvironment(sourceenv),
						Environment.getPingFedEnvironment(destinationenv), destinationoidcPolicy);

				if (con.getResponseCode() == 201) {
					LOGGER.info(Constants.PF_OIDC_POLICY_SUCCESS_RESULT + "-" + destinationoidcPolicy);

					con = OidcClient.getOIDCClientConnection(Environment.getPingFedEnvironment(sourceenv),
							sourceclientid);
					if (con.getResponseCode() == 200) {
						con = OidcClient.createOIDCClient(con, sourceclientid, destinationclientid,
								Environment.getPingFedEnvironment(destinationenv));
						if (con.getResponseCode() == 201) {
							ClusterManagement.replicateChanges(Environment.getPingFedEnvironment(destinationenv));
							LOGGER.info(Constants.PF_OIDC_CLIENT_SUCCESS_RESULT + "-" + destinationclientid);
							request.setAttribute("policyresult",
									Constants.PF_OIDC_POLICY_SUCCESS_RESULT + "-" + destinationoidcPolicy);
							request.setAttribute("clientresult",
									Constants.PF_OIDC_CLIENT_SUCCESS_RESULT + "-" + destinationclientid);

						} else {
							LOGGER.error(Constants.PF_OIDC_CLIENT_FAIL_RESULT + "-" + destinationclientid);
							request.setAttribute("clientresult",
									Constants.PF_OIDC_CLIENT_FAIL_RESULT + "-" + destinationclientid);
						}
					}
				}

				else {
					LOGGER.error(Constants.PF_OIDC_POLICY_FAIL_RESULT + "-" + destinationoidcPolicy);
					request.setAttribute("policyresult",
							Constants.PF_OIDC_POLICY_FAIL_RESULT + "-" + destinationoidcPolicy);
				}
			}

		} catch (IOException | JSONException e) {
			throw new ServletException(e);
		}

		RequestDispatcher rd = request.getRequestDispatcher("resultOidc.jsp");
		rd.forward(request, response);
	}

	public StringBuffer errorStream() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		br.close();

		return response;

	}
}
