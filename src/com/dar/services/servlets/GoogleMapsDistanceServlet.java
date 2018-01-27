package com.dar.services.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.GoogleMapsAPIServices;
import com.dar.tools.ErrorTools;

/**
 * Servlet implementation class GoogleMapsDistanceServlet
 */
public class GoogleMapsDistanceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleMapsDistanceServlet() {
        super();
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		if(request.getParameter("mode")==null || request.getParameter("addr1")==null || request.getParameter("addr2")==null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("{\"error\":{\"code\":"+ErrorTools.UNAUTHORIZED_ACTION+",\"message\":\"BAD REQUEST FORMAT\"}}");
		}
		String address1= request.getParameter("addr1");
		String address2= request.getParameter("addr2");
		String mode= request.getParameter("mode");
		
		try {
			JSONObject res = GoogleMapsAPIServices.getDistanceExchange(address1, address2,mode);
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
