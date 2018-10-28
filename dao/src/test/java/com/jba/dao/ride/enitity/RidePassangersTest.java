package com.jba.dao.ride.enitity;

import com.jba.dao.user.enitity.User;
import com.jba.dao.user.enitity.UserType;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class RidePassangersTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session= WPLSessionFactory.getDBSession();
    }

    @Test
    void createDBObject(){
        session.beginTransaction();
        Ride ride = session.createQuery("from Ride",Ride.class).getResultList().get(0);
        System.out.println(ride.toString());

        User user = session.createQuery("from User u",User.class).getResultList().get(0);

        UserType userType = session.createQuery("from UserType s where s.typeName='Passenger'", UserType.class).getSingleResult();
        User user2 = new User(userType, "test@test.pl","AWDAWDAWD","Adam","orzech", Date.valueOf("2018-01-01"),"orzeszek");

        session.save(user2);

        RidePassangers passangers = new RidePassangers(user,ride);

        session.save(passangers);

        passangers = new RidePassangers(user2,ride);

        session.save(passangers);

        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}