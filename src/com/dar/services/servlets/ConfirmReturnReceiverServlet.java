package com.dar.services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.ExchangeServices;

public class ConfirmReturnReceiverServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException{

		String id = requete.getParameter("id");
		
		try {
			JSONObject json = ExchangeServices.confirmReturnReceiver(id);
//			if(json.has("erreurs")){
//				JSONArray jarray = json.getJSONArray("erreurs");
//				json = jarray.getJSONObject(0);
//				System.out.println(json.get("code"));
//				// vérifier le code pour savoir quelle header envoyé
//			}
			reponse.setContentType("text/plain");
			reponse.getWriter().print(json);
		} catch (JSONException e) {
			reponse.setContentType("text/plain");
			reponse.getWriter().print("{\"Erreurs\":[{\"code\":-1,\"message\":\"JSONException\"}]}");
		} 

	}
}
