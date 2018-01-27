package com.dar.services.servlets;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.MessageServices;
import com.dar.tools.ErrorTools;

public class ListMessagesServlet  extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException{

		reponse.setContentType("application/json");
		String token = requete.getHeader("token");
		String idSender = requete.getParameter("id");
		String page = requete.getParameter("page");
		try {
			JSONObject json = MessageServices.getList(idSender, token, page);
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
