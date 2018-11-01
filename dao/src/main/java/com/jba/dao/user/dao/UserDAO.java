package com.jba.dao.user.dao;

import com.jba.dao.preferences.entity.Preference;
import com.jba.dao.preferences.entity.UsersPreference;
import com.jba.dao.user.enitity.User;
import com.jba.dao.user.enitity.UserType;
import com.jba.session.DBUtils;
import com.jba.session.WPLSessionFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class UserDAO {

    private final static Logger logger = Logger.getLogger(UserDAO.class);

    public static User getUserById(int id){
        Session session = WPLSessionFactory.getDBSession();
        try(session) {
            session.beginTransaction();

            List<User> users = session.
                    createQuery("from User where userId=:id", User.class).
                    setParameter("id", id).
                    getResultList();

            session.getTransaction().commit();
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

    public static User getUserByEmail(String email){
        Session session = WPLSessionFactory.getDBSession();
        try(session) {
            session.beginTransaction();

            List<User> users = session.
                    createQuery("from User where emailAddress=:email", User.class).
                    setParameter("email", email).
                    getResultList();

            session.getTransaction().commit();

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
            throw e;
        }
    }

    public static User getUserByName(String name){
        Session session = WPLSessionFactory.getDBSession();
        try(session) {
            session.beginTransaction();

            List<User> users = session.
                    createQuery("from User where userName=:name", User.class).
                    setParameter("name", name).
                    getResultList();

            session.getTransaction().commit();
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
            throw e;
        }
    }

    public static boolean verifyUserPasswordHash(User user, String hash){
        User fromDB = getUserById(user.getUserId());

        return fromDB.getPasswordHash().equals(hash);
    }

    public static void updateUserData(User user){
        DBUtils.saveOrUpdate(user);
    }

    public static UsersPreference setPreference(User user, Preference preference, String value){
        Session session = WPLSessionFactory.getDBSession();
        try(session) {
            session.beginTransaction();

            UsersPreference usersPreference = new UsersPreference(user, preference, value);

            session.save(usersPreference);
            session.getTransaction().commit();

            return usersPreference;
        }
        catch (Exception e){
            logger.error("Error setting preference "+preference.toString()+" to user "+user.toString());
            throw e;
        }
    }

    public static Map<String,String> getUsersPreferences(User user){
        Session session = WPLSessionFactory.getDBSession();

        try(session) {
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
            return result;
        }
        catch (Exception e){
            logger.error("Error retrieving users preferences!", e);
            throw e;
        }
    }

    public static User addNewUser(User user){
        DBUtils.saveOrUpdate(user);
        return user;
    }

    public static void resetPassword(User user, String passwordHash){
        user.setPasswordHash(passwordHash);
        DBUtils.saveOrUpdate(user);
    }
}
