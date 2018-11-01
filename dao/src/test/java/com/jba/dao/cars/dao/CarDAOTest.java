package com.jba.dao.cars.dao;

import com.jba.dao.cars.entity.Car;
import com.jba.dao.cars.entity.CarType;
import com.jba.dao.user.dao.UserDAO;
import com.jba.dao.user.enitity.User;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarDAOTest {

    private final static Logger logger = Logger.getLogger(CarDAOTest.class);

    @Test
    void addCar() {
        User user = UserDAO.getUserById(1);

        CarType carType = CarType.of(1);

        try {
            logger.info("Creating cars");
            Car car = new Car(carType, "Samochodo", "ARN000111", 1991);
            Car car2 = new Car(carType, "Samochodo2", "ARN000112", 1991);
            Car car3 = new Car(carType, "Samochodo3", "ARN000113", 1991);

            logger.info("Persisting cars");
            car = CarDAO.addCar(car, user);
            car2 = CarDAO.addCar(car2, user);
            car3 = CarDAO.addCar(car3, user);

            logger.info("Checking assertions");
            assertNotEquals(0, car.getCarId());
            assertNotEquals(0, car2.getCarId());
            assertNotEquals(0, car3.getCarId());
        }
        catch (Exception e){
            fail(e);
        }
    }

    @Test
    void addNewCarType() {
        CarType carType = new CarType("Nowy rodzaj samochodu");
        try{
            logger.info("Persisting carType");
            carType = CarDAO.addNewCarType(carType);

            logger.info("Checking assertions");
            assertNotEquals(0, carType.getCarTypeId());

            logger.info("Deleting carType");
            carType = CarDAO.deleteCarType(carType);
        }
        catch (Exception e){
            fail(e);
        }
    }

    @Test
    void updateCarType() {
        CarType carType;
        try {
            carType = CarDAO.getCarTypeById(1);
            String value = carType.getCarTypeName();

            carType.setCarTypeName("Yellow");

            logger.info("Updating carTypeName");
            CarDAO.updateCarType(carType);

            CarType fromDB = CarDAO.getCarTypeByName("Yellow");

            assertEquals(carType.getCarTypeId(), fromDB.getCarTypeId());

            logger.info("Reverting changes");
            carType.setCarTypeName(value);
            CarDAO.updateCarType(carType);
        }
        catch (Exception e){
            fail(e);
        }
    }

    @Test
    void updateCar() {
        try {
            logger.info("Getting car and remembering the initial value");
            Car car = CarDAO.getCarById(1);
            String initialValue = car.getCarName();

            logger.info("Updating car");
            car.setCarName("Testowa nazwa");
            CarDAO.updateCar(car);

            logger.info("Re-querying DB");
            Car fromDB = CarDAO.getCarById(1);

            logger.info("Checking assertions");
            assertEquals(fromDB.getCarId(),car.getCarId());
            assertEquals(fromDB.getCarName(),car.getCarName());

            logger.info("Rolling back changes");
            car.setCarName(initialValue);
            CarDAO.updateCar(car);
        }
        catch (Exception e){
            fail(e);
        }
    }

    @Test
    void getUsersCars() {
        try {
            logger.info("Querying DB");
            User user = UserDAO.getUserById(1);
            List<Car> cars = CarDAO.getUsersCars(user);

            logger.info("Checking assertions");
            assertNotEquals(0, cars.size());

            logger.info("Results: ");
            for(Car c: cars){
                logger.info("Got car: "+c);
            }
        }
        catch (Exception e){
            fail(e);
        }
    }

    @Test
    void deleteCar() {
        try{
            logger.info("Fetching users cars");
            List<Car> cars = CarDAO.getUsersCars(User.of(1));

            int random = (int)(Math.random()*cars.size());
            logger.info("Removing "+random+" position.");

            int deletedCarId = cars.get(random).getCarId();
            CarDAO.deleteCar(cars.get(random));

            logger.info("Checking if deletion was successfull");
            Car fromDB = CarDAO.getCarById(deletedCarId);
            assertNull(fromDB);
        }
        catch (Exception e){
            fail(e);
        }
    }
}