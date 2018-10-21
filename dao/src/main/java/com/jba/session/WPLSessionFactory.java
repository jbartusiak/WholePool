package com.jba.session;

import com.jba.dao.user.enitity.UserType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

public class WPLSessionFactory {

    private static SessionFactory FACTORY_INSTANCE;
    private static ArrayList<Session> sessionsHandedOut;

    private WPLSessionFactory(){

    }

    private static SessionFactory getInstance(){
        if(FACTORY_INSTANCE==null){
            FACTORY_INSTANCE = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(UserType.class)
                    .buildSessionFactory();
            sessionsHandedOut = new ArrayList<>();
        }
        return FACTORY_INSTANCE;
    }

    public static Session getDBSession(){
        Session session = getInstance().getCurrentSession();
        sessionsHandedOut.add(session);
        return session;
    }

    public static void closeAndFinalize(){
        sessionsHandedOut.forEach(session -> session.close());
        getInstance().close();
    }

    public static void closeSession(Session session){
        session.close();
    }
}
