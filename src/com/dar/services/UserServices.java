package com.dar.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dar.hibernate.dao.UserDAO;
import com.dar.hibernate.dao.UserSessionDAO;
import com.dar.hibernate.entity.User;
import com.dar.hibernate.entity.UserSession;
import com.dar.hibernate.utility.HibernateUtility;
import com.dar.services.subservices.gmail.MailService;
import com.dar.tools.ErrorTools;

/**
 * Cette classe <code>UserServices</code> fournit tous les services necéssaires pour l'inscription, la connexion
 * la déconnexion, et tous les services de gestions d'un utilisateurs
 * 
 * 
 * @author GamesXchanges Team
 *
 */
public class UserServices {

	private static UserDAO userDao = new UserDAO(User.class, HibernateUtility.getSessionFactory());
	private static UserSessionDAO usDao = new UserSessionDAO(UserSession.class, HibernateUtility.getSessionFactory());

	/**
	 * Ce service permet à un utilisateur de s'inscrire
	 * @param lastName : le nom d'utilisateur  
	 * @param firstName : le prenom de l'utilisateur
	 * @param address : L'adresse de l'utilisateur
	 * @param mail : l'email de l'utilisateur
	 * @param password : le mot de passe de l'utilisateur
	 * @return envoie un code de confirmation par mail et renvoie un message si tous se passe bien
	 * @throws JSONException
	 */
	public static JSONObject createUser(String lastName, String firstName, String address, String mail,
			String password) throws JSONException{

		JSONObject error = new JSONObject();

		if(lastName == null || !lastName.matches("[a-zA-Z]{2,}")){
			error.accumulate("errors", new JSONObject().put("code", 1).put("message", "invalide"));
		}
		if(firstName == null || !firstName.matches("[a-zA-Z]{2,}")){
			error.accumulate("errors", new JSONObject().put("code", 2).put("message", "invalide"));
		}
		if(address == null){
			error.accumulate("errors", new JSONObject().put("code", 3).put("message", "invalide"));
		}
		if(mail == null || !mail.matches("^[A-Za-z][A-Za-z0-9._-]+@[A-Za-z0-9-_]+\\.?[A-Za-z0-9-_]+\\.[A-Za-z]{2,6}$")){
			error.accumulate("errors", new JSONObject().put("code", 4).put("message", "invalide"));
		}
		if(password == null || !password.matches(".{6,}")){
			error.accumulate("errors", new JSONObject().put("code", 5).put("message", "invalide"));
		}

		if(error.length()!=0){
			return error;
		}

		User user = userDao.getUserByMailRole(mail, "0");
		if(user!=null){
			return ServicesTools.serviceRefused("compte utilisateur existe deja", ErrorTools.EXIST_ACCOUNT);
		}
		String url_picture="Avatar.png";
		String code = UserServices.generateCode();
		try{
			userDao.create(new User(lastName, firstName, address, mail, password, 0, code, url_picture));
		}
		catch (Exception e){
			return ServicesTools.serviceRefused("erreur base de donn�e", ErrorTools.BDD_ERROR);
		}
		return MailService.sendCodeConfirmation(mail, code, lastName);
	}

	/**
	 * Permet de récupérer les informations de l'utilisateur grace à facebook
	 * @param lastName : le nom d'utilisateur  
	 * @param firstName : le prenom de l'utilisateur
	 * @param address : L'adresse de l'utilisateur
	 * @param mail : l'email de l'utilisateur
	 * @param password : le mot de passe de l'utilisateur
	 * @param urlPicture : photo de profil
	 * @return envoie un code de confirmation par mail et renvoie un message si tous se passe bien
	 * @throws JSONException
	 */
	public static JSONObject createUserWithFacebook(String lastName, String firstName, String address, String mail,
			String password,String urlPicture) throws JSONException{

		JSONObject error = new JSONObject();

		if(lastName == null || !lastName.matches("[a-zA-Z]{2,}")){
			error.accumulate("errors", new JSONObject().put("code", 1).put("message", "invalide"));
		}
		if(firstName == null || !firstName.matches("[a-zA-Z]{2,}")){
			error.accumulate("errors", new JSONObject().put("code", 2).put("message", "invalide"));
		}
		if(address == null){
			error.accumulate("errors", new JSONObject().put("code", 3).put("message", "invalide"));
		}
		if(mail == null || !mail.matches("^[A-Za-z][A-Za-z0-9._-]+@[A-Za-z0-9-_]+\\.?[A-Za-z0-9-_]+\\.[A-Za-z]{2,6}$")){
			error.accumulate("errors", new JSONObject().put("code", 4).put("message", "invalide"));
		}
		if(password == null || !password.matches(".{6,}")){
			error.accumulate("errors", new JSONObject().put("code", 5).put("message", "invalide"));
		}

		if(error.length()!=0){
			return error;
		}

		User user = userDao.getUserByMailRole(mail, "0");
		if(user!=null){
			return ServicesTools.serviceRefused("compte utilisateur existe deja", ErrorTools.EXIST_ACCOUNT);
		}
		String code = UserServices.generateCode();
		try{
			userDao.create(new User(lastName, firstName, address, mail, password, 0, code, urlPicture));
		}
		catch (Exception e){
			return ServicesTools.serviceRefused("erreur base de donn�e", ErrorTools.BDD_ERROR);
		}
		return MailService.sendCodeConfirmation(mail, code, lastName);
	}

	/**
	 * Permet à un administrateur de crée un autre administrateur
	 * @param token : le jeton qui permet d'identifier un administrateur connecté
	 * @param lastName : le nom d'utilisateur  
	 * @param firstName : le prenom de l'utilisateur
	 * @param address : L'adresse de l'utilisateur
	 * @param mail : l'email de l'utilisateur
	 * @param password : le mot de passe de l'utilisateur
	 * @return un message de confirmation
	 * @throws JSONException
	 */
	public static JSONObject createAdmin(String token, String lastName, String firstName, String address, String mail,
			String password) throws JSONException{


		JSONObject error = new JSONObject();
		if(token == null){
			return ServicesTools.serviceRefused("arguments manquants", ErrorTools.MISSED_ARGS);
		}
		if(lastName == null || !lastName.matches("[a-zA-Z]{2,}")){
			error.accumulate("errors", new JSONObject().put("code", 1).put("message", "invalide"));
		}
		if(firstName == null || !firstName.matches("[a-zA-Z]{2,}")){
			error.accumulate("errors", new JSONObject().put("code", 2).put("message", "invalide"));
		}
		if(address == null){
			error.accumulate("errors", new JSONObject().put("code", 3).put("message", "invalide"));
		}
		if(mail == null || !mail.matches("^[A-Za-z][A-Za-z0-9._-]+@[A-Za-z0-9-_]+\\.?[A-Za-z0-9-_]+\\.[A-Za-z]{2,6}$")){
			error.accumulate("errors", new JSONObject().put("code", 4).put("message", "invalide"));
		}
		if(password == null || !password.matches(".{6,}")){
			error.accumulate("errors", new JSONObject().put("code", 5).put("message", "invalide"));
		}

		if(error.length()!=0){
			return error;
		}

		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant",ErrorTools.WRONG_TOKEN);
		}

		if(userDao.getUserByMailRole(mail,"1")!=null){
			return ServicesTools.serviceRefused("compte admin existe deja", ErrorTools.EXIST_ACCOUNT);
		}
		try{
			String urlPictureAdmin="AvatarAdmin.png";
			userDao.create(new User(lastName, firstName, address, mail, password, 1, "",urlPictureAdmin));
		}
		catch (Exception e){
			return ServicesTools.serviceRefused("erreur base de donn�e", ErrorTools.BDD_ERROR);
		}
		return ServicesTools.serviceAccepted("admin crée avec succés");
	}	


	public static JSONObject updateUser(String token,String lastName, String firstName, String address,
			String password, String urlPicture) throws JSONException{

		if(token == null){
			return ServicesTools.serviceRefused("arguments manquants", ErrorTools.MISSED_ARGS);
		}
		if(!usDao.sessionExist(token)) {
			return ServicesTools.serviceRefused("Utilisateur non connecté !", ErrorTools.WRONG_TOKEN);
		}
		JSONObject error = new JSONObject();

		if(lastName != null && !lastName.equals("")){
			if(!lastName.matches("[a-zA-Z]{2,}"))
				error.accumulate("errors", new JSONObject().put("code", 1).put("message", "invalide"));
		}

		if(firstName != null && !firstName.equals("")){
			if(!firstName.matches("[a-zA-Z]{2,}"))
				error.accumulate("errors", new JSONObject().put("code", 2).put("message", "invalide"));
		}

		if(password != null && !password.equals("")){
			if(!password.matches(".{6,}"))
				error.accumulate("errors", new JSONObject().put("code", 5).put("message", "invalide"));
		}

		if(error.length()!=0)
			return error;

		try {

			int idU = usDao.getIdUserByToken(token);	
			User userToUpdate = userDao.findById(idU);
			if(lastName!=null && !lastName.equals(""))userToUpdate.setLastName(lastName);
			if(firstName!=null && !firstName.equals(""))userToUpdate.setFirstName(firstName);
			if(address!=null && !address.equals(""))userToUpdate.setAddress(address);
			if(password!=null && !password.equals(""))userToUpdate.setPassword(password);
			if(urlPicture!=null && !urlPicture.equals(""))userToUpdate.setUrlPicture(urlPicture);

			userDao.update(userToUpdate);

			JSONObject json= new JSONObject();
			json.put("id", userToUpdate.getId());
			json.put("last_name", userToUpdate.getLastName());
			json.put("first_name", userToUpdate.getFirstName());
			json.put("address", userToUpdate.getAddress());
			json.put("mail", userToUpdate.getMail());
			json.put("statut", userToUpdate.getStatut());
			json.put("token", token);
			json.put("url_picture", userToUpdate.getUrlPicture());
			return json;

		} catch (Exception e) {
			return ServicesTools.serviceRefused("Erreur base de donnée", ErrorTools.USER_NOT_FOUND);
		}
	}

	public static JSONObject login(String mail, String password) throws JSONException{

		if(mail == null || password == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		if(!userDao.checkPassword(mail, password)){
			return ServicesTools.serviceRefused("mail ou mot de passe incorrect",ErrorTools.MAIL_PASSWORD_ERROR);
		}

		User user = userDao.getUserByMail(mail);
		if(user.getStatut()==-1){
			return ServicesTools.serviceRefused("utilisateur bannie",ErrorTools.USER_BANNED);
		}
		if(user.getStatut()==0){
			return ServicesTools.serviceRefused("l'utilisateur doit confirmer l'inscription", ErrorTools.TO_CONFIRM);
		}

		String token = usDao.create(user.getId());
		JSONObject json= new JSONObject();
		json.put("id", user.getId());
		json.put("last_name", user.getLastName());
		json.put("first_name", user.getFirstName());
		json.put("address", user.getAddress());
		json.put("mail", user.getMail());
		json.put("statut", user.getStatut());
		json.put("token", token);
		json.put("url_picture", user.getUrlPicture());
		return json;

	}

	public static JSONObject logout(String token) throws JSONException{
		if(token == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		if(!usDao.sessionExist(token)){
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		}
		usDao.delete(token);
		return ServicesTools.serviceAccepted("deconnection réussit");
	}

	public static JSONObject bannedUser(String token, String id) throws JSONException{
		if(id == null || token == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		int idUser = 0;
		try {
			idUser = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}

		User admin = userDao.findById(usDao.getIdUserByToken(token));
		if(admin.getId()==idUser){
			return ServicesTools.serviceRefused("action impossible", ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(admin.getRole()!=1){
			return ServicesTools.serviceRefused("accés non autorisé", ErrorTools.UNAUTHORIZED_ACTION);
		}
		User user = userDao.findById(idUser);
		if(user.getStatut() == -1){
			return ServicesTools.serviceRefused("utilisateur déja bannie", ErrorTools.UNAUTHORIZED_ACTION);
		}
		user.setStatut(-1);
		userDao.update(user);
		return ServicesTools.serviceAccepted("utilisateur bannie avec succés");
	}

	public static JSONObject unbannedUser(String token, String id) throws JSONException{
		if(id == null || token == null){
			return ServicesTools.serviceRefused("argument manquant", ErrorTools.MISSED_ARGS);
		}
		int idUser = 0;
		try {
			idUser = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return ServicesTools.serviceRefused("mauvais format de l'entrée",ErrorTools.WRONG_FORMAT);
		}

		User admin = userDao.findById(usDao.getIdUserByToken(token));
		if(admin.getId()==idUser){
			return ServicesTools.serviceRefused("action impossible sur l'admin",ErrorTools.UNAUTHORIZED_ACTION);
		}
		if(admin.getRole()!=1){
			return ServicesTools.serviceRefused("acc�s non autorisé", ErrorTools.UNAUTHORIZED_ACTION);
		}
		User user = userDao.findById(idUser);
		if(user.getStatut() == 1){
			return ServicesTools.serviceRefused("utilisateur déja activé", ErrorTools.UNAUTHORIZED_ACTION);
		}
		user.setStatut(1);
		userDao.update(user);
		return ServicesTools.serviceAccepted("utilisateur activ� avec succés");		
	}

	public static JSONObject passwordForgotten(String mail) throws JSONException{
		if(mail == null)
			return ServicesTools.serviceRefused("argument manquant", ErrorTools.MISSED_ARGS);

		User user = userDao.getUserByMail(mail);
		if(user == null){
			return ServicesTools.serviceRefused("utilisateur innexistant", ErrorTools.WRONG_MAIL);
		}
		if(user.getStatut() == -1 )
			return ServicesTools.serviceRefused("utilisateur bannie", ErrorTools.UNAUTHORIZED_ACTION);

		String password = user.getPassword();
		String userName = user.getFirstName();
		return MailService.recoverUserPassword(mail, userName, password);

	}

	private static String generateCode(){
		String code ="";
		String chars = "abc#defghijklm{n-o}p)qrstuvwxyzABCDEFGHIJKL+=MNOP/Q|RSTU>VWXYZ<12345!67?89:0;";
		for (int i = 0; i < 10; i++) {
			int j = (int)Math.floor(Math.random() * 62);
			code += chars.charAt(j);
		}
		return code;
	}

	public static JSONObject confirmeSubscription(String mail, String code, String password) throws JSONException{
		if(mail == null || code == null || password == null){
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		}
		if (!userDao.checkPassword(mail, password)){
			return ServicesTools.serviceRefused("Mail ou mot de passe incorrect", ErrorTools.MAIL_PASSWORD_ERROR);
		}

		User user = userDao.getUserByMail(mail);
		if (!user.getCodeConfirme().equals(code)){
			return ServicesTools.serviceRefused("Mauvais code", ErrorTools.WRONG_CODE);
		}
		if(user.getStatut() != 0){
			return ServicesTools.serviceRefused("Utilisateur à déja confirmé son inscription", ErrorTools.WRONG_CODE);
		}
		user.setStatut(1);
		userDao.update(user);
		String token = usDao.create(user.getId());
		JSONObject json= new JSONObject();
		json.put("id", user.getId());
		json.put("last_name", user.getLastName());
		json.put("first_name", user.getFirstName());
		json.put("address", user.getAddress());
		json.put("mail", user.getMail());
		json.put("statut", user.getStatut());
		json.put("token", token);
		json.put("url_picture", user.getUrlPicture());
		return json;
	}

	public static JSONObject getListUsers(String token) throws JSONException{
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
		List<User> users = userDao.findAll();

		JSONObject res = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();

		for(User u:users){
			json = new JSONObject();
			json.put("id", u.getId());
			json.put("last_name", u.getLastName());
			json.put("first_name", u.getFirstName());
			json.put("address", u.getAddress());
			json.put("mail", u.getMail());
			json.put("statut", u.getStatut());
			json.put("role", u.getRole());
			json.put("statut", u.getStatut());
			json.put("url_picture", u.getUrlPicture());
			array.put(json);
		}

		res.put("users", array);
		return res;
	}

	public static JSONObject getListAdmins(String token) throws JSONException {
		if(token == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		if(!usDao.sessionExist(token))
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		int id = usDao.getIdUserByToken(token);
		User admin = userDao.findById(id);

		if(admin.getRole() != 1){
			return ServicesTools.serviceRefused("action non autorisée", ErrorTools.UNAUTHORIZED_ACTION);
		}
		JSONObject res = new JSONObject();
		JSONArray array = new JSONArray();
		List<User> admins = userDao.getListAdmins();
		for (User adm : admins) {
			JSONObject json = new JSONObject();
			json.put("id_admin", adm.getId());
			json.put("first_name", adm.getFirstName());
			json.put("last_name", adm.getLastName());
			json.put("address", adm.getAddress());
			json.put("email", adm.getMail());
			json.put("url_picture", adm.getUrlPicture());
			json.put("statut", adm.getStatut());
			array.put(json);
		}
		res.put("admins", array);
		return res;
	}

	public static JSONObject getUsersBanned(String token) throws JSONException {
		if(token == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		if(!usDao.sessionExist(token))
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		int id = usDao.getIdUserByToken(token);
		User admin = userDao.findById(id);

		if(admin.getRole() != 1){
			return ServicesTools.serviceRefused("action non autorisée", ErrorTools.UNAUTHORIZED_ACTION);
		}
		JSONObject res = new JSONObject();
		JSONArray array = new JSONArray();
		List<User> listUsers = userDao.findAll();
		for(User user : listUsers) {
			if(user.getStatut()==(-1)) {
				JSONObject json = new JSONObject();
				json.put("id_admin", user.getId());
				json.put("first_nme", user.getFirstName());
				json.put("last_name", user.getLastName());
				json.put("address", user.getAddress());
				json.put("email", user.getMail());
				json.put("url_picture", user.getUrlPicture());
				array.put(json);
			}
		}

		res.put("usersBanned", array);

		return res;
	}

	public static JSONObject checkOldPassword(String token, String oldPass) throws JSONException{
		if(token == null)
			return ServicesTools.serviceRefused("token manquant",ErrorTools.MISSED_ARGS);
		if(oldPass == null)
			return ServicesTools.serviceRefused("argument manquant",ErrorTools.MISSED_ARGS);
		if(!usDao.sessionExist(token))
			return ServicesTools.serviceRefused("token innexistant", ErrorTools.WRONG_TOKEN);
		int id = usDao.getIdUserByToken(token);
		User user = userDao.findById(id);

		JSONObject res=new JSONObject();
		if (user.getPassword().equals(oldPass))
			return res.put("Equivalent", "TRUE");
		return res.put("Equivalent", "FALSE");
	}


}