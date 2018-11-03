package com.jba.dao2.preference.entity;

import com.jba.dao2.Dao2Application;
import com.jba.dao2.preferences.entity.Preference;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Dao2Application.class })
@WebAppConfiguration
public class PreferenceTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void READtest(){
        Session session = sessionFactory.getCurrentSession();

        System.out.println("Read test");

        try {
            List<Preference> preferences = session.createQuery("from Preference p", Preference.class).getResultList();
            int lenght = (session.createQuery("select count(p) from Preference p", Long.class).getSingleResult()).intValue();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void createTest(){
        Session session = sessionFactory.getCurrentSession();

        System.out.println("CREATE and DELETE test");

        try{
            Preference newPreference = new Preference("Allow asians", "boolean");
            session.save(newPreference);
            Preference fromDB = session.createQuery("from Preference where preferenceName='Allow asians'", Preference.class).getSingleResult();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}