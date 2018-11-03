package com.jba.daodeprecated.preferences.entity;

import com.jba.daodeprecated.user.enitity.User;
import com.jba.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersPreferenceTest {

    private Session session;
    private UsersPreference usersPreference;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test()
    void testInOrder(){
        createTest();
        selectTest();
        deleteTest();
    }

    void createTest(){
        session.beginTransaction();
        System.out.println("Get preference");
        Preference preference = session.createQuery("from Preference where preferenceId=1", Preference.class).getSingleResult();
        System.out.println("Get user");
        User user = session.createQuery("from User where userId=1", User.class).getSingleResult();

        usersPreference = new UsersPreference(user,preference,"true");

        try {
            System.out.println("Saving users preference");
            session.save(usersPreference);
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            fail(e);
        }

        System.out.println("Save complete");
    }

    void selectTest(){
        session.beginTransaction();

        UsersPreference fromDB = session.createQuery("from UsersPreference where user.id=1 and preference.id=1", UsersPreference.class).getResultList().get(0);

        assertEquals(usersPreference, fromDB);

        session.getTransaction().commit();
    }

    void deleteTest(){
        session.beginTransaction();

        session.delete(usersPreference);

        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}