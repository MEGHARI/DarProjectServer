package com.dar.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dar.hibernate.entity.FingerPrint;

public class FingerPrintDAO extends AbstractDAO<FingerPrint> {
	
	private SessionFactory sessionFactory;
	public FingerPrintDAO(Class t, SessionFactory sessionFactory) {
		super(t, sessionFactory);
		this.sessionFactory = sessionFactory;
	} 
	
public List<FingerPrint> getFingersPrintByUser(int id){
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<FingerPrint> ts = session
				.createQuery("from FingerPrint where idUser = :id").setInteger("id",id)
				.list();
		session.getTransaction().commit();
		session.close();
		return ts;
		
	}
	

}
