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
 * Servlet implementation class GoogleMapsAutocompleteServlet
 */
public class GoogleMapsAutocompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleMapsAutocompleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("prefixe")==null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("{\"error\":{\"code\":"+ErrorTools.UNAUTHORIZED_ACTION+",\"message\":\"BAD REQUEST FORMAT\"}}");
		}
		response.setContentType("application/json");
		String prefixe = request.getParameter("prefixe");
		
		try {
			JSONObject res = GoogleMapsAPIServices.getAutocompleteAddresses(prefixe);
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
