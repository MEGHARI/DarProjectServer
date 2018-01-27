package com.dar.hibernate.utility;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.dar.hibernate.entity.Exchange;
import com.dar.hibernate.entity.FingerPrint;
import com.dar.hibernate.entity.Game;
import com.dar.hibernate.entity.GamePlatform;
import com.dar.hibernate.entity.GameUser;
import com.dar.hibernate.entity.Notification;
import com.dar.hibernate.entity.Platform;
import com.dar.hibernate.entity.User;
import com.dar.hibernate.entity.UserSession;

public class HibernateUtility {
	 
    public static SessionFactory factory; 
    private HibernateUtility() {
    }
 
    public static synchronized SessionFactory getSessionFactory() {
 
        if (factory == null) {
            factory = new Configuration()
					.configure("hibernate.cfg.xml")
					.addAnnotatedClass(UserSession.class)
					.addAnnotatedClass(User.class)
					.addAnnotatedClass(Game.class)
					.addAnnotatedClass(GameUser.class)
					.addAnnotatedClass(Exchange.class)
					.addAnnotatedClass(Platform.class)
					.addAnnotatedClass(GamePlatform.class)
					.addAnnotatedClass(Notification.class)
					.addAnnotatedClass(FingerPrint.class)
					.buildSessionFactory();
        }
        return factory;
    }
}