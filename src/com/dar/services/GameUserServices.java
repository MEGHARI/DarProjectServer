package com.dar.services;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dar.hibernate.dao.ExchangeDAO;
import com.dar.hibernate.dao.GamePlatformDAO;
import com.dar.hibernate.dao.GameUserDAO;
import com.dar.hibernate.dao.UserDAO;
import com.dar.hibernate.dao.UserSessionDAO;
import com.dar.hibernate.entity.Exchange;
import com.dar.hibernate.entity.Game;
import com.dar.hibernate.entity.GamePlatform;
import com.dar.hibernate.entity.GameUser;
import com.dar.hibernate.entity.User;
import com.dar.hibernate.entity.UserSession;
import com.dar.hibernate.utility.HibernateUtility;
import com.dar.tools.ErrorTools;

public class GameUserServices  {
	private static GameUserDAO gameUserDao = new GameUserDAO(GameUser.class, HibernateUtility.getSessionFactory());

	private static UserDAO userDao = new UserDAO(User.class, HibernateUtility.getSessionFactory());
	private static UserSessionDAO usDao = new UserSessionDAO(UserSession.class, HibernateUtility.getSessionFactory());
	private static ExchangeDAO exchangeDao = new ExchangeDAO(Exchange.class, HibernateUtility.getSessionFactory());


	public static JSONObject create(String token, String idGamePlatform, String relation) throws JSONException{
		if(token == null || idGamePlatform==null || relation==null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}	
		int idg = 0;
		int idu = usDao.getIdUserByToken(token);
		int rel = 0;
		try {
			idg = Integer.parseInt(idGamePlatform);
			rel = Integer.parseInt(relation);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}
		if(rel!=0 && rel!=1)
			return ServicesTools.serviceRefused("Erreur relation",ErrorTools.WRONG_RELATION);

		if(gameUserDao.gameUserExist(idu, idg)){
			return ServicesTools.serviceRefused("GameUser existe déja",ErrorTools.UNAUTHORIZED_ACTION);
		}
		
		GamePlatformDAO gamePlatformDao = new GamePlatformDAO(GamePlatform.class, HibernateUtility.getSessionFactory());
		UserDAO userDao = new UserDAO(User.class, HibernateUtility.getSessionFactory());
		GamePlatform game = gamePlatformDao.findById(idg);
		User user = userDao.findById(idu);
		if(game==null) 
			return ServicesTools.serviceRefused("aucun jeu trouvé", ErrorTools.GAME_NOT_FOUND);
		if(user==null)
			return ServicesTools.serviceRefused("aucun utilisateur trouvé",ErrorTools.USER_NOT_FOUND);

		try {
			gameUserDao.create(new GameUser(user, game, rel));
		} catch (Exception e) {
			return ServicesTools.serviceRefused("erreur base de donnée", ErrorTools.BDD_ERROR);
		}
		return ServicesTools.serviceAccepted("ajout effectué avec succés");
	}	

	public static JSONObject remove(String token, String idGameUser) throws JSONException{
		if(token == null || idGameUser == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);

		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}

		int id = 0;
		try {
			id = Integer.parseInt(idGameUser);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}
		GameUser gu = gameUserDao.findById(id);
		if(gu == null){
			return ServicesTools.serviceRefused("gameUser n'existe pas",ErrorTools.GAME_USER_NOT_FOUND);
		}
		if(exchangeDao.exchangeExist(gu.getId())){
			return ServicesTools.serviceRefused("jeu est en plein echange", ErrorTools.UNAUTHORIZED_ACTION);
		}
		exchangeDao.deleteList(exchangeDao.getListExchangesToDelete(gu.getId()));
		gameUserDao.delete(gu);
		return ServicesTools.serviceAccepted("suppression effectu� avec succés");
	}	

	public static JSONObject listGames(String token, String relation) throws JSONException{
		if(token == null || relation == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);

		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}

		int rel = 0;
		try {
			rel = Integer.parseInt(relation);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}
		if(rel!=0 && rel!=1)
			return ServicesTools.serviceRefused("Erreur relation",ErrorTools.WRONG_RELATION);

		int id = usDao.getIdUserByToken(token);
		User user = userDao.findById(id);
		if(user==null){
			if(!usDao.sessionExist(token)){
				return ServicesTools.serviceRefused("user innexistant", ErrorTools.USER_NOT_FOUND);
			}
		}
		Set<GameUser> list = user.getGameUsers();

		JSONObject res = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		Game g = null;

		for(GameUser gu: list){
			if(gu.getRelation()==rel){
				json = new JSONObject();
				g = gu.getGame().getGamep();
				json.put("id", gu.getId());
				json.put("game_name", g.getName());
				json.put("summary", g.getSummary());
				json.put("date_release", g.getReleaseDate());
				json.put("genre", g.getGenre());
				json.put("game_cover", g.getCover());
				json.put("platform", gu.getGame().getPlatform().getName());
				json.put("id_user", gu.getUser().getId());
				array.put(json);
			}
		}
		
		res.put("games", array);

		return res;
	}	



	/**
	 * 
	 * @param token
	 * @param idUser
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject listGamesPocessLike(String token, String idUser) throws JSONException{
		if(token == null || idUser == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);

		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}

		int id = usDao.getIdUserByToken(token);
		
		int idu = 0 ;
		try {
			idu = Integer.parseInt(idUser);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}
		User userByToken = userDao.findById(id);
		User user = userDao.findById(idu);
		if(userByToken==null || user == null){
			return ServicesTools.serviceRefused("user innexistant", ErrorTools.USER_NOT_FOUND);
		}
		Set<GameUser> list = user.getGameUsers();

		JSONObject res = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
	 	Game g = null;

		for(GameUser gu: list){
			json = new JSONObject();
			g = gu.getGame().getGamep();
			json.put("id", gu.getGame().getId());
			json.put("id_user", gu.getUser().getId());
			json.put("game_name", g.getName());
			json.put("summary", g.getSummary());
			json.put("date_release", g.getReleaseDate());
			json.put("genre", g.getGenre());
			json.put("game_cover", g.getCover());
			json.put("platform", gu.getGame().getPlatform().getName());
			json.put("relation", gu.getRelation());
			json.put("url_picture", gu.getUser().getUrlPicture());
			json.put("first_name", gu.getUser().getFirstName());
			json.put("last_name", gu.getUser().getLastName());
			array.put(json);
		}
		
		res.put("games", array);

		return res;
	}	

}
