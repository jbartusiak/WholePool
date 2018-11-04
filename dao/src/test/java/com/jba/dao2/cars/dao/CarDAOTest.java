package com.jba.dao2.cars.dao;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.cars.entity.Car;
import com.jba.dao2.cars.entity.CarType;
import com.jba.dao2.user.dao.UserDAO;
import com.jba.dao2.user.enitity.User;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class CarDAOTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CarDAO carDAO;

    private final static Logger logger = Logger.getLogger(CarDAOTest.class);

    @Test
    public void addCar() {
        CarType carType = CarType.of(1);

        try {
            logger.info("Creating cars");
            Car car = new Car(carType, "Samochodo", "ARN000111", 1991);
            Car car2 = new Car(carType, "Samochodo2", "ARN000112", 1991);
            Car car3 = new Car(carType, "Samochodo3", "ARN000113", 1991);

            logger.info("Persisting cars");
            car = carDAO.addCar(car, User.of(1));
            car2 = carDAO.addCar(car2, User.of(1));
            car3 = carDAO.addCar(car3, User.of(1));

            logger.info("Checking assertions");
            assertNotEquals(0, car.getCarId());
            assertNotEquals(0, car2.getCarId());
            assertNotEquals(0, car3.getCarId());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void addNewCarType() {
        CarType carType = new CarType("Nowy rodzaj samochodu");
        try{
            logger.info("Persisting carType");
            carType = carDAO.addNewCarType(carType);

            logger.info("Checking assertions");
            assertNotEquals(0, carType.getCarTypeId());

            logger.info("Deleting carType");
            carType = carDAO.deleteCarType(CarType.of(carType.getCarTypeId()));
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void updateCarType() {
        CarType carType;
        try {
            carType = carDAO.getCarTypeById(1);
            String value = carType.getCarTypeName();

            carType.setCarTypeName("Yellow");

            logger.info("Updating carTypeName");
            carDAO.updateCarType(carType);

            CarType fromDB = carDAO.getCarTypeByName("Yellow");

            assertEquals(carType.getCarTypeId(), fromDB.getCarTypeId());

            logger.info("Reverting changes");
            carType.setCarTypeName(value);
            carDAO.updateCarType(carType);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void updateCar() {
        try {
            logger.info("Getting car and remembering the initial value");
            Car car = carDAO.getCarById(1);
            String initialValue = car.getCarName();

            logger.info("Updating car");
            car.setCarName("Testowa nazwa");
            carDAO.updateCar(car);

            logger.info("Re-querying DB");
            Car fromDB = carDAO.getCarById(1);

            logger.info("Checking assertions");
            assertEquals(fromDB.getCarId(),car.getCarId());
            assertEquals(fromDB.getCarName(),car.getCarName());

            logger.info("Rolling back changes");
            car.setCarName(initialValue);
            carDAO.updateCar(car);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void getUsersCars() {
        try {
            logger.info("Querying DB");
            User user = userDAO.getUserById(1);
            List<Car> cars = carDAO.getUsersCars(user);

            logger.info("Checking assertions");
            assertNotEquals(0, cars.size());

            logger.info("Results: ");
            for(Car c: cars){
                logger.info("Got car: "+c);
            }
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void deleteCar() {
        try{
            logger.info("Fetching users cars");
            List<Car> cars = carDAO.getUsersCars(User.of(1));

            int random = (int)(Math.random()*cars.size());
            logger.info("Removing "+random+" position.");

            int deletedCarId = cars.get(random).getCarId();
            carDAO.deleteCar(cars.get(random));
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}