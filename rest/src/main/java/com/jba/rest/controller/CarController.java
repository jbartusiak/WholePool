package com.jba.rest.controller;

import com.jba.dao2.cars.entity.CarType;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.CarService;
import com.jba.service.ifs.UserService;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getCar(
            @RequestParam(value = "carId", required = false) Integer carId
    ) {
        if (carId == null)
            return new WPLResponse<>(HttpStatus.OK, carService.getAllCars());
        else
            return new WPLResponse<>(HttpStatus.OK, carService.getCarById(carId));
    }

    @GetMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getCarType(
            @RequestParam(value = "carTypeId", required = false) Integer carTypeId
    ) {
        if (carTypeId == null)
            return new WPLResponse<>(HttpStatus.OK, carService.getAllCarTypes());
        else
            return new WPLResponse<>(HttpStatus.OK, carService.getCarTypeById(carTypeId));
    }

    @PostMapping("/type")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addNewCarType(
            @RequestBody(required = true) CarType carType
    ) {
        return new WPLResponse<>(HttpStatus.CREATED,carService.addNewCarType(carType));
    }

    @PutMapping("/type")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WPLResponse updateCarType(
            @RequestBody(required = true) CarType carType
    ){
        return new WPLResponse<>(HttpStatus.ACCEPTED, carService.updateCarType(carType));
    }

    @DeleteMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse deleteCarType(
            @RequestParam(value = "carTypeId", required = true) Integer carTypeId
    ){
        return new WPLResponse<>(HttpStatus.OK, carService.deleteCarType(CarType.of(carTypeId)));
    }
}
