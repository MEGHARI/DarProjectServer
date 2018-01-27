package com.dar.services.servlets;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.UserServices;
import com.dar.tools.ErrorTools;

public class CreateUserServlet  extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException{

		reponse.setContentType("application/json");
		String requestData = requete.getReader().lines().collect(Collectors.joining());
		try {
			JSONObject res = new JSONObject(requestData);			
			
			JSONObject json = UserServices.createUser(res.optString("lastName", null), res.optString("firstName", null), res.optString("address", null),
					res.optString("mail", null), res.optString("password", null));
			if(json.has("error")){
				ErrorTools.setStatut(json.getJSONObject("error").getInt("code"), reponse);
			}
			else if(json.has("errors")){
				reponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			reponse.getWriter().print(json);
		} catch (JSONException e) {
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			reponse.getWriter().print("{\"error\":{\"code\":"+ErrorTools.JSON_ERROR+",\"message\":\"JSONException\"}}");
		}

	}
}
