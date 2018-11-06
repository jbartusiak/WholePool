package com.jba.service.ifs;

import com.jba.dao2.cars.entity.Car;
import com.jba.dao2.cars.entity.CarType;
import com.jba.dao2.cars.entity.UsersCars;
import com.jba.dao2.user.enitity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface CarService {
    CarType addNewCarType(CarType carType);
    CarType updateCarType(CarType carType);
    CarType getCarTypeById(int id);
    CarType deleteCarType(CarType carType);

    List<Car> getAllCars();
    Car getCarById(int id);
    List<Car> getUsersCars(User user);
    Car addUsersCar(User user, Car car);
    Car deleteUsersCar(User user, Car car);
}
