package com.dar.services.subservices.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookTools {
	
	public static final String FB_APP_ID = "395287297553702";
	public static final String FB_APP_SECRET = "90505d272f8d49b48d50c73e88c059af";
	public static final String REDIRECT_URI = "http://localhost:8080/DarProject/testFacebook/userProfile";
	
	public static final String PROFILE_GRAPH = "https://graph.facebook.com/v2.1/me?fields=first_name,last_name,birthday,gender,address,hometown,work,picture{url}&access_token=";
	
	/**
	 * 
	 * @return the URL for Authentication
	 * @throws UnsupportedEncodingException
	 */
	public static String getAuthentificationURL(){
		String facebookURL = "";
		
				try {
					facebookURL = "http://www.facebook.com/dialog/oauth?" + "client_id="
							+ FacebookTools.FB_APP_ID + "&redirect_uri="
							+ URLEncoder.encode(FacebookTools.REDIRECT_URI, "UTF-8")
							+ "&scope=public_profile,email,user_birthday,user_hometown,user_location";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
		
				return facebookURL; 
	}
	
	/**
	 * 
	 * @param code
	 * @return the graph url 
	 * @throws UnsupportedEncodingException
	 */
	public static String getFacebookGraphURL(String code) throws UnsupportedEncodingException{
		String graphURL = "";
		
		graphURL = "https://graph.facebook.com/oauth/access_token?"
				 + "client_id=" + FacebookTools.FB_APP_ID + "&redirect_uri="
				 + URLEncoder.encode(FacebookTools.REDIRECT_URI, "UTF-8")
				 + "&client_secret=" + FacebookTools.FB_APP_SECRET + "&code=" + code;
		
		return graphURL;
	}
	
	/**
	 * 
	 * @param graphURL
	 * @return URL response
	 * @throws IOException
	 */
	public static String openConnectionWithFacebook(String graphURL) throws IOException{
		URL Url = new URL(graphURL);
		URLConnection conn = Url.openConnection();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		
		String line = in.readLine();
		return line;
	}
	
	/**
	 * 
	 * @param line
	 * @return Access_Token
	 */
	public static String getAccessToken(String line){
		String TOKEN;
		
		String [] sp = line.split(",");
		
		String accessTokenJson[] = sp[0].split(":");
		String access_token = accessTokenJson[1];
		TOKEN = access_token.substring(1, access_token.length()-1);
		return TOKEN;
		
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return User Profile facebook
	 * @throws IOException
	 */
	public static String getUserProfile(String accessToken) throws IOException{
		
		URL graphURL = new URL(PROFILE_GRAPH + accessToken);
        HttpURLConnection myWebClient = (HttpURLConnection) graphURL.openConnection();        

		BufferedReader input = new BufferedReader(new InputStreamReader(
				myWebClient.getInputStream()));
		
		
		StringBuffer b = null;
		String readIn="";
		
		b = new StringBuffer();
		while ((readIn = input.readLine()) != null)
			b.append(readIn + "\n");
		
		return b.toString();
	}
	
	public static JSONObject getJson(String urlResponse){
		JSONObject result=new JSONObject();
		try {
			String first_name;
			String last_name;
			String birthday;
			String email;
			String nameTown;
			String gender;
			String picture;
			
			JSONObject js = new JSONObject(urlResponse);

			try {
				first_name=js.getString("first_name");
			} catch (JSONException e) {
				first_name="NULL";
			}
		    try {
		    	last_name=js.getString("last_name");
			} catch (JSONException e) {
				last_name="NULL";
			}
			try {
			    birthday=js.getString("birthday");				
			} catch (JSONException e) {
			    birthday="NULL";
			}
			try {
				email=js.getString("email");	
			} catch (JSONException e) {
				email="NULL";
			}
			
			try {
				JSONObject jsobb = js.getJSONObject("hometown");
				nameTown = jsobb.getString("name");
			} catch (JSONException e) {
				nameTown = "NULL";
			}
			try {
				gender=js.getString("gender");	
			} catch (JSONException e) {
				gender="NULL";
			}
			try {
				JSONObject jsonPicture = js.getJSONObject("picture");
				JSONObject jsonData = jsonPicture.getJSONObject("data");
				picture=jsonData.getString("url");
			} catch (JSONException e) {
				picture="NULL";
			}
			result.put("first_name", first_name);
			result.put("last_name", last_name);
			result.put("birthday", birthday);
			result.put("email", email);
			result.put("adress", nameTown);
			result.put("gender", gender);
			result.put("picture", picture);
			System.out.println(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	

}
