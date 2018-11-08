package com.jba.service;

import com.jba.dao2.cars.dao.CarDAO;
import com.jba.dao2.cars.entity.Car;
import com.jba.dao2.cars.entity.CarType;
import com.jba.dao2.user.enitity.User;
import com.jba.service.ifs.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarDAO carDAO;

    @Override
    public CarType addNewCarType(CarType carType) {
        carType.setCarTypeId(0);
        return carDAO.addNewCarType(carType);
    }

    @Override
    public CarType updateCarType(CarType carType) {
        return carDAO.updateCarType(carType);
    }

    @Override
    public List<CarType> getAllCarTypes() {
        return carDAO.getAllCarTypes().stream().collect(Collectors.toList());
    }

    @Override
    public CarType getCarTypeById(int id) {
        return carDAO.getCarTypeById(id);
    }

    @Override
    public CarType deleteCarType(CarType carType) {
        return carDAO.deleteCarType(carType);
    }

    @Override
    public List<Car> getAllCars() {
        return carDAO.getAllCars();
    }

    @Override
    public Car getCarById(int id) {
        return carDAO.getCarById(id);
    }

    @Override
    public List<Car> getUsersCars(User user) {
        return carDAO.getUsersCars(user);
    }

    @Override
    public Car addUsersCar(User user, Car car) {
        return carDAO.addCar(car, user);
    }

    @Override
    public Car deleteUsersCar(User user, Car car) {
        car = getCarById(car.getCarId());
        carDAO.deleteUsersCar(user, car);
        carDAO.deleteCar(car);
        return car;
    }
}
