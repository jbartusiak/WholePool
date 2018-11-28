package com.jba.dao2.ride.dao;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.ride.enitity.RidePassangers;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.user.enitity.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@EnableTransactionManagement
public interface RideDAO {

    @Transactional
    Ride getRideById(int id);

    @Transactional
    List<Ride> getAllRides();

    @Transactional
    List<Ride> findRideByCriteria(
            Route route,
            @Nullable LocalDateTime dateOfDeparture,
            @Nullable LocalDateTime dateOfArrival
    );

    @Transactional
    List<RideDetails> getAllRideDetails();

    @Transactional
    RideDetails getRideDetials(Ride ride);

    @Transactional
    Ride addRide(User offerer, Ride ride);

    @Transactional
    RideDetails addRideDetails(Ride ride, RideDetails rideDetails);

    @Transactional
    Ride deleteRide(Ride ride);

    @Transactional
    Ride deleteOfferedRides(Ride ride);

    @Transactional
    Ride removePassengers(Ride ride);

    @Transactional
    Ride deleteRideDetails(Ride ride);

    @Transactional
    List<User> getRidePassengers(Ride ride);

    @Transactional
    RidePassangers registerToRide(User user, Ride ride) throws UnsupportedOperationException;

    @Transactional
    RidePassangers unregisterFromRide(User user, Ride ride);

    @Transactional
    List<Ride> getRidesByUser(User user);

    @Transactional
    User getRideOfferer(Ride ride);

    @Transactional
    List<RideDetails> getUpcomingRidesForUser(User user);
}
