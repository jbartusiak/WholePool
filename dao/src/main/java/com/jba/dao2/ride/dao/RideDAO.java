package com.jba.dao2.ride.dao;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.ride.enitity.RidePassangers;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.user.enitity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RideDAO {

    @Transactional
    Ride getRideById(int id);

    @Transactional
    Set<Ride> getAllRides();

    @Transactional
    Set<Ride> findRideByCriteria(
            Route route,
            Optional<Date> dateOfDeparture,
            Optional<Date> dateOfArrival
    );

    @Transactional
    RideDetails getRideDetials(Ride ride);

    @Transactional
    Ride addRide(User offerer, Ride ride);

    @Transactional
    RideDetails addRideDetails(Ride ride, RideDetails rideDetails);

    @Transactional
    Ride deleteRide(Ride ride);

    @Transactional
    RidePassangers registerToRide(User user, Ride ride) throws UnsupportedOperationException;

    @Transactional
    RidePassangers unregisterFromRide(User user, Ride ride);

    @Transactional
    Set<Ride> getRidesByUser(User user);
}
