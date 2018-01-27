package com.dar.services.servlets;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.GameServices;
import com.dar.tools.ErrorTools;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AddGameServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException{
		reponse.setContentType("application/json");
		String requestData = requete.getReader().lines().collect(Collectors.joining());
		
		try {
			JSONObject j = new JSONObject(requestData);
			JSONObject json = GameServices.add(j.optString("id", null));
			if(json.has("error")){
				ErrorTools.setStatut(json.getJSONObject("error").getInt("code"), reponse);
			}
			
			reponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch (JSONException e) {
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			reponse.getWriter().print("{\"error\":{\"code\":"+ErrorTools.JSON_ERROR+",\"message\":\"JSONException\"}}");
		} catch (UnirestException e) {
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			reponse.getWriter().print("{\"error\":{\"code\":"+ErrorTools.UNIREST_ERROR+",\"message\":\"erreur interne\"}}");
		}

	}
}
