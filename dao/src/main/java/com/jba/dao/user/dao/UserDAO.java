package com.jba.dao.user.dao;

import com.jba.dao.preferences.entity.Preference;
import com.jba.dao.preferences.entity.UsersPreference;
import com.jba.dao.user.enitity.User;
import com.jba.dao.user.enitity.UserType;
import com.jba.session.DBUtils;
import com.jba.session.WPLSessionFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class UserDAO {

    public static User getUserById(int id){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        List<User> users = session.
                createQuery("from User where userId=:id", User.class).
                setParameter("id", id).
                getResultList();

        session.getTransaction().commit();

        session.close();

        if(users.size()>1){
            throw new IllegalArgumentException("There are more than one users of this id");
        }
        else{
            return users.get(0);
        }
    }

    public static User getUserByEmail(String email){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        List<User> users = session.
                createQuery("from User where emailAddress=:email", User.class).
                setParameter("email", email).
                getResultList();

        session.getTransaction().commit();

        session.close();

        if(users.size()>1){
            throw new IllegalArgumentException("There are more than one users of this email");
        }
        else{
            return users.get(0);
        }
    }

    public static User getUserByName(String name){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        List<User> users = session.
                createQuery("from User where userName=:name", User.class).
                setParameter("name", name).
                getResultList();

        session.getTransaction().commit();

        session.close();
        if(users.size()>1){
            throw new IllegalArgumentException("There are more than one users of this name");
        }
        else{
            return users.get(0);
        }
    }

    public static boolean verifyUserPasswordHash(User user, String hash){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        User fromDB = getUserById(user.getUserId());

        return fromDB.getPasswordHash().equals(hash);
    }

    public static void updateUserData(User user){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        DBUtils.saveOrUpdate(session,user);
    }

    public static UsersPreference setPreference(User user, Preference preference, String value){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        UsersPreference usersPreference = new UsersPreference(user,preference,value);

        session.save(usersPreference);
        session.getTransaction().commit();
        session.close();

        return usersPreference;
    }

    public static Map<String,String> getUsersPreferences(User user){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        int userId = user.getUserId();

        List<UsersPreference> usersPreferences = session.
                createQuery("from UsersPreference up where up.user=:user", UsersPreference.class).
                setParameter("user",user).
                getResultList();

        Map<String,String> result = new HashMap<>();

        usersPreferences.forEach(
                preference->result.put(preference.getPreference().getPreferenceName(), preference.getValue())
        );

        session.getTransaction().commit();

        return result;
    }

    public static User addNewUser(User user){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        DBUtils.saveOrUpdate(session,user);

        return user;
    }

    public static void resetPassword(User user, String passwordHash){
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        user.setPasswordHash(passwordHash);

        DBUtils.saveOrUpdate(session, user);
    }
}
