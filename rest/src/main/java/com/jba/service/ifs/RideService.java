package com.jba.service.ifs;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.ride.enitity.RidePassangers;
import com.jba.dao2.user.enitity.User;
import com.jba.service.entity.SearchCriteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface RideService {
    Ride getRideById(Integer rideId);
    List<Ride> getAllRides();
    List<RideDetails> findRideByCriteria(Integer routeId, LocalDateTime dateOfDeparture, LocalDateTime dateOfArrival);
    List<RideDetails> getAllRideDetails();
    RideDetails getRideDetails(Integer rideId);
    Ride addRide(Integer userOffererId, Ride ride);
    RideDetails addRideDetails(Integer rideId, RideDetails rideDetails);
    Ride deleteRide(Integer rideId);
    List<User> getPassangersForRide(Integer rideId);
    RidePassangers registerToRide(Integer userId, Integer rideId) throws UnsupportedOperationException;
    RidePassangers unregisterFromRide(Integer userId, Integer rideId);
    List<Ride> getRidesByUser(Integer userId);
    User getRideOfferer(Integer rideId);
    List<RideDetails> getRidesForUser(Integer userId, Boolean trimToTime);
}
