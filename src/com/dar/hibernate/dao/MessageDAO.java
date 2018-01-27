package com.dar.hibernate.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dar.bd.DBStatic;
import com.dar.hibernate.entity.User;
import com.dar.hibernate.utility.HibernateUtility;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MessageDAO {
	private final String NAME_COLLECTION = "messages";

	public JSONObject send(int idSender, int idReceiver, String message, int type, int idExchange) throws JSONException{
		MongoClient m = new MongoClient(DBStatic.mongoDB_host,DBStatic.mongoDB_port);

		DB db = m.getDB(DBStatic.mysql_db);
		DBCollection col = db.getCollection(NAME_COLLECTION);
		BasicDBObject obj = new BasicDBObject();

		obj.put("id_sender", idSender);
		obj.put("id_receiver", idReceiver);
		obj.put("date", new Date().getTime());
		obj.put("message", message);
		obj.put("show", 0);
		if(idSender==0){
			obj.put("type", type);
			obj.put("id_exchange", idExchange);
			System.out.println("ID EXC "+idExchange);
			if(type==1){
				obj.put("statut", 0);
			}
		}

		col.insert(obj);
		JSONObject json = new JSONObject();
		json.put("res", obj);
		m.close();

		return json.getJSONObject("res");
	}

	public boolean messageExist(String id){
		MongoClient m =  new MongoClient(DBStatic.mongoDB_host,DBStatic.mongoDB_port);

		DB db = m.getDB(DBStatic.mysql_db);
		DBCollection col = db.getCollection(NAME_COLLECTION);

		BasicDBObject query= new BasicDBObject();
		query.put("_id", new ObjectId(id));
		DBObject res = col.findOne(query);	
		m.close();
		return res!=null;
	}

	public JSONObject getList(int idSender, int idReceiver, int page) throws JSONException {
		MongoClient m = new MongoClient(DBStatic.mongoDB_host,DBStatic.mongoDB_port);
		@SuppressWarnings("deprecation")
		DB db = m.getDB(DBStatic.mysql_db);
		DBCollection col = db.getCollection(NAME_COLLECTION);

		BasicDBObject obj = new BasicDBObject();
		obj.put("date", -1);

		BasicDBObject obj1 = new BasicDBObject();
		BasicDBObject obj2 = new BasicDBObject();

		obj1.put("id_sender", idSender);
		obj1.put("id_receiver", idReceiver);

		obj2.put("id_sender", idReceiver);
		obj2.put("id_receiver", idSender);

		BasicDBObject orQuery = new BasicDBObject();
		List<BasicDBObject> listObj = new ArrayList<BasicDBObject>();
		listObj.add(obj1);
		listObj.add(obj2);
		orQuery.put("$or", listObj);

		DBCursor cursor = col.find(orQuery).sort(obj);

		UserDAO userDao = new UserDAO(User.class, HibernateUtility.getSessionFactory());

		String pic_sender = null;
		if(idSender!=0) pic_sender = userDao.findById(idSender).getUrlPicture();
		String pic_receiver = userDao.findById(idReceiver).getUrlPicture();

		int nbMsgs = 0;
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		while(cursor.hasNext()){
			nbMsgs++;
			DBObject res= cursor.next();
			if(nbMsgs>((page*5)-5) && nbMsgs<=(page*5)){
				array.put(res);
				if(idSender !=0)
					if(idSender == (int)res.get("id_sender")) res.put("url_picture", pic_sender);
					else res.put("url_picture", pic_receiver);

				if(nbMsgs>(page*5))
					break;
			}
		}
		showMessage(idSender, idReceiver);
		cursor.close();
		m.close();
		json.put("messages", array);
		return json;

	}

	private void showMessage(int idSender, int idReceiver){
		MongoClient m =  new MongoClient(DBStatic.mongoDB_host,DBStatic.mongoDB_port);

		DB db = m.getDB(DBStatic.mysql_db);
		DBCollection col = db.getCollection(NAME_COLLECTION);

		BasicDBObject query= new BasicDBObject();
		query.put("id_sender", idSender);
		query.put("id_receiver", idReceiver);

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("show", 1));

		BasicDBObject searchQuery = new BasicDBObject().append("id_sender", idSender).append("id_receiver", idReceiver);

		col.updateMulti(searchQuery, newDocument);

		m.close();
	}

	public JSONObject getNoShowingConversations(int idReceiver) throws JSONException{

		AggregationOutput output = getAllConversations("id_receiver", idReceiver);	

		UserDAO userDao = new UserDAO(User.class, HibernateUtility.getSessionFactory());
		JSONArray array = new JSONArray(output.results().toString());
		JSONObject json = new JSONObject();
		JSONObject res = new JSONObject();
		JSONArray a = new JSONArray();

		Set<Integer> set = new HashSet<>();
		set = getIds(idReceiver);

		for(int i=0; i<array.length(); i++){
			json = array.getJSONObject(i);
			if(json.getInt("count")!=0){
				if(json.getInt("_id")!=0){
					User user = userDao.findById(json.getInt("_id"));
					json.put("lastName", user.getLastName());
					json.put("firstName", user.getFirstName());
					json.put("url_picture", user.getUrlPicture());
					a.put(json);
					if(set.contains(json.getInt("_id"))){
						set.remove(json.getInt("_id"));
					}
				} else{
					a.put(json);
				}

			}

		}
		Iterator it = set.iterator();
		while(it.hasNext()){
			json = new JSONObject();
			int idd = (int) it.next();
			if(idd!=0){
				json.put("_id", idd);
				User user = userDao.findById(idd);
				json.put("lastName", user.getLastName());
				json.put("firstName", user.getFirstName());
				json.put("url_picture", user.getUrlPicture());
				a.put(json);
			}

		}
		res.put("conversations", a);
		return res;
	}

	private AggregationOutput getAllConversations(String type, int idReceiver) {
		MongoClient m = new MongoClient(DBStatic.mongoDB_host,DBStatic.mongoDB_port);
		DB db = m.getDB(DBStatic.mysql_db);
		DBCollection col = db.getCollection(NAME_COLLECTION);


		List<Object> eqOperand1 = new ArrayList<Object>();
		eqOperand1.add("$show");
		eqOperand1.add(0);

		List<Object> eqOperand2 = new ArrayList<Object>();
		eqOperand2.add("$id_receiver");
		eqOperand2.add(idReceiver);

		BasicDBObject eq1 = new BasicDBObject("$eq", eqOperand1);
		BasicDBObject eq2 = new BasicDBObject("$eq", eqOperand2);

		List<Object> andOperands = new ArrayList<Object>();
		andOperands.add(eq1);
		andOperands.add(eq2);

		List<Object> positiveCondOperands = new ArrayList<Object>();
		positiveCondOperands.add(new BasicDBObject("$and", andOperands));
		positiveCondOperands.add(1);
		positiveCondOperands.add(0);
		BasicDBObject finalPositiveCond = new BasicDBObject("$cond",
				positiveCondOperands);

		DBObject sumOfPositiveCount = new BasicDBObject("$sum",
				finalPositiveCond);

		DBObject obj = new BasicDBObject();
		obj.put("_id", "$id_sender");
		obj.put("count", sumOfPositiveCount);

		DBObject group = new BasicDBObject("$group", obj);

		DBObject sortFields = new BasicDBObject("count", -1);

		DBObject sort = new BasicDBObject("$sort", sortFields);

		AggregationOutput output = col.aggregate(group, sort);
		m.close();
		return output;
	}

	private Set<Integer> getIds(int idReceiver) throws JSONException {
		MongoClient m = new MongoClient(DBStatic.mongoDB_host,DBStatic.mongoDB_port);
		DB db = m.getDB(DBStatic.mysql_db);
		DBCollection col = db.getCollection(NAME_COLLECTION);

		BasicDBObject obj1 = new BasicDBObject();
		BasicDBObject obj2 = new BasicDBObject();

		obj1.put("id_receiver", idReceiver);

		obj2.put("id_sender", idReceiver);

		BasicDBObject orQuery = new BasicDBObject();
		List<BasicDBObject> listObj = new ArrayList<BasicDBObject>();
		listObj.add(obj1);
		listObj.add(obj2);
		orQuery.put("$or", listObj);

		DBCursor cursor = col.find(orQuery);

		Set<Integer> set = new HashSet<>();
		while(cursor.hasNext()){
			DBObject res= cursor.next();
			JSONObject res2 = new JSONObject(res.toString());
			if(res2.getInt("id_sender")==idReceiver){
				set.add(res2.getInt("id_receiver"));
			}else{
				set.add(res2.getInt("id_sender"));
			}

		}
		cursor.close();
		m.close();
		return set;
	}

	public void removeAll(){
		MongoClient m = new MongoClient(DBStatic.mongoDB_host,DBStatic.mongoDB_port);
		DB db = m.getDB(DBStatic.mysql_db);
		DBCollection col = db.getCollection(NAME_COLLECTION);
		BasicDBObject document = new BasicDBObject();

		col.remove(document);
	}

	public JSONObject notifier(int idReceiver) throws JSONException{
		JSONArray array = getNoShowingConversations(idReceiver).getJSONArray("conversations");
		int count = 0;
		JSONObject json = new JSONObject();
		for(int i=0; i<array.length(); i++){
			if(array.getJSONObject(i).getInt("_id")!=0){
				if(array.getJSONObject(i).has("count")){
					count+=array.getJSONObject(i).getInt("count");
				}
			}else{
				json.put("system_count", array.getJSONObject(i).getInt("count"));
			}
		}

		json.put("messages_count", count);
		if(!json.has("system_count")) json.put("system_count", 0);
		JSONObject res = new JSONObject();
		res.put("notifications_count", json);
		return res;
	}
	
	public void setStatut(int idExchange, int statut){
		MongoClient m =  new MongoClient(DBStatic.mongoDB_host,DBStatic.mongoDB_port);

		DB db = m.getDB(DBStatic.mysql_db);
		DBCollection col = db.getCollection(NAME_COLLECTION);

	    BasicDBObject query = new BasicDBObject();
	    query.put("id_exchange", idExchange);

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("statut", statut));

		BasicDBObject searchQuery = new BasicDBObject().append("id_exchange", idExchange);

		col.updateMulti(searchQuery, newDocument);

		m.close();
	}

}
