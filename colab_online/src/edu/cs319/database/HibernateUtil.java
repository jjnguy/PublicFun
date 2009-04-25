package edu.cs319.database;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Hibernate helper class...will allow the loading of the session factory and also
 * the assist in the mapping of the desired classes via Java Annotations
 * 
 * Reference: http://www.hibernate.org/hib_docs/annotations/reference/en/html_single/
 *
 */
public class HibernateUtil {
 
    private static final SessionFactory sessionFactory;
    
    static {
        try {

            sessionFactory = new AnnotationConfiguration().configure(new File("hibernate.cfg.xml")).buildSessionFactory();
        } catch (Throwable ex) {
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession()
            throws HibernateException {
        return sessionFactory.openSession();
    }

 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
}

