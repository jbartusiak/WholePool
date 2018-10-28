package com.jba.dao.ride.enitity;

import com.jba.dao.route.entity.Route;
import com.jba.dao.source.entity.Source;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RideTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void testInOrder(){
        createTest();
        getTest();
        deleteTest();
    }

    void createTest(){
        session.beginTransaction();
        Source source = session.createQuery("from Source where sourceName='localhost'", Source.class).getSingleResult();
        System.out.println("Create route");
        Route route = new Route("xxx", "yyy");
        try {
            session.save(route);
            Ride ride = new Ride(source, route);
            session.save(ride);
            session.getTransaction().commit();
        }
        catch (Exception e){
            session.getTransaction().rollback();
            fail(e);
        }
    }

    void getTest(){
        session.beginTransaction();

        List<Ride> rides = session.createQuery("from Ride",Ride.class).getResultList();

        assertNotNull(rides);
        assertNotEquals(0,rides.size(),"There is at least one Ride saved in previous test");

        session.getTransaction().commit();
    }

    void deleteTest(){
        session.beginTransaction();

        Ride ride = session.createQuery(
                "from Ride r where r.sourceId.sourceName='localhost' and r.routeForThisRide.routeFromLocation='xxx'",
                Ride.class).getResultList().get(0);

        session.delete(ride);
        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}