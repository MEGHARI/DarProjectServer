package com.dar.services.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.UserServices;
import com.dar.tools.ConfigTools;
import com.dar.tools.ErrorTools;
import com.dar.tools.RandomName;

import sun.misc.BASE64Decoder;

/**
 * Servlet implementation class UpdateUserInformations
 */
public class UpdateUserInformations extends HttpServlet {

	protected void doPut(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException, IOException {
		
		reponse.setContentType("application/json");
		String requestData = requete.getReader().lines().collect(Collectors.joining());
		String token = requete.getHeader("token");
		
		BufferedImage image = null;
		byte[] imageByte;
		
		try {
			
			JSONObject res = new JSONObject(requestData);
			String url = null; 
			if(res.has("avatar") && !res.isNull("avatar")){
				BASE64Decoder decoder = new BASE64Decoder();
				imageByte = decoder.decodeBuffer(res.getJSONObject("avatar").getString("value"));
				ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
				image = ImageIO.read(bis);
				bis.close();
				String dir = ConfigTools.TOMCAT_FOLDER_WEBAPPS
						+ConfigTools.PROJECT_FOLDER
						+ConfigTools.UPLOADED_IMAGE_FOLDER;
				url = RandomName.randomIdentifier()+".png";
				File outputfile = new File("/opt/tomcat/webapps/DarProject/"+url);
				ImageIO.write(image, "png", outputfile);
			}

			
			
			JSONObject json = UserServices.updateUser(token, res.optString("lastName", null), res.optString("firstName", null), res.optString("address", null),
					res.optString("password", null), url);
			if(json.has("error")){
				ErrorTools.setStatut(json.getJSONObject("error").getInt("code"), reponse);
			}
			else if(json.has("errors")){
				reponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			reponse.getWriter().print(json);
		} catch (JSONException e) {
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			reponse.getWriter().print("{\"error\":{\"code\":"+ErrorTools.JSON_ERROR+",\"message\":\"JSONException\"}}");
		}
	}

}
