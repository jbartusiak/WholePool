package com.jba.service;

import com.jba.dao2.ride.dao.RideDAO;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.ride.enitity.RidePassangers;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.user.enitity.User;
import com.jba.service.entity.SearchCriteria;
import com.jba.service.ifs.RideService;
import com.jba.service.ifs.SearchService;
import com.jba.service.ifs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RideServiceImpl implements RideService {

    @Autowired
    private RideDAO rideDAO;

    @Autowired
    private SearchService searchService;

    @Autowired
    UserService userService;

    @Override
    public Ride getRideById(Integer rideId) {
        return rideDAO.getRideById(rideId);
    }

    @Override
    public List<Ride> getAllRides() {
        return rideDAO.getAllRides();
    }

    @Override
    public List<RideDetails> findRideByCriteria(String routeFrom, String routeTo, LocalDateTime dateOfDeparture, LocalDateTime dateOfArrival){
        List<Route> routes = searchService.findRouteByCriteria(routeFrom, routeTo);

        return rideDAO
                .findRideByCriteria(
                        routes,
                        dateOfDeparture,
                        dateOfArrival
                );
    }

    @Override
    public List<RideDetails> getAllRideDetails() {
        return rideDAO.getAllRideDetails();
    }

    @Override
    public RideDetails getRideDetails(Integer rideId) {
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
        rideDAO.deleteRideDetails(getRideById(rideId));
        rideDAO.deleteOfferedRides(getRideById(rideId));
        rideDAO.removePassengers(getRideById(rideId));
        return rideDAO.deleteRide(getRideById(rideId));
    }

    @Override
    public List<User> getPassangersForRide(Integer rideId) {
        return rideDAO.getRidePassengers(getRideById(rideId));
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
    public List<Ride> getRidesByUser(Integer userId) {
        return rideDAO.getRidesByUser(User.of(userId));
    }

    @Override
    public User getRideOfferer(Integer rideId) {
        return rideDAO.getRideOfferer(getRideById(rideId));
    }

    @Override
    public List<RideDetails> getRidesForUser(Integer userId, Boolean trimToTime) {
        return rideDAO.getRidesForUser(User.of(userId), trimToTime);
    }
}
