package com.jba.dao.preferences.entity;

import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PreferenceTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void READtest(){
        session.beginTransaction();

        System.out.println("Read test");

        try {
            List<Preference> preferences = session.createQuery("from Preference p", Preference.class).getResultList();
            int lenght = (session.createQuery("select count(p) from Preference p", Long.class).getSingleResult()).intValue();
            assertEquals(lenght,preferences.size(), "Length and collection size are "+lenght+ " collection "+preferences.size());
        }
        catch (Exception e){
            session.getTransaction().rollback();
            fail(e);
        }
        finally {
            session.getTransaction().commit();
        }
    }

    @Test
    void createTest(){
        Transaction tx = session.beginTransaction();

        System.out.println("CREATE and DELETE test");

        try{
            Preference newPreference = new Preference("Allow asians", "boolean");
            session.save(newPreference);
            Preference fromDB = session.createQuery("from Preference where preferenceName='Allow asians'", Preference.class).getSingleResult();

            assertEquals(fromDB, newPreference, "Saved and local preferences are equal");
        }
        catch (Exception e){
            tx.rollback();
            fail(e);
        }
        finally {
            tx.rollback();
        }
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}