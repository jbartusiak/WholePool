package com.jba.dao2.ride.enitity;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.user.enitity.User;
import com.jba.dao2.user.enitity.UserType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class RidePassangersTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void createDBObject(){
        Session session = sessionFactory.getCurrentSession();

        Ride ride = session.createQuery("from Ride",Ride.class).getResultList().get(0);
        System.out.println(ride.toString());

        User user = session.createQuery("from User u",User.class).getResultList().get(0);

        UserType userType = session.createQuery("from UserType s where s.typeName='Passenger'", UserType.class).getSingleResult();
        User user2 = new User(userType, "test@test.pl","AWDAWDAWD","Adam","orzech", Date.valueOf("2018-01-01"),"orzeszek");

        session.save(user2);

        RidePassangers passangers = new RidePassangers(user,ride);

        session.delete(passangers);

        session.save(passangers);

        RidePassangers passangers2 = new RidePassangers(user2,ride);

        session.save(passangers);

        session.delete(passangers);
        session.delete(passangers2);
        session.delete(user2);
    }
}