package org.ping.migrate.servlets;

import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.ping.migrate.params.Constants;
import org.ping.migrate.params.Environment;
import org.ping.migrate.params.Params;
import org.ping.migrate.pf.objects.ClusterManagement;
import org.ping.migrate.pf.objects.SpConnection;

@WebServlet("/PFSamlObjects")
@MultipartConfig
public class PFSamlObjects extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String sourceenv;
	private String destinationenv;
	private String sourcespid;
	private String destinationspconn;
	private String destinationspid;
	private String destinationacs[];
	private String signingCerts;
	private String isEncryption;
	private Part certFile;
	private HttpsURLConnection conn;
	static final Logger LOGGER = Logger.getLogger(PFSamlObjects.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		sourceenv = request.getParameter("sourceenv");
		destinationenv = request.getParameter("destenv");
		sourcespid = request.getParameter("sourcespid");
		destinationspconn = request.getParameter("destspconn");
		destinationspid = request.getParameter("destspid");
		destinationacs = request.getParameterValues("destacs[]");
		signingCerts = request.getParameter("signingcert");
		isEncryption = request.getParameter("isencryption");

		Params.setSourceenv(sourceenv);
		Params.setDestinationenv(destinationenv);
		Params.setSourcespid(sourcespid);
		Params.setDestinationspconn(destinationspconn);
		Params.setDestspid(destinationspid);
		Params.setDestacs(destinationacs);

		if (isEncryption.equalsIgnoreCase("Yes")) {
			certFile = request.getPart("file");
			Params.setCertFile(certFile);
		}

		String[] signCertID = signingCerts.split(" ");

		Params.setSignCertID(signCertID[1]);

		try {
			conn = SpConnection.setSPConnection(isEncryption);
			if (conn.getResponseCode() == 201) {
				ClusterManagement.replicateChanges(Environment.getPingFedEnvironment(destinationenv));
				LOGGER.info(Constants.PF_SAML_SUCCESS_RESULT + "-" + destinationspconn);
				request.setAttribute("samlresult", Constants.PF_SAML_SUCCESS_RESULT + "-" + destinationspconn);
			} else {
				LOGGER.info(Constants.PF_SAML_FAIL_RESULT + "-" + destinationspconn);
				request.setAttribute("samlresult", Constants.PF_SAML_FAIL_RESULT + "-" + destinationspconn);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher("resultSaml.jsp");
		rd.forward(request, response);

	}
}
