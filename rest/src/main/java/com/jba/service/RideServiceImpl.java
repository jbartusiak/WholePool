package com.jba.service;

import com.jba.dao2.ride.dao.RideDAO;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.ride.enitity.RidePassangers;
import com.jba.dao2.user.enitity.User;
import com.jba.service.entity.SearchCriteria;
import com.jba.service.ifs.RideService;
import com.jba.service.ifs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RideServiceImpl implements RideService {

    @Autowired
    private RideDAO rideDAO;

    @Autowired
    UserService userService;

    @Override
    public Ride getRideById(Integer rideId) {
        return rideDAO.getRideById(rideId);
    }

    @Override
    public Set<Ride> getAllRides() {
        return rideDAO.getAllRides();
    }

    @Override
    public List<Ride> findRideByCriteria(SearchCriteria searchCriteria) {
        return rideDAO
                .findRideByCriteria(
                        searchCriteria.getRoute(),
                        Optional.of(searchCriteria.getDOD()),
                        Optional.of(searchCriteria.getDOA())
                ).stream().collect(Collectors.toList());
    }

    @Override
    public RideDetails getRideDetials(Integer rideId) {
        return rideDAO.getRideDetials(getRideById(rideId));
    }

    @Override
    public Ride addRide(Integer userOffererId, Ride ride) {
        return rideDAO.addRide(userService.getUser(userOffererId), ride);
    }

    @Override
    public RideDetails addRideDetails(Integer rideId, RideDetails rideDetails) {
        return rideDAO.addRideDetails(getRideById(rideId), rideDetails);
    }

    @Override
    public Ride deleteRide(Integer rideId) {
        return rideDAO.deleteRide(getRideById(rideId));
    }

    @Override
    public RidePassangers registerToRide(Integer userId, Integer rideId) throws UnsupportedOperationException {
        return rideDAO.registerToRide(
                userService.getUser(userId),
                getRideById(rideId)
        );
    }

    @Override
    public RidePassangers unregisterFromRide(Integer userId, Integer rideId) {
        return rideDAO.unregisterFromRide(
                userService.getUser(userId),
                getRideById(rideId)
        );
    }

    @Override
    public Set<Ride> getRidesByUser(Integer userId) {
        return rideDAO.getRidesByUser(User.of(userId));
    }
}
