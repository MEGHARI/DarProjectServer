package com.dar.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.Game;
import com.dar.hibernate.entity.Platform;

public class PlatformDAO extends AbstractDAO<Platform>{

	private SessionFactory sessionFactory;
	
	public PlatformDAO(Class t, SessionFactory sessionFactory) {
		super(t, sessionFactory);
		this.sessionFactory = sessionFactory;
	}
	
	public Platform getByApiId(int id){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Platform> e = session
				.createQuery("from Platform where id_platform="+id)
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return e.get(0);
	}
	
	// ADDED BY HK GET LIST OF PLATEFORMS
	public List<Platform> getListPlatforms(){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Platform> e = session
				.createQuery("from Platform ")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		
		return e;
	}

}
