package com.dar.services.servlets;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.ExchangeServices;
import com.dar.tools.ErrorTools;

public class RefuseExchangeServlet extends HttpServlet{
	@Override
	protected void doPut(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException{
		reponse.setContentType("application/json");
		String requestData = requete.getReader().lines().collect(Collectors.joining());
		String token = requete.getHeader("token");
		try {
			JSONObject j = new JSONObject(requestData);
			JSONObject json = ExchangeServices.refuseExchange(token, j.optString("id", null));
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
