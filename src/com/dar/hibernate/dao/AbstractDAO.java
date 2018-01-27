package com.dar.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractDAO<T extends Serializable> {

	private SessionFactory sessionFactory;
	private Class<T> clazz;

	public AbstractDAO(Class<T> t, SessionFactory sessionFactory){
		this.clazz = t;
		this.sessionFactory = sessionFactory;
	}

	public Integer create(T obj) throws Exception{
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Integer t = (Integer) session.save(obj);
		session.getTransaction().commit();
		session.close();
		return t;
	}

	/*
	 * SI pas trouv�, retourne null
	 */
	public T findById(int id){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		T t = session.get(clazz, id);
		session.getTransaction().commit();
		session.close();
		return t;
	}
	
	public List<T> findAll(){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<T> ts = session
				.createQuery("from "+ clazz.getName())
				.list();
		session.getTransaction().commit();
		session.close();
		return ts;
	}

	public void update(T obj){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.update(obj);
		session.getTransaction().commit();
		session.close();
	}

	public void delete(T obj){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.delete(obj);
		session.getTransaction().commit();
		session.close();
	}
}
