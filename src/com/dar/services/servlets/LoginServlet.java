package com.dar.services.servlets;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.FingerPrintServices;
import com.dar.services.UserServices;
import com.dar.tools.ErrorTools;

public class LoginServlet  extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException{

		reponse.setContentType("application/json");
		String requestData = requete.getReader().lines().collect(Collectors.joining());
		
		/*------------- FingerPrint part ----------------------*/
		
		// get all informations FingerPrint from header
		int timezone = Integer.parseInt(requete.getHeader("timezone"));
		String resolution = requete.getHeader("resolution");
		int enabledCookie = Integer.parseInt(requete.getHeader("enabledCookie"));
		try {
			JSONObject j = new JSONObject(requestData);
			JSONObject json = UserServices.login(j.optString("mail", null), j.optString("password", null));
			if(json.has("error")){
				ErrorTools.setStatut(json.getJSONObject("error").getInt("code"), reponse);
			} else {
				FingerPrintServices.create(timezone, resolution, enabledCookie, json.optInt("id"));
			}
			reponse.getWriter().print(json);
		} catch (JSONException e) {
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			reponse.getWriter().print("{\"error\":{\"code\":"+ErrorTools.JSON_ERROR+",\"message\":\"JSONException\"}}");
		}

	}
}
