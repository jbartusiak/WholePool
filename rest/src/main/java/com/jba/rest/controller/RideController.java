package com.jba.rest.controller;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.user.enitity.User;
import com.jba.dao2.entity.WPLResponse;
import com.jba.service.entity.SearchCriteria;
import com.jba.service.ifs.RideService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/ride")
public class RideController {

    @Autowired
    private RideService rideService;

    @ApiOperation(value = "Get ride(s)", notes = "Fetches ride(s) from the database. Consumes optional parameters " +
            "rideId and userId. If rideId is given, fetches a single ride of that id. If userId is given, fetches " +
            "rides offered by that user. If no parameters are given, returns a list of all rides available in the " +
            "system.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getRide(
            @ApiParam(name = "rideId", value="An Integer referring to Ride entity", required = false,
                    type = "Integer")
            @RequestParam(name = "rideId", required = false) Integer rideId,
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = false,
                    type = "Integer")
            @RequestParam(name = "userId", required = false) Integer userId
    ){
        if(rideId!=null)
            return new WPLResponse<>(HttpStatus.OK, rideService.getRideById(rideId));
        else if (userId!=null){
            return new WPLResponse<>(HttpStatus.OK, rideService.getRidesByUser(userId), Ride.class);
        }
        else return new WPLResponse<>(HttpStatus.OK, rideService.getAllRides(), Ride.class);
    }

    @ApiOperation(value = "Get ride detail(s)", notes = "Fetches ride details from the database. If rideId is " +
            "specified, returns only details for that given ride.")
    @GetMapping("/details")
    public WPLResponse getRideDetails(
        @ApiParam(name="rideId", value = "An Integer reffering to Ride entity", required = false, type = "Integer")
        @RequestParam(name="rideId", required = false) Integer rideId
    ){
        if(rideId!=null)
            return new WPLResponse<>(HttpStatus.OK, rideService.getRideDetails(rideId));
        return new WPLResponse<>(HttpStatus.OK, rideService.getAllRideDetails(), RideDetails.class);
    }

    @GetMapping("/offerer")
    public WPLResponse getRideOfferer(
        @RequestParam(name="rideId", required = true) Integer rideId
    ){
        return new WPLResponse<>(HttpStatus.OK, rideService.getRideOfferer(rideId));
    }

    @ApiOperation(value = "Find ride(s)", notes = "Finds ride(s) matching the criteria. Consumes routeId, " +
            "dateOfDeparture and dateOfArrival parameters. Only routeId is required to run the search.")
    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse findRide(
            @ApiParam(name = "routeFrom", value="An String referring to Route entity", required = true,
                    type = "String")
            @RequestParam(name = "routeFrom", required = true) String routeFrom,
            @ApiParam(name = "routeTo", value="An String referring to Route entity", required = true,
                    type = "routeTo")
            @RequestParam(name = "routeTo", required = true) String routeTo,
            @ApiParam(name = "dateOfDeparture", value="A string representing date in format YYYY-MM-DDTHH24:MI:SS",
                    required = false, type = "LocalDateTime")
            @RequestParam(name = "dateOfDeparture", required = false) String dateOfDeparture,
            @ApiParam(name = "dateOfArrival", value="A string representing date in format YYYY-MM-DDTHH24:MI:SS",
                    required = false, type = "LocalDateTime")
            @RequestParam(name = "dateOfArrival", required = false) String dateOfArrival
    ){
        LocalDateTime DOD = null;
        LocalDateTime DOA = null;

        if(dateOfDeparture!=null)
            DOD = LocalDateTime.parse(dateOfDeparture);

        if(dateOfArrival!=null)
            DOA = LocalDateTime.parse(dateOfArrival);

        return new WPLResponse<>(HttpStatus.OK, rideService.findRideByCriteria(routeFrom, routeTo, DOD, DOA), RideDetails.class);
    }

    @ApiOperation(value = "Get passenger(s) for a ride", notes = "Gets passengers for given rideId.")
    @GetMapping("/passengers")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getPassengers(
            @ApiParam(name = "rideId", value="An Integer referring to Ride entity", required = true,
                    type = "Integer")
            @RequestParam(name="rideId", required = true) Integer rideId
    ){
        return new WPLResponse<>(HttpStatus.OK, rideService.getPassangersForRide(rideId), User.class);
    }

    @ApiOperation(value = "Adds a new ride", notes = "Creates and saves a new ride in the system.",
            consumes = "application/json")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addRide(
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
            @RequestParam(name = "userId", required = true) Integer userId,
            @ApiParam(name = "ride", value="A JSON representing the Ride entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) Ride ride
    ){
        return new WPLResponse<>(HttpStatus.CREATED, rideService.addRide(userId, ride));
    }

    @ApiOperation(value = "Adds ride's details", notes = "Creates and saves a ride details in the system.",
            consumes = "application/json")
    @PostMapping("/details")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addRideDetails(
            @ApiParam(name = "rideId", value="An Integer referring to Ride entity", required = true,
                    type = "Integer")
            @RequestParam(name="rideId", required = true) Integer rideId,
            @ApiParam(name = "ride", value="A JSON representing the RideDetails entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) RideDetails rideDetails
    ){
        return new WPLResponse<>(HttpStatus.CREATED, rideService.addRideDetails(rideId, rideDetails));
    }

    @ApiOperation(value = "Deletes a ride", notes = "Deletes a ride from the system.")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteRide(
            @ApiParam(name = "rideId", value="An Integer referring to Ride entity", required = true,
                    type = "Integer")
            @RequestParam(name="rideId", required = true) Integer rideId
    ){
        rideService.deleteRide(rideId);
    }

    @ApiOperation(value = "Register for ride", notes="Registers a user as a passenger for ride given by rideId")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse registerToRide(
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
            @RequestParam(name="userId", required = true) Integer userId,
            @ApiParam(name = "rideId", value="An Integer referring to Ride entity", required = true,
                    type = "Integer")
            @RequestParam(name = "rideId", required = true) Integer rideId
    ){
        return new WPLResponse<>(HttpStatus.CREATED, rideService.registerToRide(userId, rideId));
    }

    @ApiOperation(value = "Unregister from ride", notes="Unregisters a user as a passenger for ride given by rideId")
    @DeleteMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void unregisterFromRide(
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
            @RequestParam(name = "userId", required = true) Integer userId,
            @ApiParam(name = "rideId", value="An Integer referring to Ride entity", required = true,
                    type = "Integer")
            @RequestParam(name = "rideId", required = true) Integer rideId
    ){
        rideService.unregisterFromRide(userId, rideId);
    }

    @ApiOperation(value = "Get rides in which user is a passenger", notes = "Returns a list of rides for which the " +
            "given by parameter is a passenger. Boolean parameter decides whether to trim rides which happened in " +
            "the past")
    @GetMapping("/passenger")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getRidesForUser(
        @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
        @RequestParam(name="userId", required = true) Integer userId,
        @ApiParam(name = "trim", value="A Boolean which decides whether to trim old rides", required = true,
                type = "Boolean")
        @RequestParam(name = "trim", required = true) Boolean trim
    ){
        return new WPLResponse<>(HttpStatus.OK, rideService.getRidesForUser(userId, trim), RideDetails.class);
    }
}
