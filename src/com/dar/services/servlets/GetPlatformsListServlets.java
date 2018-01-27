package com.dar.services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.GameServices;
import com.dar.tools.ErrorTools;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GetPlatformsListServlets extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		String token = request.getHeader("token");
		String idGameApi=request.getParameter("id_game");
		
		try {
			JSONObject json = GameServices.getPlatformsOfGame(token, idGameApi);
			if(json.has("error")){
				ErrorTools.setStatut(json.getJSONObject("error").getInt("code"), response);
			}
			response.getWriter().print(json);
		} catch (JSONException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("{\"error\":{\"code\":"+ErrorTools.JSON_ERROR+",\"message\":\"JSONException\"}}");
		} catch (UnirestException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("{\"error\":{\"code\":"+ErrorTools.UNIREST_ERROR+",\"message\":\"erreur interne\"}}");
		} 
	}

}
