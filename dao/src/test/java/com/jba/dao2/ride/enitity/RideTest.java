package com.jba.dao2.ride.enitity;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.source.entity.Source;
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

import static junit.framework.TestCase.assertNotNull;
import static org.assertj.core.api.Java6Assertions.fail;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class RideTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void createTest(){
        Session session = sessionFactory.getCurrentSession();
        Source source = session.createQuery("from Source where sourceName='localhost'", Source.class).getSingleResult();
        System.out.println("Create route");
        Route route = new Route("xxx", "yyy");
        try {
            session.save(route);
            Ride ride = new Ride(source, route);
            session.save(ride);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void getTest(){
        Session session = sessionFactory.getCurrentSession();

        List<Ride> rides = session.createQuery("from Ride",Ride.class).getResultList();

        assertNotNull(rides);
        assertNotEquals("There is at least one Ride saved in previous test", 0,rides.size());
    }

    @Test
    @Transactional
    public void deleteTest(){
        Session session = sessionFactory.getCurrentSession();

        Ride ride = session.createQuery(
                "from Ride r where r.sourceId.sourceName='localhost' and r.routeForThisRide.routeFromLocation='xxx'",
                Ride.class).getResultList().get(0);
    }
}