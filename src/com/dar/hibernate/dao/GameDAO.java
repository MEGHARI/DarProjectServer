package com.dar.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.Game;

public class GameDAO extends AbstractDAO<Game>{
	
	private SessionFactory sessionFactory;

	public GameDAO(Class t, SessionFactory sessionFactory) {
		super(t, sessionFactory);
		this.sessionFactory = sessionFactory;
	}

	public Boolean GameExistByIdApi(int idGame){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Game> ts = session
				.createQuery("from Game where id_game ="+idGame)
				.list();
		session.getTransaction().commit();
		session.close();
		return !ts.isEmpty();
	}
	
	public int GetIdGameByIdApi(int idGame){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Game> ts = session
				.createQuery("from Game where id_game ="+idGame)
				.list();
		session.getTransaction().commit();
		session.close();
		return ts.get(0).getId();
	}
	
	public boolean gameByNameExist(String name){
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Game> ts = session
				.createQuery("from Game where name like :sf").setString("sf", '%'+name+'%')
				.list();
		session.getTransaction().commit();
		session.close();
		return !ts.isEmpty();
		
	}
	
	public List<Game> getGameByName(String name){
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Game> ts = session
				.createQuery("from Game where name like :sf").setString("sf", '%'+name+'%')
				.list();
		session.getTransaction().commit();
		session.close();
		return ts;
		
	}
	
}
