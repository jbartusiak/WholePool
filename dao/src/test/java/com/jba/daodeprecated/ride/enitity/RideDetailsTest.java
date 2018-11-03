package com.jba.daodeprecated.ride.enitity;

import com.jba.daodeprecated.route.entity.Route;
import com.jba.daodeprecated.source.entity.Source;
import com.jba.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

class RideDetailsTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void shouldCreateRideDetails(){
        session.beginTransaction();
        Route route = new Route("xxx","yyy");
        session.save(route);
        Source source = session.createQuery("from Source s where s.sourceName='localhost'",Source.class).getSingleResult();

        Ride ride = new Ride(source, route);
        session.save(ride);

        RideDetails details = new RideDetails(ride,Date.valueOf("2018-01-01"),Date.valueOf("2018-02-02"),2000,2.0,"Description");

        session.save(details);

        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
    }
}