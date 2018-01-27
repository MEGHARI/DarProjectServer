package com.dar.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dar.hibernate.dao.ExchangeDAO;
import com.dar.hibernate.dao.GameDAO;
import com.dar.hibernate.dao.GamePlatformDAO;
import com.dar.hibernate.dao.GameUserDAO;
import com.dar.hibernate.dao.MessageDAO;
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

/**
 * Cette classe <code>ExchangeServices</code> fournit tous les services necéssaires pour gérer les echanges 
 * (demande, confirmation, refus...)
 * 
 * 
 * @author GamesXchanges Team
 *
 */
public class ExchangeServices {

	private static ExchangeDAO exchangeDao = new ExchangeDAO(Exchange.class, HibernateUtility.getSessionFactory());
	private static UserSessionDAO usDao = new UserSessionDAO(UserSession.class, HibernateUtility.getSessionFactory());
	private static GameUserDAO gameUserDao = new GameUserDAO(GameUser.class, HibernateUtility.getSessionFactory());
	private static GamePlatformDAO gamePlatformDao = new GamePlatformDAO(GamePlatform.class, HibernateUtility.getSessionFactory());
	private static GameDAO gameDao = new GameDAO(Game.class, HibernateUtility.getSessionFactory());
	private static MessageDAO msgDao = new MessageDAO();

	
	public static JSONObject create(String token, String idGameUserSender, String idGameUserReceiver,
			String dateExchange, String dateRetrun) throws JSONException{

		if(token == null || idGameUserSender == null || idGameUserReceiver == null || 
				dateExchange == null || dateRetrun == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}	

		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dExchange = null;
		Date dReturn = null;
		Date dActuelle = new Date();
		try {
			dExchange = formatter.parse(dateExchange);
			dReturn = formatter.parse(dateRetrun);
		} catch (ParseException e1) {
			return ServicesTools.serviceRefused("format date érroné", ErrorTools.WRONG_DATE);
		}
		
		if(dExchange.after(dReturn)){
			return ServicesTools.serviceRefused("opération impossible: date de retour avant date d'échange", ErrorTools.UNAUTHORIZED_ACTION);
		}
		
		if(dActuelle.after(dExchange)){
			return ServicesTools.serviceRefused("opération impossible: vous devez choisir la bonne date pour la date d'échange", ErrorTools.UNAUTHORIZED_ACTION);
		}
		
		if(dActuelle.after(dReturn)){
			return ServicesTools.serviceRefused("opération impossible: vous devez choisir la bonne date pour la date de retour", ErrorTools.UNAUTHORIZED_ACTION);
		}

		int id1 = 0;
		int id2 = 0;
		try {
			id1 = Integer.parseInt(idGameUserSender);
			id2 = Integer.parseInt(idGameUserReceiver);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}
		if(id1==id2){
			return ServicesTools.serviceRefused("Opération impossible",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(exchangeDao.exchangeExist(id1) || exchangeDao.exchangeExist(id2) ){
			return ServicesTools.serviceRefused("Un echange existant empéche l'opération",ErrorTools.EXIST_ECHANGE);
		}

		GameUserDAO gameUserDao = new GameUserDAO(GameUser.class, HibernateUtility.getSessionFactory());
		GameUser gus = gameUserDao.findById(id1);
		GameUser gur = gameUserDao.findById(id2);


		if(gus == null || gur == null){
			return ServicesTools.serviceRefused("Aucun gameUser trouvé",ErrorTools.GAME_USER_NOT_FOUND);
		}
		
		if(gus.getRelation()==0 || gur.getRelation()==0){
			return ServicesTools.serviceRefused("un des utilisateurs ne possède pas le jeu",ErrorTools.UNAUTHORIZED_ACTION);
		}

		if(gus.getUser().getId()==id1){
			return ServicesTools.serviceRefused("Opération impossible",ErrorTools.UNAUTHORIZED_ACTION);
		}
		int idExchange = 0;
		try {
			idExchange = exchangeDao.create(new Exchange(gus.getId(), gur.getId(), dateExchange,dateRetrun));
		
		}catch (Exception e) {
			return ServicesTools.serviceRefused("erreur base de données", ErrorTools.BDD_ERROR);
		}
		String message = gus.getUser().getFirstName()+" "+ gus.getUser().getLastName()
				+ " vous propose un échange de jeu."
				+ " Il vous propose : "+  gus.getGame().getGamep().getName()+"."
				+ " Contre votre : "+ gur.getGame().getGamep().getName()+"."
				+ " Son adresse : "+gur.getUser().getAddress();
		msgDao.send(0, gur.getUser().getId(), message, 1, idExchange);
		return ServicesTools.serviceAccepted("echange demandé");
	}

	public static JSONObject confirmExchange(String token, String idExchange) throws JSONException{
		if(idExchange == null || token == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}
		int id = 0;
		try {
			id = Integer.parseInt(idExchange);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}
		Exchange e = exchangeDao.findById(id);
		if(e==null){
			return ServicesTools.serviceRefused("echange innexistant",ErrorTools.EXCHANGE_NOT_FOUND);
		}
		if(e.getConfirmExchange()==1){
			return ServicesTools.serviceRefused("Echange déja confirmé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(e.getConfirmExchange()==-1){
			return ServicesTools.serviceRefused("echange annulé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		e.setConfirmExchange(1);
		exchangeDao.update(e);
		
		GameUser guR = gameUserDao.findById(e.getIdReceiver());
		GameUser guS = gameUserDao.findById(e.getIdSender()); 
		
		String message = guR.getUser().getFirstName() +" "+guR.getUser().getLastName()
				+ " a accepté votre demande d'échange"
				+ " Pour rappel: Vous lui avez proposé : "+  guS.getGame().getGamep().getName()+"."
				+ " Contre son : "+ guR.getGame().getGamep().getName();
		msgDao.send(0, guS.getUser().getId(), message, 2, e.getId());
		msgDao.setStatut(e.getId(), 1);
		return ServicesTools.serviceAccepted("echange confirmé avec succés");
	}

	public static JSONObject refuseExchange(String token, String idExchange) throws JSONException{
		if(idExchange == null || token == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}
		
		int id = 0;
		try {
			id = Integer.parseInt(idExchange);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}
		Exchange e = exchangeDao.findById(id);
		if(e==null){
			return ServicesTools.serviceRefused("echange innexistant",ErrorTools.EXCHANGE_NOT_FOUND);
		}
		if(e.getConfirmExchange()==1){
			return ServicesTools.serviceRefused("echange déja confirmé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(e.getConfirmExchange()==-1){
			return ServicesTools.serviceRefused("echange déja annulé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		e.setConfirmExchange(-1);
		exchangeDao.update(e);
		
		GameUser guR = gameUserDao.findById(e.getIdReceiver());
		GameUser guS = gameUserDao.findById(e.getIdSender()); 
		
		String message = guR.getUser().getFirstName() +" "+guR.getUser().getLastName()
				+ " a refusé votre demande d'échange"
				+ " Pour rappel: Vous lui avez proposé : "+  guS.getGame().getGamep().getName()+"."
				+ " Contre son : "+ guR.getGame().getGamep().getName();
		msgDao.send(0, guS.getUser().getId(), message, 3, e.getId());
		msgDao.setStatut(e.getId(), -1);
		return ServicesTools.serviceAccepted("echange refusé avec succés");
	}

	public static JSONObject confirmReturnSender(String ids) throws JSONException{
		if(ids == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		int id = 0;
		try {
			id = Integer.parseInt(ids);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}
		Exchange e = exchangeDao.findById(id);
		if(e==null){
			return ServicesTools.serviceRefused("echange innexistant",ErrorTools.EXCHANGE_NOT_FOUND);
		}
		if(e.getConfirmReturnSender()==1){
			return ServicesTools.serviceRefused("Echange déja confirmé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(e.getConfirmExchange()==-1){
			return ServicesTools.serviceRefused("echange annulé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(e.getConfirmExchange()==0){
			return ServicesTools.serviceRefused("echange non confirmé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(e.getConfirmReturnReceiver()==1){
			msgDao.setStatut(e.getId(), 4);
			e.setReturnOk(1);
		}else{
			msgDao.setStatut(e.getId(), 2);
		}
		e.setConfirmReturnSender(1);
		exchangeDao.update(e);
		
		GameUser guR = gameUserDao.findById(e.getIdReceiver());
		GameUser guS = gameUserDao.findById(e.getIdSender()); 
		
		String message = "CONFIRMATION RETOUR."
				+ guS.getUser().getFirstName() +" "+guS.getUser().getLastName()
				+ " a confirmé le retour du jeu"
				+ " Pour rappel: vous avez echangé votre : "+  guS.getGame().getGamep().getName()+"."
				+ " Contre son : "+ guR.getGame().getGamep().getName();
		msgDao.send(0, guS.getUser().getId(), message, 4, e.getId());
		
		return ServicesTools.serviceAccepted("retour Sender confirmé avec succés");
	}

	public static JSONObject confirmReturnReceiver(String ids) throws JSONException{
		if(ids == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		int id = 0;
		try {
			id = Integer.parseInt(ids);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.PLATFORM_ERROR);
		}
		Exchange e = exchangeDao.findById(id);
		if(e==null){
			return ServicesTools.serviceRefused("echange innexistant",ErrorTools.EXCHANGE_NOT_FOUND);
		}
		if(e.getConfirmReturnSender()==1){
			return ServicesTools.serviceRefused("Echange déja confirmé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(e.getConfirmExchange()==-1){
			return ServicesTools.serviceRefused("echange annulé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(e.getConfirmExchange()==0){
			return ServicesTools.serviceRefused("echange non confirmé",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(e.getConfirmReturnSender()==1){
			msgDao.setStatut(e.getId(), 4);
			e.setReturnOk(1);
		}else{

			msgDao.setStatut(e.getId(), 3);
		}
		e.setConfirmReturnReceiver(1);
		exchangeDao.update(e);
		
		GameUser guR = gameUserDao.findById(e.getIdReceiver());
		GameUser guS = gameUserDao.findById(e.getIdSender()); 
		
		String message = guR.getUser().getFirstName() +" "+guR.getUser().getLastName()
				+ " a confirmé le retour du jeu"
				+ " Pour rappel: vous avez echangé votre : "+  guS.getGame().getGamep().getName()+"."
				+ " Contre son : "+ guR.getGame().getGamep().getName();
		msgDao.send(0, guS.getUser().getId(), message, 5, e.getId());
		
		return ServicesTools.serviceAccepted("retour receiver confirmé avec succés");
	}



private static JSONObject getStatitisticsExchanges(int idGameUser) throws JSONException{
		
		List<Exchange> list = exchangeDao.getListExchanges(idGameUser);
		JSONObject res = new JSONObject();
		for (int i = 0; i < list.size(); i++) {
			JSONObject json = new JSONObject();
			json.put("id_exchange", list.get(i).getId());
			if(list.get(i).getIdReceiver()==idGameUser) {
				GameUser gu = gameUserDao.findById(list.get(i).getIdSender());
				json.put("first_name", gu.getUser().getFirstName());
				json.put("last_name", gu.getUser().getLastName());
				json.put("game_name", gu.getGame().getGamep().getName());
				json.put("platform", gu.getGame().getPlatform().getName());
			}else {
			GameUser gu = gameUserDao.findById(list.get(i).getIdReceiver());
			json.put("first_name", gu.getUser().getFirstName());
			json.put("last_name", gu.getUser().getLastName());
			json.put("game_name", gu.getGame().getGamep().getName());
			json.put("platform", gu.getGame().getPlatform().getName());
			}
			String DateRe = list.get(i).getDateReturn();
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date d = new Date();
				String currentDate = formatter.format(d);
				if ((currentDate.compareTo(DateRe) > 0) && (list.get(i).getReturnOk()==0) ){
					json.put("retard", "YES");
					json.put("date_return", DateRe);
				}else {
					json.put("retard", "NO");
					json.put("date_return", DateRe);
				}
			} catch (Exception e) {
				return ServicesTools.serviceRefused("mauvais format de date",ErrorTools.WRONG_FORMAT);
			}
			
			res=json;
		}
		return res;
		
	}

	public static JSONObject getUserExchanges(String token, String iduser) throws JSONException{
		if(token == null || iduser == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}
		int id = 0;
		try {
			id = Integer.parseInt(iduser);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entr�e",ErrorTools.WRONG_FORMAT);
		}
		
		UserDAO userDao = new UserDAO(User.class, HibernateUtility.getSessionFactory());
		User u= userDao.findById(id);
		if(u == null){
			return ServicesTools.serviceRefused("User n'existe pas",ErrorTools.USER_NOT_FOUND);
		}
		List<GameUser> listgu = gameUserDao.getListGameUser(u.getId());
		if (listgu.isEmpty()) {
			JSONObject js=new JSONObject();
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(new JSONObject());
			js.put("exchanges",jsonArray);
			return js;
		}
		JSONObject res = new JSONObject();
		
		for ( GameUser gUser : listgu ) {
			res.accumulate("exchanges", getStatitisticsExchanges(gUser.getId()));
		}
		
		JSONObject jsonObject= new JSONObject();
		JSONArray jsArray= res.optJSONArray("exchanges");
		if (jsArray==null) {
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(res.get("exchanges"));
			jsonObject.put("exchanges",jsonArray);
			return jsonObject;
		} else {
			return res;
		}
	} 


	public static JSONObject getListExchanges(String token, String title) throws JSONException{
		if(token == null || title == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}
		
		int idUser = usDao.getIdUserByToken(token);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject res = new JSONObject();
		
		List<Game> listGames = gameDao.getGameByName(title);
		List<GameUser> gameUsers = null;
		List<GamePlatform> listGamesPlatform = null;
		for(Game g: listGames){
			listGamesPlatform= gamePlatformDao.getGamePlatformsByIdGame(g.getId());
			for(GamePlatform gp: listGamesPlatform){
				gameUsers = gameUserDao.getListUsersGame(gp.getId());
				for (GameUser gu: gameUsers) {
					if(gu.getUser().getId() != idUser && gu.getRelation() == 1){
						json.put("game_name", gu.getGame().getGamep().getName());
						json.put("id_game_user", gu.getId());
						json.put("first_name", gu.getUser().getFirstName());
						json.put("last_name", gu.getUser().getLastName());
						json.put("adress", gu.getUser().getAddress());
						json.put("platform_name", gu.getGame().getPlatform().getName());
						json.put("cover", gu.getGame().getGamep().getCover());
						json.put("summary", gu.getGame().getGamep().getSummary());
						json.put("id_user", gu.getUser().getId());
						array.put(json);
					}				
				}
			}
		}
		res.put("games_suggested", array);
		return res;
	}
	

}
