package com.jba.session;

import com.jba.dao.user.enitity.UserType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

public class WPLSessionFactory {

    private static SessionFactory FACTORY_INSTANCE;
    private static ArrayList<Session> sessnionRegister;

    private WPLSessionFactory(){

    }

    private static SessionFactory getInstance(){
        if(FACTORY_INSTANCE==null){
            FACTORY_INSTANCE = initialize();
            sessnionRegister = new ArrayList<>();
        }
        if(FACTORY_INSTANCE.isClosed()){
            FACTORY_INSTANCE = initialize();
        }
        return FACTORY_INSTANCE;
    }

    private static SessionFactory initialize(){
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(UserType.class)
                .buildSessionFactory();
    }

    public static Session getDBSession(){
        Session session = getInstance().openSession();
        sessnionRegister.add(session);
        return session;
    }

    public static void closeAndFinalize(){
        sessnionRegister.forEach(session ->{
            if(session.isOpen()) {
                session.close();
            }
        });
        getInstance().close();
    }

    public static void closeSession(Session session){
        session.close();
    }
}
