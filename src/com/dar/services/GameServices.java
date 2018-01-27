package com.dar.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dar.hibernate.dao.GameDAO;
import com.dar.hibernate.dao.GamePlatformDAO;
import com.dar.hibernate.dao.GameUserDAO;
import com.dar.hibernate.dao.PlatformDAO;
import com.dar.hibernate.dao.UserDAO;
import com.dar.hibernate.dao.UserSessionDAO;
import com.dar.hibernate.entity.Game;
import com.dar.hibernate.entity.GamePlatform;
import com.dar.hibernate.entity.GameUser;
import com.dar.hibernate.entity.Platform;
import com.dar.hibernate.entity.User;
import com.dar.hibernate.entity.UserSession;
import com.dar.hibernate.utility.HibernateUtility;
import com.dar.tools.ConfigTools;
import com.dar.tools.ErrorTools;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Cette classe <code>GameServices</code> fournit tous les services necéssaire pour ajouter, rechercher et récupèrer la liste 
 * de plateformes de chaque jeu récupèré depuis l'API
 * 
 * 
 * @author GamesXchanges Team
 *
 */
public class GameServices {

	private static UserDAO userDao = new UserDAO(User.class, HibernateUtility.getSessionFactory());
	private static GameDAO gameDao = new GameDAO(Game.class, HibernateUtility.getSessionFactory());
	private static GameUserDAO gameUserDao = new GameUserDAO(GameUser.class, HibernateUtility.getSessionFactory());
	private static UserSessionDAO usDao = new UserSessionDAO(UserSession.class, HibernateUtility.getSessionFactory());
	private static GamePlatformDAO gamePlatformDao = new GamePlatformDAO(GamePlatform.class, HibernateUtility.getSessionFactory());
	private static PlatformDAO platformDao = new PlatformDAO(Platform.class, HibernateUtility.getSessionFactory());

	/**
	 * 
	 * @param idGame : identifiant d'un jeu qu'on va récupèrer depuis l'API @see <a href="https://www.igdb.com/api">IGDB</a>
	 * @return un message sera retourné sous format JSONObject
	 * @throws JSONException
	 * @throws UnirestException
	 */
	public static JSONObject add(String idGame)throws JSONException, UnirestException{
		if(idGame==null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		int id; 
		try{
			id = Integer.parseInt(idGame);	
		}catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entr�e",ErrorTools.WRONG_FORMAT);
		}
		if(gameDao.GameExistByIdApi(id)){
			return ServicesTools.serviceAccepted("aucun contenue");
		}

		HttpResponse<JsonNode> response;
		response = Unirest.get("https://api-2445582011268.apicast.io/games/"+id)
				.header("user-key", ConfigTools.API_IGDB_KEY)
				.header("Accept", "application/json")
				.asJson();
		if(response.getBody().getArray().isNull(0)){
			return ServicesTools.serviceRefused("Le jeu n'existe pas",ErrorTools.EMPTY_GAME);
		}

		JSONObject json = response.getBody().getArray().getJSONObject(0);

		String name = json.getString("name");
		String summary = json.optString("summary", "emptry");
		String releaseDate = json.optString("first_release_date", "empty");

		String cover;
		if(json.has("cover")){
			JSONObject jsonCover = json.getJSONObject("cover");
			cover = jsonCover.getString("url");
		}else{
			cover = "empty";
		}

		String genre;
		if(json.has("genres")){
			genre =  json.getJSONArray("genres").getString(0);
			response = Unirest.get("https://api-2445582011268.apicast.io/genres/"+genre)
					.header("user-key", ConfigTools.API_IGDB_KEY)
					.header("Accept", "application/json")
					.asJson();
			JSONObject json2 = response.getBody().getArray().getJSONObject(0);
			genre = json2.getString("name");

		}else{
			genre = "empty";
		}

		int idg;
		try {
			idg = gameDao.create(new Game(id, name, summary, releaseDate, cover, genre));
		} catch (Exception e1) {
			return ServicesTools.serviceRefused("erreur base de donn�e", ErrorTools.BDD_ERROR);
		}
		JSONArray platforms;
		try {
			platforms = json.getJSONArray("platforms");
			for(int i=0; i<platforms.length(); i++){
				createGamePlatform(idg, platforms.getInt(i));
			}
		} catch (JSONException e) {
			return ServicesTools.serviceRefused("Erreur platformes", ErrorTools.PLATFORM_ERROR);
		}
		return ServicesTools.serviceAccepted("le jeu est stock� dans la base de donn�e");
	}

	/**
	 * 
	 * @param id : identifiant du jeu de l'API
	 * @param idPlatform : identifiant de la plateforme de jeu 
	 * @return un message sera retourné sous format JSONObject
	 * @throws JSONException
	 */
	private static JSONObject createGamePlatform(int id, int idPlatform) throws JSONException{
		Game game = gameDao.findById(id);
		Platform platform = platformDao.getByApiId(idPlatform);
		try {
			gamePlatformDao.create(new GamePlatform(game, platform));
		} catch (Exception e) {
			return ServicesTools.serviceRefused("erreur base de donn�e", ErrorTools.BDD_ERROR);
		}
		return ServicesTools.serviceAccepted("Game platform jout�e avec succ�es");
	}

	/**
	 * 
	 * @param token
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getListGames(String token) throws JSONException{
		if(token == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}
		int id = usDao.getIdUserByToken(token);
		User admin = userDao.findById(id);

		if(admin.getRole() != 1){
			return ServicesTools.serviceRefused("action non autorisé", ErrorTools.UNAUTHORIZED_ACTION);
		}
		List<GamePlatform> games = gamePlatformDao.findAll();

		JSONObject res = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray array1 = new JSONArray();
		for(GamePlatform g:games){
			if(!g.getGameUsers().isEmpty()){
				json = new JSONObject();
				json.put("id", g.getId());
				json.put("game_name", g.getGamep().getName());
				json.put("summary", g.getGamep().getSummary());
				json.put("date_release", g.getGamep().getReleaseDate());
				json.put("genre", g.getGamep().getGenre());
				json.put("cover", g.getGamep().getCover());
				json.put("platform_name", g.getPlatform().getName());
				array1.put(json);
			}
		}

		res.put("games", array1);

		return res;
	}

	/**
	 * 
	 * @param token : Le token de l'utilsateur connecté
	 * @param idApiGame: identifiant du jeu 
	 * @return la liste des Utilisateurs qui possèdent le jeu qui a l'identifiant <code>IdApiGame</code> 
	 * @throws JSONException
	 */
	public static JSONObject getListUsersGame(String token, String idGamePlatform) throws JSONException {
		if(idGamePlatform==null || token == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		if(!usDao.sessionExist(token))
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		int id; 
		try{
			id = Integer.parseInt(idGamePlatform);
		}catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}

		JSONObject res = new JSONObject();
		GamePlatform gamePlatform = gamePlatformDao.findById(id);
		JSONArray array = new JSONArray();
		List<GameUser> listUsersGame = gameUserDao.getListUsersGame(gamePlatform.getId());
		for (int j = 0; j < listUsersGame.size(); j++) {
			JSONObject json = new JSONObject();
			json.put("id", listUsersGame.get(j).getUser().getId());
			json.put("first_name", listUsersGame.get(j).getUser().getFirstName());
			json.put("last_name", listUsersGame.get(j).getUser().getLastName());
			json.put("address", listUsersGame.get(j).getUser().getAddress());
			json.put("mail", listUsersGame.get(j).getUser().getMail());
			json.put("url_picture", listUsersGame.get(j).getUser().getUrlPicture());
			json.put("role", listUsersGame.get(j).getUser().getRole());
			json.put("statut", listUsersGame.get(j).getUser().getStatut());

			array.put(json);
		}

		res.put("users_of_game", array);

		return res;
	}

	/**
	 * 
	 * @param title : intitulé du jeu à rechercher
	 * @param offset : offset de la page
	 * @return un jeu sous format JSON
	 * @throws JSONException
	 * @throws UnirestException
	 */
	public static JSONObject searchGameInAPI(String title, String offset)throws JSONException, UnirestException{

		if(title == null || offset == null) return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);

		try{
			Integer.parseInt(offset);	
		}catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entr�e",ErrorTools.WRONG_FORMAT);
		}

		HttpResponse<JsonNode> response;
		response = Unirest.get("https://api-2445582011268.apicast.io/games/?search="+title+"&fields=name,summary,first_release_date,cover&offset="+offset)
				.header("user-key", ConfigTools.API_IGDB_KEY)
				.header("Accept", "application/json")
				.asJson();
		JSONObject result = new JSONObject();
		result.put("games", response.getBody().getArray());
		return result;
	}

	/**
	 * 
	 * @param title : titre de jeu à rechercher dans l'API
	 * @return un jeu sous format JSON
	 * @throws JSONException
	 * @throws UnirestException
	 */
	public static JSONObject searchGameAutoComplete(String title)throws JSONException, UnirestException{

		if(title == null) return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);

		HttpResponse<JsonNode> response;
		response = Unirest.get("https://api-2445582011268.apicast.io/games/?search="+title+"&fields=name&offset=0")
				.header("user-key", ConfigTools.API_IGDB_KEY)
				.header("Accept", "application/json")
				.asJson();
		JSONObject result = new JSONObject();
		result.put("games", response.getBody().getArray());

		return result;
	}



	/**
	 * 
	 * @param token :Le token de l'utilsateur connecté
	 * @param idApiGame :Identifiant d'un jeu 
	 * @return la liste de plateformes d'un jeu qui comme identifiant idApiGame
	 * @throws JSONException
	 * @throws UnirestException
	 */
	public static JSONObject getPlatformsOfGame(String token, String idApiGame) throws JSONException, UnirestException{
		if(idApiGame==null || token == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		if(!usDao.sessionExist(token))
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		int idGame; 
		try{
			idGame=Integer.parseInt(idApiGame);
		}catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entr�e",ErrorTools.WRONG_FORMAT);
		}
		add(idApiGame);
		JSONObject res = new JSONObject();
		int idUser = usDao.getIdUserByToken(token);
		List<GameUser> listGameUser = gameUserDao.getListGameUser(idUser);
		int idGameDB=gameDao.GetIdGameByIdApi(idGame);

		List<GamePlatform> listPlatform = gamePlatformDao.getGamePlatformsByIdGame(idGameDB);

		boolean trouve;
		for (int i = 0; i < listPlatform.size(); i++) {
			trouve=false;
			for (int j = 0; j < listGameUser.size(); j++) {

				if (listGameUser.get(j).getGame().getGamep().getId()==listPlatform.get(i).getGamep().getId()) {
					if (listGameUser.get(j).getGame().getId() == listPlatform.get(i).getId()) {

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("id_game", listGameUser.get(j).getGame().getGamep().getId());
						jsonObject.put("id_platform", listPlatform.get(i).getId());
						jsonObject.put("name_game", listGameUser.get(j).getGame().getGamep().getName());
						jsonObject.put("name_platform", listPlatform.get(i).getPlatform().getName());
						jsonObject.put("relation", listGameUser.get(j).getRelation());	
						res.accumulate("platforms",jsonObject);
						trouve=true;
					}
				}
			}
			if(!trouve) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id_game", listPlatform.get(i).getGamep().getId());
				jsonObject.put("id_platform", listPlatform.get(i).getId());
				jsonObject.put("name_game", listPlatform.get(i).getGamep().getName());
				jsonObject.put("name_platform", listPlatform.get(i).getPlatform().getName());
				jsonObject.put("relation", 0);	
				res.accumulate("platforms",jsonObject);				
			}
		}			

		JSONObject jsonObject= new JSONObject();
		JSONArray jsArray= res.optJSONArray("platforms");
		if (jsArray==null) {
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(res.get("platforms"));
			jsonObject.put("platforms",jsonArray);
			return jsonObject;
		} else {
			return res;
		}
	}


}
