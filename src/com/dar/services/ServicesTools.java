package com.dar.services;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * La classe <code>ServicesTools</code> fournit les services d'acceptation ou de refus lors de la demande
 * d'un service, cette classe illustre deux services <code>serviceAccepted</code> en cas de succès et 
 * <code>serviceRefused</code> en cas d'erreur lors de la demande d'un service. 
 * @author GamesXchanges Team
 *
 */
public class ServicesTools {
	/**
	 * 
	 * @param message
	 * @param codeErreur
	 * @return un message d'erreur et le code de l'erreur sous format JSON
	 * @throws JSONException
	 */
	public static JSONObject serviceRefused(String message, int codeErreur) throws JSONException{
		JSONObject json= new JSONObject();
		json.put("error", new JSONObject().put("code", codeErreur).put("message", message));
		return json;
	}
	/**
	 * 
	 * @param message
	 * @return un message de succès et le code sous format JSON
	 * @throws JSONException
	 */
	public static JSONObject serviceAccepted(String message) throws JSONException{
		JSONObject json= new JSONObject();
		json.put("message", message);
		return json;
	}
}