package com.dar.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.Exchange;
import com.dar.hibernate.entity.GameUser;

public class GameUserDAO extends AbstractDAO<GameUser>{

	private static SessionFactory sessionFactory;
	
	public GameUserDAO(Class t, SessionFactory sessionFactory) {
		super(t, sessionFactory);
		this.sessionFactory = sessionFactory; 
	}
	
	public boolean gameUserExist(int idUser, int idGame){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Exchange> e = session
				.createQuery("from GameUser where id_user = "+idUser+" and id_game ="+idGame)
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return !e.isEmpty();
	}
	
	public List<GameUser> getListGameUser(int id){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<GameUser> e = session
				.createQuery("from GameUser where id_user="+id)
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return e;
	}
	
	public List<GameUser> getListUsersGame(int id){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<GameUser> e = session
				.createQuery("from GameUser where id_game="+id)
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return e;
	}
	
}
