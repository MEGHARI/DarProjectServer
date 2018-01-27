package com.dar.hibernate.test;

import org.json.JSONObject;

import com.dar.services.ExchangeServices;
import com.dar.services.GameServices;

public class TestGeneral {

	public static void main(String[] args) throws Exception {
		
//		UserSessionDAO sessionDao = new UserSessionDAO(UserSession.class, HibernateUtility.getSessionFactory());
//		System.out.println(UserServices.logout("544f7455-7986-4cea-861a-d5b77b351dd7"));
		
//		
	JSONObject jsonObject = new JSONObject();
	//jsonObject=UserServices.createUser("Mouloud", "HAbchi", "31 Boulevard Gallieni, Neuilly-Plaisance, France 93360", "mouloud@gmail.com", "HK2018");
	//jsonObject=UserServices.createUser("Aghiles", "MEGHARI", "6 rue jussieu, Paris, France 75005", "aghiles@gmail.com", "HK2018");
	
//	jsonObject=GameServices.add("28204");
	
//	jsonObject=GameUserServices.create("43988033-e59e-4c54-bcbf-27a8318532f2", "35", "1");
//	jsonObject=GameUserServices.create("43988033-e59e-4c54-bcbf-27a8318532f2", "36", "1");
//	
//	jsonObject=GameServices.getListUsersGame("c8b5931d-69f8-463f-a06e-941b89a52a50", "7");
	
//	jsonObject=UserServices.getListAdmins("8604d30c-c55d-4c2a-b1c0-f5b353cf929a");
	
//	jsonObject=UserServices.getUsersBanned("8604d30c-c55d-4c2a-b1c0-f5b353cf929a");

//	jsonObject=GameUserServices.listGameLikePocess("8604d30c-c55d-4c2a-b1c0-f5b353cf929a");
	
//	jsonObject=GameServices.getPlateformsOfGame("8604d30c-c55d-4c2a-b1c0-f5b353cf929a", "697");
	
	
//	jsonObject=ExchangeServices.create("43988033-e59e-4c54-bcbf-27a8318532f2", "7", "6", "23-11-2017", "29-12-2017");

	
	jsonObject=ExchangeServices.getUserExchanges("43988033-e59e-4c54-bcbf-27a8318532f2", "35");
	System.out.println(jsonObject);
//	jsonObject=GameUserServices.listGamePocess("8604d30c-c55d-4c2a-b1c0-f5b353cf929a");
	
//	jsonObject=UserServices.updateUser("8604d30c-c55d-4c2a-b1c0-f5b353cf929a", "mou", "habchi", "Tizi", "Habchi@gmail.com", "KASDI202020", "urlPicture.png");
//		System.out.println(jsonObject);
		
		
//		System.out.println(GameUserServices.listGames("43988033-e59e-4c54-bcbf-27a8318532f2", "1"));
		


		//System.out.println(UserServices.updateUser("43988033-e59e-4c54-bcbf-27a8318532f2", "lastName", null, null, null, null));

//		MessageDAO m = new MessageDAO();
//		m.removeAll();
//		m.send(20, 22, "hello");
//		m.send(20, 22, "hello");
//		m.send(20, 22, "hello");
//		m.send(20, 22, "hello");
//		m.send(21, 22, "hello");
//		m.send(21, 22, "hello", 0, 0);
//		m.send(29, 22, "hello");
//		m.send(0, 22, "hello");
//		m.send(0, 22, "Salim SMAIL vous propose un échange de jeu. il vous propose son : FIFA 18."
//				+ " Contre votre : PES 2018. Son adresse : 33 Boulevard Gallieni, Neuilly Plaisance, 93360 France ", 1, 6);
//		m.send(22, 20, "zebi");

//		System.out.println(MessageServices.getList("0", "43988033-e59e-4c54-bcbf-27a8318532f2", "1"));	
		
//		System.out.println(ExchangeServices.create("43988033-e59e-4c54-bcbf-27a8318532f2", "6", "7", "22-12-2017", "24-12-2017"));
//		System.out.println(GameServices.getListUsersGame("43988033-e59e-4c54-bcbf-27a8318532f2", "34"));
	
//	System.out.println(GameServices.getListGames("43988033-e59e-4c54-bcbf-27a8318532f2").getJSONArray("games").length());
	}

}
