package org.ping.oidc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

@WebServlet("/Code")
public class Code extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Code() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");

		System.out.println("Returned code " + code);
		if (code != null) {
			System.out.println("Get Access Token");
			try {
				BackChannelAuth.getTokens(request, response, code);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
