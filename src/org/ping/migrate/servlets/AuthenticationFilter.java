package org.ping.migrate.servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.ping.oidc.DiscoveryDocument;
import org.ping.oidc.ReadOidcProperties;
import org.ping.oidc.SessionManager;

public class AuthenticationFilter implements Filter {

	private ServletContext context;
	String pingAutomate = null;
	String authenticationURL = null;
	private String[] noAuthorizationRequiredPages = { "/PingAutomation/Code", "/PingAutomation/js",
			"/PingAutomation/css", "/PingAutomation/favicon.ico", "/PingAutomation/"};

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("AuthenticationFilter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (!isAuthenticationRequired(req)) {
			chain.doFilter(request, response);
			return;
		} else {
			if (SessionManager.validateSession(req)) {
				RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
				rd.forward(request, response);

			} else {
				ReadOidcProperties r = new ReadOidcProperties();
				try {
					authenticationURL = DiscoveryDocument.getAuthServerObject().getString("authorization_endpoint")
							+ "?client_id=" + r.getPropValues().getProperty("client_id") + "&response_type="
							+ r.getPropValues().getProperty("response_type") + "&acr_values="
							+ r.getPropValues().getProperty("acr_values") + "&redirect_uri="
							+ "http://localhost.uhc.com:8080/PingAutomation/Code" + "&scope="
							+ r.getPropValues().getProperty("scope");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				res.sendRedirect(authenticationURL);
			}
		}
	}

	protected boolean isAuthenticationRequired(HttpServletRequest request) {
		String requestedURI = request.getRequestURI();
		for (int i = 0, length = noAuthorizationRequiredPages.length; i < length; i++) {
			if (requestedURI.contains(noAuthorizationRequiredPages[i])) {
				return false;
			}
		}
		return true;
	}

	public void destroy() {

	}
}
