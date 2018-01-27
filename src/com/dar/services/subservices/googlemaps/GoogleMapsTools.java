package com.dar.services.subservices.googlemaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * La classe <code>GoogleMapsTools</code> fournit les outils necéssaires pour faire de requêtes à aux APIs
 * Googles Maps, nous avons utilsé 4 types d'API Google Maps <a href="">Google Maps Distance Matrix API</a>
 * pour récupèrer les adresses
 * 
 * @author GamesXchanges Team
 *
 */
@SuppressWarnings("deprecation")
public class GoogleMapsTools {
	

	private static final String API_KEY ="AIzaSyC1wJYoHnCcVyNjyy26HGg6B44UUdrHETI";
	private static final String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&";
	private static final String URL_GEOLOCATION ="https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyBp7S5L06BOgsXawqfeZEYqs7MCVfuxwWc";
	private static final String PLACES_API= "AIzaSyDgRwKDUHx6v0A9G_pFaPV0he00w8zrTKg";
	private static final String GEOCODE_API= "AIzaSyBto1WwMn2d7yw-aZNgfBAmVRvFpxVsQ60";// TO GET POSTAL_CODE 
	
	
	
	/**
	 * 
	 * @param address1
	 * @param address2
	 * @param mode : mode de transport
	 * @return the complete URI to send to GoogleMaps API
	 */
	private static String  setUrlGoogleMaps(String address1, String address2, String mode) {
		String url="";
		url=GoogleMapsTools.URL+"&mode="+mode.replaceAll(" ", "+")+"&origins="+address1.replaceAll(" ", "+")+"&destinations="+address2.replaceAll(" ", "+")+"&language=fr&key="+GoogleMapsTools.API_KEY;
		return url;
	}
	
	/**
	 * 
	 * @param prefixePlace
	 * @return All places begins with the same prefix as Json format response
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static JSONObject getPlaceAutocomplete(String prefixePlace) throws JSONException, IOException {
		JSONObject table = new JSONObject();
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+prefixePlace.replaceAll(" ", "+")
				+ "&types=geocode&"
				+ "language=fr&"
				+ "key="+GoogleMapsTools.PLACES_API;
		JSONObject result;
		try {
			result = GoogleMapsTools.openConnectionWithGoogleMaps(url);
		} catch (IOException e) {
			result = new JSONObject(e);
		}
		
		JSONArray predictions = result.optJSONArray("predictions");
		
		for (int i = 0; i < predictions.length(); i++) {
			JSONObject jsonObjectRow = (JSONObject) predictions.get(i);
			String address = jsonObjectRow.get("description").toString();
			String postal_cd = GoogleMapsTools.getPostalCode(address);
			String complete_addr =address+" "+postal_cd;
			JSONObject jsonObject = new JSONObject().put("address", complete_addr);
			table.accumulate("all_addresses",jsonObject);
		}
		JSONObject jsonObject= new JSONObject();
		JSONArray jsArray= table.optJSONArray("all_addresses");
		if (jsArray==null) {
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(table.get("all_addresses"));
			jsonObject.put("all_addresses",jsonArray);
			return jsonObject;
		} else {
			return table;
		}
	} 

	/**
	 * 
	 * @param URL of the Google maps API
	 * @return The distance between the two points and the time
	 * @throws IOException
	 */
	public static JSONObject openConnectionWithGoogleMaps(String URL) throws IOException{
		URL Url = new URL(URL);
		URLConnection conn = Url.openConnection();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String buffer="";
		String line;
		while ((line = in.readLine())!=null) {
			buffer+=line;
		}
		JSONObject res=null;
		try {
			res = new JSONObject(buffer);
		} catch (JSONException e) {
			res = new JSONObject(e);
		}
		return res;
	}
	
	/**
	 * Require a Post HTTP Request to get the response
	 * @return Json of GeoLocation lat and lng
	 * @throws IOException
	 */
	public static JSONObject geoLocation() throws IOException{
		InputStream inputStream = null;
		HttpClient client = new DefaultHttpClient();  
        HttpPost post = new HttpPost(GoogleMapsTools.URL_GEOLOCATION);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        inputStream = entity.getContent();
		String buffer="";
		String line;
		 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),8);
         
		while ((line = reader.readLine())!=null) {
			buffer+=line;
		}
		JSONObject res=null;
		try {
			res = new JSONObject(buffer);
		} catch (JSONException e) {
			res = new JSONObject(e);
		}
		inputStream.close();
		
		return res;
	}
	/**
	 * 
	 * @param address une chaine de charactère désignant l'adresse 
	 * @return le code postal à cette adresse ou cette localité
	 * @throws JSONException
	 * @throws IOException
	 */
	private static String getPostalCode(String address) throws JSONException, IOException {
		String postalCode="";
		
		String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+address.replaceAll(" ", "+")+"&key="+GEOCODE_API;
		JSONObject result = GoogleMapsTools.openConnectionWithGoogleMaps(url);
		
		JSONArray jj = result.optJSONArray("results");
		for (int i = 0; i < jj.length(); i++) {
			JSONObject jsonObjectRow = (JSONObject) jj.get(i);
			JSONArray add = jsonObjectRow.optJSONArray("address_components");
			for (int j = 0; j < add.length(); j++) {

				JSONObject jsonObjectRowZ = (JSONObject) add.get(j);
				if(jsonObjectRowZ.opt("types").toString().contains("postal_code")) {
					postalCode=jsonObjectRowZ.opt("short_name").toString();
				}
			}
		}
		
		return postalCode;
	} 
	
	/**
	 * 
	 * @param address1 : l'adresse de l'utilisateur 1
	 * @param address2 : l'adresse de l'utilisateur 2
	 * @param mode	   : mode de transport pour se déplacer soit en voiture <driving>, à pied <walking>,
	 * 					 en vélo <bicycling>, en transports publics <transit>  
	 * @return la durée et la distance qui sépare les deux utilisateurs
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject getDistanceBetweenUsers(String address1, String address2, String mode) throws IOException, JSONException {
		JSONObject resultTable= new JSONObject();
		
		JSONObject result = new JSONObject();
		String url = GoogleMapsTools.setUrlGoogleMaps(address1, address2, mode);
		result = GoogleMapsTools.openConnectionWithGoogleMaps(url);
		
		JSONArray jArray = new JSONArray(result.opt("rows").toString());
		for (int j = 0; j < jArray.length(); j++) {
			JSONObject jsonObjectRowZ = (JSONObject) jArray.get(j);
			JSONArray jArray2 = new JSONArray(jsonObjectRowZ.opt("elements").toString());
			for (int i = 0; i < jArray2.length(); i++) {
				JSONObject jsonObject = (JSONObject) jArray2.get(i);
				JSONObject duration = new JSONObject(jsonObject.opt("duration").toString());
				JSONObject distance = new JSONObject(jsonObject.opt("distance").toString());
				String status = jsonObject.opt("status").toString();

				JSONObject distDuration = new JSONObject();
				distDuration.put("duration",duration.opt("text").toString());
				distDuration.put("distance",distance.opt("text").toString());
				distDuration.put("status_http",status);
				
				resultTable.accumulate("distance_duration",distDuration);
			}
		}
		JSONObject jsonObject= new JSONObject();
		JSONArray jsArray= resultTable.optJSONArray("distance_duration");
		if (jsArray==null) {
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(resultTable.get("distance_duration"));
			jsonObject.put("distance_duration",jsonArray);
			return jsonObject;
		} else {
			return resultTable;
		}
	}
}
