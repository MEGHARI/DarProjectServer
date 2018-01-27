package com.dar.services.subservices.facebook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.UserServices;
import com.dar.tools.ErrorTools;

/**
 * Servlet implementation class CallBackRedirect
 */
public class CallBackRedirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallBackRedirect() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		String code = request.getParameter("code");
		System.out.println(code);
		String facebookGraphURL = FacebookTools.getFacebookGraphURL(code);
		String facebookConnection = FacebookTools.openConnectionWithFacebook(facebookGraphURL);
		String token = FacebookTools.getAccessToken(facebookConnection);
		

		JSONObject userProfile = FacebookTools.getJson(FacebookTools.getUserProfile(token));
		System.out.println(userProfile);
		
		
		response.getWriter().print(userProfile);
		
				
		
	}

}
