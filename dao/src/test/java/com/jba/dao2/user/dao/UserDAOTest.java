package com.jba.dao2.user.dao;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.preferences.entity.UsersPreference;
import com.jba.dao2.user.enitity.User;
import com.jba.dao2.user.enitity.UserType;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class UserDAOTest {

    final static Logger logger = Logger.getLogger(UserDAOTest.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDAO userDAO;
    
    @Test
    public void getUserById() {
        logger.info("Get user by id 1");
        User user = userDAO.getUserById(1);
        assertNotNull(user);

        logger.info("From DB: "+user.toString());
        assertEquals(user.getEmailAddress(), "jakub.bartusiak@gmail.com");
    }

    @Test
    public void getUserByEmail() {
        logger.info("Get user by email: jakub.bartusiak@gmail.com");
        User user = userDAO.getUserByEmail("jakub.bartusiak@gmail.com");
        assertNotNull(user);

        logger.info("From DB: "+user.toString());
        assertEquals(user.getFirstName(), "Jakub");
    }

    @Test
    public void getUserByName() {
        logger.info("Get user by name: ambiguous1991");
        User user = userDAO.getUserByName("ambiguous1991");
        assertNotNull(user);

        logger.info("FromDB: "+user.toString());
        assertEquals(user.getFirstName(), "Jakub");
    }

    @Test
    public void getUserPasswordHash() {
        logger.info("Assuming password hash for userid=1 is PLACEHOLDER");
        User fromDB = userDAO.getUserById(1);
        String password = userDAO.getUserPasswordHash(fromDB);

        assertEquals(password, "PLACEHOLDER");
    }

    @Test
    public void updateUserData() {
        User user = userDAO.getUserById(1);

        logger.info("At the beggining: "+user.getLastName());
        user.setLastName("Adamowski");

        userDAO.updateUserData(user);

        User fromDB = userDAO.getUserById(1);
        logger.info("After update: "+fromDB.getLastName());

        assertEquals(user.getLastName(), fromDB.getLastName());

        logger.info("Returning to default");

        user.setLastName("Bartusiak");
        userDAO.updateUserData(user);
    }

    @Test
    @Transactional
    public void setPreferenceAndGetUsersPreferences() {
        Session session = sessionFactory.getCurrentSession();

        Preference preference = session.createQuery("from Preference p where p.preferenceId=1", Preference.class).getSingleResult();
        User user = userDAO.getUserById(1);

        UsersPreference usersPreference = userDAO.setPreference(user,preference, "true");

        Map<String,String> preferences = userDAO.getUsersPreferences(user);

        assertNotEquals(preferences.size(),0);
        
        session.delete(usersPreference);
        session.getTransaction().commit();
    }

    @Test
    public void addNewUser() {
        UserType ut = new UserType();
        ut.setTypeId(1);
        User user = new User(ut, "instancja@test.pl", "test", "test", "test", Date.valueOf("1991-01-01"),"test");
        userDAO.addNewUser(user);

        assertNotEquals(0, user.getUserId());
    }

    @Test
    public void resetPassword() {
        User user = userDAO.getUserById(2);

        if(user.getPasswordHash().equals("TEMP")){
            user.setPasswordHash("OTHER");
            userDAO.updateUserData(user);
        }

        userDAO.resetPassword(user, "TEMP");
        assertEquals(user.getPasswordHash(), "TEMP");
    }
}