package com.dar.services;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.subservices.googlemaps.GoogleMapsTools;
import com.dar.tools.ErrorTools;
/**
 * La classe <code>GoogleMapsAPIServices</code> décrit les services offerts par l'API GoogleMaps
 * nous avons utilisé les deux services primordiaux également <code>getDistanceExchange</code> pour récupèrer 
 * la distance et le temps qui sépare les deux utilisateurs qui optent pour un échange et le service qui 
 * retourne l'autocomplete d'une adresse en saisissant le début d'une adresse, le service est nomé 
 * <code>getAutocompleteAddresses</code>
 * 
 * 
 * @author GamesXchanges Team
 *
 */
public class GoogleMapsAPIServices {

	/**
	 * 
	 * @param address1 : signifie l'adresse de l'utilisateur 1
	 * @param address2 : signifie l'adresse de l'utilisateur 2
	 * @param mode	   : mode de transport souhaité
	 * @return un JSONObject formé de la distance qui les sépare en km et le temps en minute 
	 * @throws JSONException
	 * @throws IOException
	 */
	public static JSONObject getDistanceExchange(String address1, String address2, String mode) throws JSONException, IOException {
		if (address1.equals("") || address2.equals("") || mode.equals("") || address1 == null || address2 == null || mode == null)
			return ServicesTools.serviceRefused("Argument manquant !", ErrorTools.MISSED_ARGS);
		return GoogleMapsTools.getDistanceBetweenUsers(address1, address2,mode);
		
	}
	
	/**
	 * 
	 * @param prefix : sigifie le prefixe de l'adresse envoyé par le client 
	 * @return : l'adresse complète à l'aide de l'API GoogleMap Places
	 * @throws JSONException
	 * @throws IOException
	 */
	public static JSONObject getAutocompleteAddresses(String prefix) throws JSONException, IOException {
		if (prefix.equals("") || prefix == null)
			return ServicesTools.serviceRefused("Argument manquant", ErrorTools.MISSED_ARGS);
		return GoogleMapsTools.getPlaceAutocomplete(prefix);
	}
}
