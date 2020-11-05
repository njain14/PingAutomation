package org.ping.oidc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionManager {

	static String pingAutomate;

	public static boolean validateSession(HttpServletRequest Request) throws IllegalArgumentException, IOException {
		Cookie[] cookies = Request.getCookies();
		ReadOidcProperties r = new ReadOidcProperties();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("pingAutomate")) {
					pingAutomate = cookie.getValue();
					if (validateCookie(pingAutomate, r.getPropValues().getProperty("client_id"))) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;

	}

	public static void createSession(HttpServletResponse response, String token) {
		Cookie c = new Cookie("pingAutomate", "true");
		c.setDomain("uhc.com");
		c.setPath("/");
		c.setValue(token);
		c.setSecure(false);
		c.setMaxAge(-1);
		response.addCookie(c);
	}

	public static void destroySession() {

	}

	private static boolean validateCookie(String cookie, String client_id)
			throws IllegalArgumentException, IOException {
		String requestParams = "?token=" + cookie + "&client_id=" + client_id;
		String jwtValidateAPI = "http://localhost:8081/verifyaccessjwt";
		String apiUrl = jwtValidateAPI + requestParams;
		System.out.println(apiUrl);
		URL url = new URL(apiUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.getOutputStream();
		int responseCode = con.getResponseCode();
		System.out.println(responseCode);

		if (responseCode != HttpURLConnection.HTTP_OK) {
			return false;
		}
		return true;
	}

}
