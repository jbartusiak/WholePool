package com.jba.dao2.entity;

import com.jba.dao2.Dao2Application;
import com.jba.dao2.route.entity.PopularRoute;
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
public class PopularRouteTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void addNewItemToPopularRoute(){
        Session session = sessionFactory.getCurrentSession();
        Route route = session.createQuery("from Route r",Route.class).getResultList().get(0);
        PopularRoute popularRoute = new PopularRoute(route,2.0,3.0,1.0,2);
        session.save(popularRoute);
        session.delete(popularRoute);
    }
}