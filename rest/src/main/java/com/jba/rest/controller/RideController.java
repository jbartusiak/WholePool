package com.jba.rest.controller;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.route.entity.Route;
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
            @RequestParam(name = "rideId", required = false) Integer rideId
    ){
        if(rideId!=null)
            return new WPLResponse<>(HttpStatus.OK, rideService.getRideById(rideId));
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
}
