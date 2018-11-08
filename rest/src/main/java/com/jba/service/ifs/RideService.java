package com.jba.service.ifs;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.ride.enitity.RidePassangers;
import com.jba.service.entity.SearchCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface RideService {
    Ride getRideById(Integer rideId);
    List<Ride> getAllRides();
    List<Ride> findRideByCriteria(SearchCriteria searchCriteria);

    @Deprecated
    RideDetails getRideDetials(Integer rideId);

    Ride addRide(Integer userOffererId, Ride ride);
    RideDetails addRideDetails(Integer rideId, RideDetails rideDetails);
    Ride deleteRide(Integer rideId);
    RidePassangers registerToRide(Integer userId, Integer rideId) throws UnsupportedOperationException;
    RidePassangers unregisterFromRide(Integer userId, Integer rideId);
    Set<Ride> getRidesByUser(Integer userId);
}
