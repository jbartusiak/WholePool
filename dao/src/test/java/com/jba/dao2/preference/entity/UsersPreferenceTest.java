package com.jba.dao2.preference.entity;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.preferences.entity.UsersPreference;
import com.jba.dao2.user.enitity.User;
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
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class UsersPreferenceTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void createTest(){
        Session session = sessionFactory.getCurrentSession();
        System.out.println("Get preference");
        Preference preference = session.createQuery("from Preference where preferenceId=1", Preference.class).getSingleResult();
        System.out.println("Get user");
        User user = session.createQuery("from User where userId=1", User.class).getSingleResult();

        UsersPreference usersPreference = new UsersPreference(user,preference,"true");

        try {
            System.out.println("Saving users preference");
            session.save(usersPreference);
            session.getTransaction().commit();
        }
        catch(Exception e){
            fail(e.getMessage());
        }

        System.out.println("Save complete");
    }

    @Test
    @Transactional
    public void selectTest(){
        Session session = sessionFactory.getCurrentSession();

        List<UsersPreference> fromDB = session.
                createQuery("from UsersPreference where user.id=1 and preference.id=1", UsersPreference.class).
                getResultList();

        assertNotNull(fromDB);
    }
}