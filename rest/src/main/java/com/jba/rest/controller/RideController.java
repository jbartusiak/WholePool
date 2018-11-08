package com.jba.rest.controller;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
