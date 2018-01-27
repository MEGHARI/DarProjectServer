package com.dar.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.hibernate.dao.MessageDAO;
import com.dar.hibernate.dao.UserDAO;
import com.dar.hibernate.dao.UserSessionDAO;
import com.dar.hibernate.entity.User;
import com.dar.hibernate.entity.UserSession;
import com.dar.hibernate.utility.HibernateUtility;
import com.dar.tools.ErrorTools;

/**
 * Cette classe <code>MessageServices</code> fournit tous les services necéssaires pour envoyer, récupèrer la liste 
 * des toutes les conversations et des messages avec une personne donnée et aussi une service de notifications
 * 
 * 
 * @author GamesXchanges Team
 *
 */
public class MessageServices {

	private static UserDAO userDao = new UserDAO(User.class, HibernateUtility.getSessionFactory());
	private static MessageDAO messageDao = new MessageDAO();
	private static UserSessionDAO usDao = new UserSessionDAO(UserSession.class, HibernateUtility.getSessionFactory());

	/**
	 * Ce service permet d'envoyer un message d'un utilisateur à un autre. Le systéme enregistre le message
	 * dans une base de donnée noSQL (mongoDB)
	 * @param token : le jeton qui permet d'identifier un utilisateur connecté qui envoie le message 
	 * @param idReceiver : Id de l'utilisateur qui reçoie le message
	 * @param message : le message envoyé
	 * @return : un message de confirmation en format JSON
	 * @throws JSONException
	 */
	public static JSONObject send(String token, String idReceiver, String message) throws JSONException{
		if(token == null || idReceiver == null || message == null){
			return ServicesTools.serviceRefused("argument manquant", ErrorTools.MISSED_ARGS);
		}
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant",ErrorTools.WRONG_TOKEN);
		}
		if(message.equals("")){
			return ServicesTools.serviceRefused("message vide", ErrorTools.WRONG_MSG);
		}
		int idR = 0;
		try {
			idR = Integer.parseInt(idReceiver);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entr�e", ErrorTools.WRONG_FORMAT);
		}
		int idSender = usDao.getIdUserByToken(token);
		if(userDao.findById(idSender)==null || userDao.findById(idR) == null){
			return ServicesTools.serviceRefused("utilisateur innexistant",ErrorTools.USER_NOT_FOUND);
		}

		return messageDao.send(idSender, idR, message, 0, 0);
	}

	/**
	 * Ce service permet de lister les messages entre deux personnes
	 * @param idSender : L'id de la personne qui a envoyé le message
	 * @param token : le jeton qui permet d'identifier un utilisateur connecté qui reçoit les messages 
	 * @param page : sert d'offset. page 1 = 5 dérniers messages en format JSON
	 * @return la liste des messages entre deux personnes 
	 * @throws JSONException
	 */
	public static JSONObject getList(String idSender, String token, String page) throws JSONException{
		if(idSender == null || token == null || page == null){
			return ServicesTools.serviceRefused("argument manquant",-1);
		}
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant",ErrorTools.WRONG_TOKEN);
		}
		int idS = 0;
		int p = 0;
		try {
			idS = Integer.parseInt(idSender);
			p = Integer.parseInt(page);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entr�e",-1);
		}

		int idR = usDao.getIdUserByToken(token);

		if(idS!=0){
			if(userDao.findById(idS)==null || userDao.findById(idR) == null){
				return ServicesTools.serviceRefused("utilisateur innexistant",-1);
			}
		}else{
			if(userDao.findById(idR) == null){
				return ServicesTools.serviceRefused("utilisateur innexistant",-1);
			}
		}

		return messageDao.getList(idS, idR, p);
	}

	/**
	 * Ce services permet d'avoir une liste de conversations d'une personne avec le nombre de message non lus
	 * @param token : le jeton qui permet d'identifier un utilisateur connecté qui reçoit les messages 
	 * @return Retourne la liste des conversations avec le nombres de messages non lus pour chaque conversations en format JSON
	 * @throws JSONException
	 */
	public static JSONObject getAllConversations(String token) throws JSONException{
		if(token == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant",ErrorTools.WRONG_TOKEN);
		}
		int idR = usDao.getIdUserByToken(token);
		if(userDao.findById(idR)==null){
			return ServicesTools.serviceRefused("utilisateur innexistant",ErrorTools.USER_NOT_FOUND);
		}

		return messageDao.getNoShowingConversations(idR);
	}

	/**
	 * Ce service permet de notifier un utilisateur par le nombre de messages non lus et les notifications du système
	 * @param token
	 * @return Le nombre de messages non lus et le nombre de notifications du système en format JSON
	 * @throws JSONException
	 */
	public static JSONObject notifier(String token) throws JSONException{
		if(token == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant",ErrorTools.WRONG_TOKEN);
		}
		int idR = usDao.getIdUserByToken(token);
		if(userDao.findById(idR)==null){
			return ServicesTools.serviceRefused("utilisateur innexistant",ErrorTools.USER_NOT_FOUND);
		}

		return messageDao.notifier(idR);
	}

}
