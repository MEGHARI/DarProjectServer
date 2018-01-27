package com.dar.hibernate.dao;

import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.UserSession;
import com.sun.mail.iap.Literal;

import antlr.collections.List;

public class UserSessionDAO{

	SessionFactory sessionFactory;
	
	public UserSessionDAO(Class<UserSession> t, SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public String create(int idUser){
		String token = UUID.randomUUID().toString();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		String t = (String) session.save(new UserSession(token, idUser, System.currentTimeMillis()));
		session.getTransaction().commit();
		session.close();
		return t;
	}
	
	public void delete(String token){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.delete(session.get(UserSession.class, token));
		session.getTransaction().commit();
		session.close();
	}
	
	public boolean sessionExist(String token){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		UserSession us = session.get(UserSession.class, token);
		session.getTransaction().commit();
		session.close();
		return us!=null;
	}
	
	public int getIdUserByToken(String token){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		UserSession us = session.get(UserSession.class, token);
		session.getTransaction().commit();
		session.close();
		if(us==null){
			return -1;
		}
		return us.getIdUser();
	}
	

}