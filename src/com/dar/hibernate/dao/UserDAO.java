package com.dar.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.User;

public class UserDAO extends AbstractDAO<User>{
	
	SessionFactory sessionFactory;

	public UserDAO(Class t, SessionFactory sessionFactory) {
		super(t, sessionFactory);
		this.sessionFactory = sessionFactory;
	}

	public boolean checkPassword(String mail, String password){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<User> l = session
				.createQuery("from User where mail='"+mail+"' and password='"+password+"'")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return !l.isEmpty();
	}

	public User getUserByMail(String mail){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<User> l = session
				.createQuery("from User where mail='"+mail+"'")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		if(l.size()==0){
			return null;
		}
		return l.get(0);
	}
	
	public User getUserByMailRole(String mail, String role){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<User> l = session
				.createQuery("from User where mail='"+mail+"' and role='"+role+"'")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		if(l.size()==0){
			return null;
		}
		return l.get(0);
	}
	
	public List<User> getListAdmins(){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<User> l = session
				.createQuery("from User where role=1")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return l;
	}
	
}