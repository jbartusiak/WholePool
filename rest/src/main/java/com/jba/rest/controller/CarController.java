package com.jba.rest.controller;

import com.jba.dao2.cars.entity.Car;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.CarService;
import com.jba.service.ifs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getCar(
            @RequestParam(value = "carId", required = false) Integer carId
    ){
        if(carId==null)
            return new WPLResponse<>(HttpStatus.OK, carService.getAllCars());
        else
            return new WPLResponse<>(HttpStatus.OK, carService.getCarById(carId));
    }

    @GetMapping("/type")
    public WPLResponse getCarType(
            @RequestParam(value = "carTypeId", required = false) Integer carTypeId
    ){
        if(carTypeId==null)
            return new WPLResponse<>(HttpStatus.OK, carService.getAllCarTypes());
        else
            return new WPLResponse<>(HttpStatus.OK, carService.getCarTypeById(carTypeId));
    }
}
