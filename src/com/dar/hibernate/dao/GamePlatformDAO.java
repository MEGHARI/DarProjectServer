package com.dar.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.GamePlatform;

public class GamePlatformDAO extends AbstractDAO<GamePlatform>{

	private SessionFactory sessionFactory;
	
	public GamePlatformDAO(Class<GamePlatform> t, SessionFactory sessionFactory) {
		super(t, sessionFactory);
		this.sessionFactory = sessionFactory;
	}
	
	public List<GamePlatform> getGamePlatformsByIdGame(int idGame){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<GamePlatform> ts = session
				.createQuery("from GamePlatform where id_game ="+idGame)
				.list();
		session.getTransaction().commit();
		session.close();
		return ts;
	}
	
}
