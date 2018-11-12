package com.jba.rest.controller;

import com.jba.dao2.cars.entity.Car;
import com.jba.dao2.cars.entity.CarType;
import com.jba.dao2.user.enitity.User;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.CarService;
import com.jba.service.ifs.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Get car from database", notes = "Fetches cars from DB. If given no parameters, all cars " +
            "available will be returned. If carId is given, returns a single car. If userId is given, returns all " +
            "cars owned by that user. carId and userId should not be present at the same time, as the APIs checks " +
            "first for carId, then for userId."
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getCar(
            @ApiParam(name= "carId", value="An Integer referring to Car entity", type = "Integer")
            @RequestParam(value = "carId", required = false) Integer carId,
            @ApiParam(name="userId", value = "An integer referring to User entity", type = "Integer")
            @RequestParam(value = "userId", required = false) Integer userId
    ) {
        if (carId != null)
            return new WPLResponse<>(HttpStatus.OK, carService.getCarById(carId));
        else if (userId != null)
            return new WPLResponse<>(HttpStatus.OK, carService.getUsersCars(User.of(userId)));
        else
            return new WPLResponse<>(HttpStatus.OK, carService.getAllCars());
    }

    @ApiOperation(value = "Register a new car", notes = "Adds a new car with association to the user. No car in the " +
            "system can exist without a owner.", consumes = "application/json")
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addUsersCar(
            @ApiParam(name="userId", value = "An integer referring to User entity", type = "Integer", required = true)
            @RequestParam(value = "userId", required = true) Integer userId,
            @ApiParam(name="car", value = "A JSON object representing the Car entity", type = "application/json",
                    required = true)
            @RequestBody(required = true) Car car
    ){
        return new WPLResponse<>(HttpStatus.CREATED, carService.addUsersCar(userService.getUser(userId), car));
    }

    @ApiOperation(value = "Delete a car", notes = "Removes a car from the system, and deletes a user-car association.")
    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUsersCar(
            @ApiParam(name="userId", value = "An integer referring to User entity", required = true, type = "Integer")
            @RequestParam(value = "userId", required = true) Integer userId,
            @ApiParam(name="carId", value = "An integer referring to Car entity", required = true, type = "Integer")
            @RequestParam(value = "carId", required = true) Integer carId
    ){
        carService.deleteUsersCar(User.of(userId), Car.of(carId));
    }

    @ApiOperation(value = "Get car type(s)", notes = "Gets car types available in the system. Consumes an optional " +
            "parameter carTypeId.")
    @GetMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getCarType(
            @ApiParam(name="carTypeId", value = "An integer referring to CarType entity", required = false, type = "Integer")
            @RequestParam(value = "carTypeId", required = false) Integer carTypeId
    ) {
        if (carTypeId == null)
            return new WPLResponse<>(HttpStatus.OK, carService.getAllCarTypes());
        else
            return new WPLResponse<>(HttpStatus.OK, carService.getCarTypeById(carTypeId));
    }

    @ApiOperation(value = "Add a new car type", notes = "Adds a new car type to the system", consumes = "application/json")
    @PostMapping("/type")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addNewCarType(
            @ApiParam(name="carType", value = "A JSON object representing the Car entity", type = "application/json",
                    required = true)
            @RequestBody(required = true) CarType carType
    ) {
        return new WPLResponse<>(HttpStatus.CREATED,carService.addNewCarType(carType));
    }

    @ApiOperation(value = "Update car type", notes = "Updates a car type already existing in the system",
            consumes = "application/json")
    @PutMapping("/type")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WPLResponse updateCarType(
            @ApiParam(name="carType", value = "A JSON object representing the Car entity", required = true, type = "application/json")
            @RequestBody(required = true) CarType carType
    ){
        return new WPLResponse<>(HttpStatus.ACCEPTED, carService.updateCarType(carType));
    }

    @ApiOperation(value = "Delete a car type", notes = "Deletes a car type from the system")
    @DeleteMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse deleteCarType(
            @ApiParam(name="carTypeId", value = "An integer referring to CarType entity", required = true, type = "Integer")
            @RequestParam(value = "carTypeId", required = true) Integer carTypeId
    ){
        return new WPLResponse<>(HttpStatus.OK, carService.deleteCarType(CarType.of(carTypeId)));
    }
}
