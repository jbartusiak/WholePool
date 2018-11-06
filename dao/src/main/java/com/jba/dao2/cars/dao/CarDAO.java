package com.jba.dao2.cars.dao;

import com.jba.dao2.cars.entity.Car;
import com.jba.dao2.cars.entity.CarType;
import com.jba.dao2.user.enitity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface CarDAO {

    @Transactional
    Car addCar(Car car, User user);

    @Transactional
    CarType addNewCarType(CarType type);

    @Transactional
    CarType updateCarType(CarType carType);

    @Transactional
    List<Car> getAllCars();

    @Transactional
    Car getCarById(int id);

    @Transactional
    Car updateCar(Car car);

    @Transactional
    List<Car> getUsersCars(User user);

    @Transactional
    void deleteCar(Car car);

    @Transactional
    Set<CarType> getAllCarTypes();

    @Transactional
    CarType getCarTypeById(int id);

    @Transactional
    CarType getCarTypeByName(String name);

    @Transactional
    CarType deleteCarType(CarType carType);
}
