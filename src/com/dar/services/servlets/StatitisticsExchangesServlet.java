package com.dar.services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.ExchangeServices;
import com.dar.tools.ErrorTools;

public class StatitisticsExchangesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatitisticsExchangesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getHeader("token")==null || request.getParameter("idUser")==null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("{\"error\":{\"code\":"+ErrorTools.UNAUTHORIZED_ACTION+",\"message\":\"TOKEN NULL\"}}");
		}
		response.setContentType("application/json");
		String token = request.getHeader("token");
		String idUser= request.getParameter("idUser");
		
		try {
			JSONObject res = ExchangeServices.getUserExchanges(token, idUser);
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
