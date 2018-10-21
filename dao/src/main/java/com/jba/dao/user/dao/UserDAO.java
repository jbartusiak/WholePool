package com.jba.dao.user.dao;

import com.jba.dao.user.enitity.User;
import com.jba.dao.user.enitity.UserType;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;

import java.util.List;

public class UserDAO {

    private UserDAO(){

    }

    public static User getUserById(int id){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        User result = session.get(User.class, id);

        session.getTransaction().commit();

        WPLSessionFactory.closeSession(session);

        return result;
    }

    public static List<UserType> getUserByUsername(String username){
        //todo: Implement

        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        List<User> result = session
                .createQuery(" from UserType u")
                .getResultList();

        session.getTransaction().commit();

        WPLSessionFactory.closeSession(session);

        return null;
    }
}
