package com.dar.services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.GameUserServices;
import com.dar.tools.ErrorTools;

public class ListGamesPocessLikeServlet extends HttpServlet {

	protected void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException {
		reponse.setContentType("application/json");
		String token = requete.getHeader("token");
		String idUser = requete.getParameter("id");
		
		try {
			JSONObject json = GameUserServices.listGamesPocessLike(token, idUser);
			if(json.has("error")){
				ErrorTools.setStatut(json.getJSONObject("error").getInt("code"), reponse);
			}
			reponse.getWriter().print(json);
		} catch (JSONException e) {
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			reponse.getWriter().print("{\"error\":{\"code\":"+ErrorTools.JSON_ERROR+",\"message\":\"JSONException\"}}");
		}
		
	}


}
