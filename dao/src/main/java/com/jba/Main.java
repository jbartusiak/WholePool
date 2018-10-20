package com.jba;

import com.jba.dao.user.UserType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Configuration configuration;
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserType.class).buildSessionFactory();

        Session session = factory.getCurrentSession();

        try{
            System.out.println("Creating a new user type.");
            UserType userType = new UserType("Example user");

            System.out.println("Opening session");
            session.beginTransaction();

            System.out.println("Saving user type");
            session.save(userType);

            System.out.println("Commiting");
            session.getTransaction().commit();

            System.out.println("Done");
        }
        finally {
            factory.close();
        }

    }
}
