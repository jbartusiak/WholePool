package com.jba.rest.controller;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.user.enitity.User;
import com.jba.entity.WPLResponse;
import com.jba.service.entity.SearchCriteria;
import com.jba.service.ifs.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("/api/ride")
public class RideController {

    @Autowired
    private RideService rideService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getRide(
            @RequestParam(name = "rideId", required = false) Integer rideId,
            @RequestParam(name = "userId", required = false) Integer userId
    ){
        if(rideId!=null)
            return new WPLResponse<>(HttpStatus.OK, rideService.getRideById(rideId));
        else if (userId!=null){
            return new WPLResponse<>(HttpStatus.OK, rideService.getRidesByUser(userId), Ride.class);
        }
        else return new WPLResponse<>(HttpStatus.OK, rideService.getAllRides(), Ride.class);
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse findRide(
            @RequestParam(name = "routeId", required = true) Integer routeId,
            @RequestParam(name = "dateOfDeparture", required = false) String dateOfDeparture,
            @RequestParam(name = "dateOfArrival", required = false) String dateOfArrival
    ){
        SearchCriteria searchCriteria = new SearchCriteria(Route.of(routeId));
        if(dateOfDeparture!=null) searchCriteria.setDOD(Date.valueOf(dateOfDeparture));
        if(dateOfArrival!=null) searchCriteria.setDOA(Date.valueOf(dateOfArrival));

        return new WPLResponse<>(HttpStatus.OK, rideService.findRideByCriteria(searchCriteria), Ride.class);
    }

    @GetMapping("/passengers")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getPassengers(
            @RequestParam(name="rideId", required = true) Integer rideId
    ){
        return new WPLResponse<>(HttpStatus.OK, rideService.getPassangersForRide(rideId), User.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addRide(
            @RequestParam(name = "userId", required = true) Integer userId,
            @RequestBody(required = true) Ride ride
    ){
        return new WPLResponse<>(HttpStatus.CREATED, rideService.addRide(userId, ride));
    }

    @PostMapping("/details")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addRideDetails(
            @RequestParam(name="rideId", required = true) Integer rideId,
            @RequestBody(required = true) RideDetails rideDetails
    ){
        return new WPLResponse<>(HttpStatus.CREATED, rideService.addRideDetails(rideId, rideDetails));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteRide(
            @RequestParam(name="rideId", required = true) Integer rideId
    ){
        rideService.deleteRide(rideId);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse registerToRide(
        @RequestParam(name="userId", required = true) Integer userId,
        @RequestParam(name = "rideId", required = true) Integer rideId
    ){
        return new WPLResponse<>(HttpStatus.CREATED, rideService.registerToRide(userId, rideId));
    }

    @DeleteMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void unregisterFromRide(
            @RequestParam(name = "userId", required = true) Integer userId,
            @RequestParam(name = "rideId", required = true) Integer rideId
    ){
        rideService.unregisterFromRide(userId, rideId);
    }
}
