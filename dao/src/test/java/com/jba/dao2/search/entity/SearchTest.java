package com.jba.dao2.search.entity;

import com.jba.dao2.DAOConfig;
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
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class SearchTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void createNewSearch(){
        Session session = sessionFactory.getCurrentSession();
        Route route = new Route("routeFrom","routeTo");
        session.save(route);
        Search search = new Search(route, "searchcriteria", "orderCriteria");
        session.save(search);
        session.getTransaction().commit();
    }

}