package com.jba.dao2.ride.dao;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
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
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//TODO Implement!!!

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class RideDAOTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RideDAO rideDAO;

    @Test
    public void getRideById() {
        List<Ride> rides = rideDAO.getAllRides();

        int randomVal = (new Random()).nextInt(rides.size());

        Ride singleRide = rideDAO.getRideById(rides.get(randomVal).getRideId());

        assertEquals("These two items should be equal!", rides.get(randomVal).getRideId(), singleRide.getRideId());
    }

    @Test
    @Transactional
    public void getAllRides() {
        List<Ride> rides = rideDAO.getAllRides();
        Session session = sessionFactory.getCurrentSession();

        long collectionSize = session.createQuery("select count(r) from Ride r", Long.class).getSingleResult();

        assertEquals(collectionSize, rides.size());
    }

    @Test
    public void findRideByCriteria() {
    }

    @Test
    public void getRideDetials() {
    }

    @Test
    public void addRide() {
    }

    @Test
    public void addRideDetails() {
    }

    @Test
    public void deleteRide() {
    }

    @Test
    public void registerToRide() {
    }

    @Test
    public void unregisterFromRide() {
    }

    @Test
    public void getRidesByUser() {
    }

    @Test
    public void getUpcomingRidesForUser() {
        List<RideDetails> rd = rideDAO.getRidesForUser(User.of(1), true);

        for(RideDetails rideDetails: rd){
            System.out.println(rd);
        }

        assertNotNull(rd);
    }
}