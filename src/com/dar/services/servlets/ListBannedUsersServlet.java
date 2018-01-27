package com.dar.services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.UserServices;
import com.dar.tools.ErrorTools;

public class ListBannedUsersServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		String token = request.getHeader("token");
		
		try {
			JSONObject res = UserServices.getUsersBanned(token);
			System.out.println(res);
			if(res.has("error")){
				ErrorTools.setStatut(res.getJSONObject("error").getInt("code"), response);
			}
			response.getWriter().print(res);
		} catch (JSONException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("{\"error\":{\"code\":"+ErrorTools.JSON_ERROR+",\"message\":\"JSONException\"}}");
}		
	}

}
