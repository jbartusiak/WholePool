package com.jba.dao2.ride.enitity;

import com.jba.dao2.Dao2Application;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.source.entity.Source;
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
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Dao2Application.class })
@WebAppConfiguration
public class OfferedRidesTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void getTest(){
        Session session = sessionFactory.getCurrentSession();

        List<OfferedRides> rides = session.createQuery("from OfferedRides ", OfferedRides.class).getResultList();
        int count = session.createQuery("select count(o) from OfferedRides o", Long.class).getSingleResult().intValue();

        assertEquals("Count(*) and collection size should be equal", rides.size(), count);
    }

    @Test
    @Transactional
    public void createTest(){
        Session session = sessionFactory.getCurrentSession();

        System.out.println("Get a user from DB");
        User user = session.createQuery("from User where userId=1", User.class).getSingleResult();
        System.out.println("Get source from DB");
        Source source = session.createQuery("from Source where sourceName='localhost'", Source.class).getSingleResult();
        System.out.println("Create route");
        Route route = new Route("warszawa", "wroclaw");

        try {
            System.out.println("Saving route");
            session.save(route);

            Ride ride = new Ride(source, route);
            System.out.println("Saving ride");
            session.save(ride);

            OfferedRides offeredRides = new OfferedRides(ride,user);
            System.out.println("Saving offeredRide");
            session.save(offeredRides);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}