package com.projectSta.utils.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;

public class StoreHibernateUtil {
	
	private static final SessionFactory sessionFactory;
	 
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public static Session openSession() {
        return sessionFactory.openSession();
    }
    
    public static StatelessSession openStatelessSession() {
        return sessionFactory.openStatelessSession();
    }
}
