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

public class SearchGameServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException{

		reponse.setContentType("application/json");
		String title = requete.getParameter("title");
		String offset = requete.getParameter("offset");
		
		try {
			
			JSONObject json = GameServices.searchGameInAPI(title, offset==null?"0":offset);
			
			if(json.has("error")){
				ErrorTools.setStatut(json.getJSONObject("error").getInt("code"), reponse);
			}
			reponse.getWriter().print(json);
		} catch (JSONException e) {
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			reponse.getWriter().print("{\"error\":{\"code\":"+ErrorTools.JSON_ERROR+",\"message\":\"JSONException\"}}");
		} catch (UnirestException e) {
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			reponse.getWriter().print("{\"error\":{\"code\":"+ErrorTools.UNIREST_ERROR+",\"message\":\"UniretException\"}}");
		} 

	}
}
