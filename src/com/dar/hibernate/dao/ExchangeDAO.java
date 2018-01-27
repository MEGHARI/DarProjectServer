package com.dar.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.Exchange;

public class ExchangeDAO extends AbstractDAO<Exchange>{

	private static SessionFactory sessionFactory;
	
	public ExchangeDAO(Class t, SessionFactory sessionFactory) {
		super(t, sessionFactory);
		this.sessionFactory = sessionFactory; 
	}

	public boolean exchangeExist(int id){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Exchange> e = session
				.createQuery("from Exchange where "
						+ "(id_game_user_sender = "+id+" and confirm_exchange != -1 and return_ok = 0)"
						+ "or (id_game_user_receiver = "+id+" and confirm_exchange = 1 and return_ok = 0)")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return !e.isEmpty();
	}
	
	public List<Exchange> getExchangeByIdGameUser(int id){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Exchange> e = session
				.createQuery("from Exchange where "
						+ "(id_game_user_sender = "+id+" and confirm_exchange != -1 and return_ok = 0)"
						+ "or (id_game_user_receiver = "+id+" and confirm_exchange = 1 and return_ok = 0)")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return e;
	}
	
	public List<Exchange> getListExchanges(int id){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Exchange> e = session
				.createQuery("from Exchange where "
						+ "(id_game_user_sender = "+id+" and confirm_exchange = 1)"
						+ "or (id_game_user_receiver = "+id+" and confirm_exchange = 1)")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return e;
	}
	
	public List<Exchange> findByIdReceiver(int idReceiver){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Exchange> e = session
				.createQuery("from Exchange where id_game_user_receiver = "+idReceiver)
				.list();
		session.getTransaction().commit();
		session.close();
		return e;
	}
	
	public List<Exchange> getListExchangesToDelete(int id){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Exchange> e = session
				.createQuery("from Exchange where "
						+ "(id_game_user_sender = "+id+")"
						+ "or (id_game_user_receiver = "+id+")")
				.getResultList();
		session.getTransaction().commit();
		session.close();
		return e;
	}
	
	public void deleteList(List<Exchange> list) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		for(Exchange e:list) {
			session.delete(e);
		}
		session.getTransaction().commit();
		session.close();

	}
}
