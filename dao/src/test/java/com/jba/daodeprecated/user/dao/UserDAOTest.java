package com.jba.daodeprecated.user.dao;

import com.jba.daodeprecated.preferences.entity.Preference;
import com.jba.daodeprecated.preferences.entity.UsersPreference;
import com.jba.daodeprecated.user.enitity.User;
import com.jba.daodeprecated.user.enitity.UserType;
import com.jba.WPLSessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    final static Logger logger = Logger.getLogger(UserDAOTest.class);

    @Test
    void getUserById() {
        logger.info("Get user by id 1");
        User user = UserDAO.getUserById(1);
        assertNotNull(user);

        logger.info("From DB: "+user.toString());
        assertEquals(user.getEmailAddress(), "jakub.bartusiak@gmail.com");
    }

    @Test
    void getUserByEmail() {
        logger.info("Get user by email: jakub.bartusiak@gmail.com");
        User user = UserDAO.getUserByEmail("jakub.bartusiak@gmail.com");
        assertNotNull(user);

        logger.info("From DB: "+user.toString());
        assertEquals(user.getFirstName(), "Jakub");
    }

    @Test
    void getUserByName() {
        logger.info("Get user by name: ambiguous1991");
        User user = UserDAO.getUserByName("ambiguous1991");
        assertNotNull(user);

        logger.info("FromDB: "+user.toString());
        assertEquals(user.getFirstName(), "Jakub");
    }

    @Test
    void verifyUserPasswordHash() {
        logger.info("Assuming password hash for userid=1 is PLACEHOLDER");
        assertTrue(UserDAO.verifyUserPasswordHash(UserDAO.getUserById(1), "PLACEHOLDER"));
    }

    @Test
    void updateUserData() {
        User user = UserDAO.getUserById(1);

        logger.info("At the beggining: "+user.getLastName());
        user.setLastName("Adamowski");

        UserDAO.updateUserData(user);

        User fromDB = UserDAO.getUserById(1);
        logger.info("After update: "+fromDB.getLastName());

        assertEquals(user.getLastName(), fromDB.getLastName());

        logger.info("Returning to default");

        user.setLastName("Bartusiak");
        UserDAO.updateUserData(user);
    }

    @Test
    void setPreferenceAndGetUsersPreferences() {
        Session session = WPLSessionFactory.getDBSession();
        session.beginTransaction();

        Preference preference = session.createQuery("from Preference p where p.preferenceId=1", Preference.class).getSingleResult();
        User user = UserDAO.getUserById(1);

        UsersPreference usersPreference = UserDAO.setPreference(user,preference, "true");

        Map<String,String> preferences = UserDAO.getUsersPreferences(user);

        assertNotEquals(preferences.size(),0);

        session = WPLSessionFactory.getDBSession();

        session.beginTransaction();
        session.delete(usersPreference);
        session.getTransaction().commit();
        session.close();
    }

    @Test
    void addNewUser() {
        UserType ut = new UserType();
        ut.setTypeId(1);
        User user = new User(ut, "test", "test", "test", "test", Date.valueOf("1991-01-01"),"test");
        UserDAO.addNewUser(user);

        assertNotEquals(0, user.getUserId());
    }

    @Test
    void resetPassword() {
        User user = UserDAO.getUserById(2);

        if(user.getPasswordHash().equals("TEMP")){
            user.setPasswordHash("OTHER");
            UserDAO.updateUserData(user);
        }

        UserDAO.resetPassword(user, "TEMP");
        assertEquals(user.getPasswordHash(), "TEMP");
    }
}