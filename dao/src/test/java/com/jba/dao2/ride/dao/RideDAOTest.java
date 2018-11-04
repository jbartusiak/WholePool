package com.jba.dao2.ride.dao;

import com.jba.dao2.DAOConfig;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
    }

    @Test
    public void getAllRides() {
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
}