package com.jba.dao2.entity;

import com.jba.dao2.Dao2Application;
import com.jba.dao2.route.entity.Route;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Dao2Application.class })
@WebAppConfiguration
public class RouteTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void addNewEntryToRoute(){
        Session session = sessionFactory.getCurrentSession();
        Route route = new Route("adsfafafaf", "fafafafasas");
        session.save(route);
    }
}