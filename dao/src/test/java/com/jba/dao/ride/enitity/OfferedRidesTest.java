package com.jba.dao.ride.enitity;

import com.jba.dao.route.entity.Route;
import com.jba.dao.source.entity.Source;
import com.jba.dao.user.enitity.User;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//TODO Implement
class OfferedRidesTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session= WPLSessionFactory.getDBSession();
    }

    @Test
    void getTest(){
        session.beginTransaction();

        List<OfferedRides> rides = session.createQuery("from OfferedRides ", OfferedRides.class).getResultList();
        int count = session.createQuery("select count(o) from OfferedRides o", Long.class).getSingleResult().intValue();

        assertEquals(rides.size(), count, "Count(*) and collection size should be equal");

        session.getTransaction().commit();
    }

    @Test
    void createTest(){
        session.beginTransaction();

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
            session.getTransaction().rollback();
            fail(e);
        }
        finally {
            session.getTransaction().rollback();
        }
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}