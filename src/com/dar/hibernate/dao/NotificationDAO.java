package com.dar.hibernate.dao;

import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.Notification;

public class NotificationDAO extends AbstractDAO<Notification>{

	private static SessionFactory sessionFactory;
	
	public NotificationDAO(Class t, SessionFactory sessionFactory) {
		super(t, sessionFactory);
		this.sessionFactory = sessionFactory; 
	}
	
}
