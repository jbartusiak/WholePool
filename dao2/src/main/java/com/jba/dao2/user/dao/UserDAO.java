package com.jba.dao2.user.dao;

import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.preferences.entity.UsersPreference;
import com.jba.dao2.user.enitity.User;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private final static Logger logger = Logger.getLogger(UserDAO.class);

    @Transactional
    public User getUserById(int id){
        Session session = sessionFactory.getCurrentSession();
        try {
            List<User> users = session.
                    createQuery("from User where userId=:id", User.class).
                    setParameter("id", id).
                    getResultList();

            if(users.size()==0){
                throw new IllegalArgumentException("No users found with id: "+id);
            }
            else if (users.size() > 1) {
                throw new IllegalArgumentException("There are more than one users of this id");
            } else {
                return users.get(0);
            }
        }
        catch (Exception e){
            logger.error("Error getting user with id: "+id);
            throw e;
        }
    }

    public User getUserByEmail(String email){
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            List<User> users = session.
                    createQuery("from User where emailAddress=:email", User.class).
                    setParameter("email", email).
                    getResultList();

            session.getTransaction().commit();

            session.close();

            if(users.size()==0){
                throw new IllegalArgumentException("No user found with email: "+email);
            }
            else if (users.size() > 1) {
                throw new IllegalArgumentException("There are more than one users of this email");
            } else {
                return users.get(0);
            }
        }
        catch (Exception e){
            logger.error("Error getting user with email: "+email);
            if(session.isOpen())
                session.close();
            throw e;
        }
    }

    public User getUserByName(String name){
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            List<User> users = session.
                    createQuery("from User where userName=:name", User.class).
                    setParameter("name", name).
                    getResultList();

            session.getTransaction().commit();
            session.close();
            if(users.size()==0){
                throw new IllegalArgumentException("No users found with name: "+name);
            }
            else if (users.size() > 1) {
                throw new IllegalArgumentException("There are more than one users of this name");
            } else {
                return users.get(0);
            }
        }
        catch (Exception e){
            logger.error("Error getting user with name: "+name);
            if(session.isOpen())
                session.close();
            throw e;
        }
    }

    public boolean verifyUserPasswordHash(User user, String hash){
        User fromDB = getUserById(user.getUserId());

        return fromDB.getPasswordHash().equals(hash);
    }

    public void updateUserData(User user){
        Session session = sessionFactory.getCurrentSession();

        session.saveOrUpdate(user);
    }

    public UsersPreference setPreference(User user, Preference preference, String value){
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            UsersPreference usersPreference = new UsersPreference(user, preference, value);

            session.save(usersPreference);
            session.getTransaction().commit();

            session.close();

            return usersPreference;
        }
        catch (Exception e){
            logger.error("Error setting preference "+preference.toString()+" to user "+user.toString());
            if(session.isOpen()){
                session.getTransaction().rollback();
                session.close();
            }
            throw e;
        }
    }

    public Map<String,String> getUsersPreferences(User user){
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();
            int userId = user.getUserId();

            List<UsersPreference> usersPreferences = session.
                    createQuery("from UsersPreference up where up.user=:user", UsersPreference.class).
                    setParameter("user", user).
                    getResultList();

            Map<String, String> result = new HashMap<>();

            usersPreferences.forEach(
                    preference -> result.put(preference.getPreference().getPreferenceName(), preference.getValue())
            );

            session.getTransaction().commit();

            session.close();

            return result;
        }
        catch (Exception e){
            logger.error("Error retrieving users preferences!", e);
            if(session.isOpen())
                session.close();
            throw e;
        }
    }

    public User addNewUser(User user){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
        return user;
    }

    public void resetPassword(User user, String passwordHash){
        user.setPasswordHash(passwordHash);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
    }
}
