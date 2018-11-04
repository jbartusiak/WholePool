package com.jba.dao2.cars.dao;


import com.jba.dao2.cars.entity.Car;
import com.jba.dao2.cars.entity.CarType;
import com.jba.dao2.cars.entity.UsersCars;
import com.jba.dao2.user.enitity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CarDAOMySQLRepository implements CarDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private final static Logger logger = Logger.getLogger(CarDAOMySQLRepository.class);

    public Car addCar(Car car, User user) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(car);
        logger.info("Persisted " + car);

        UsersCars usersCar = new UsersCars(user, car);
        session.saveOrUpdate(usersCar);
        logger.info("Persisted " + usersCar);

        return car;
    }

    public CarType addNewCarType(CarType type) {
        logger.info("Persisting " + type);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(type);
        return type;
    }

    public CarType updateCarType(CarType carType) {
        logger.info("Updating carType of id " + carType.getCarTypeId());
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(carType);
        return carType;
    }

    public Car getCarById(int id) {
        Session session = sessionFactory.getCurrentSession();

        try {
            Car result = session.
                    createQuery("from Car c where c.carId=:id", Car.class).
                    setParameter("id", id).
                    getSingleResult();
            return result;
        } catch (Exception e) {
            logger.error("Error fetching car of id " + id, e);
            return null;
        }
    }

    public Car updateCar(Car car) {
        logger.info("Updateing car of id " + car.getCarId());
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(car);
        return car;
    }

    public List<Car> getUsersCars(User user) {
        Session session = sessionFactory.getCurrentSession();

        try {
            List<UsersCars> usersCars = session.
                    createQuery("from UsersCars u where u.user=:user", UsersCars.class).
                    setParameter("user", user).
                    getResultList();

            logger.info("Fetched " + usersCars.size() + " results.");

            List<Car> result = new ArrayList<>();

            usersCars.forEach(car -> result.add(car.getCar()));
            return result;
        } catch (Exception e) {
            logger.error("Failed querying for users cars!", e);
            return new ArrayList<>();
        }
    }

    public void deleteCar(Car car) {
        Session session = sessionFactory.getCurrentSession();

        int carId = car.getCarId();

        try {
            UsersCars usersCars = session.
                    createQuery("from UsersCars u where u.car=:car", UsersCars.class).
                    setParameter("car", car).
                    getSingleResult();

            logger.info("Deleting user-car association: " + usersCars);

            session.delete(usersCars);
        } catch (Exception e) {
            logger.error("Failed to delete user-car association", e);
        }

        Session session2 = sessionFactory.getCurrentSession();
        try {
            logger.info("Deleting car " + car);
            session2.delete(Car.of(carId));
        } catch (Exception e) {
            logger.error("Failed to delete car " + car, e);
        }
        logger.info("Deletion of car " + car + " successfull!");
    }

    public Set<CarType> getAllCarTypes() {
        Session session = sessionFactory.getCurrentSession();

        try {
            Set<CarType> carTypes = session.
                    createQuery("from CarType ", CarType.class).
                    getResultStream().
                    collect(Collectors.toSet());

            return carTypes;
        } catch (Exception e) {
            logger.error("Error while fetching car types!", e);
            return new HashSet<>();
        }
    }

    public CarType getCarTypeById(int id) {
        Set<CarType> carTypes = getAllCarTypes();

        for (CarType c : carTypes) {
            if (c.getCarTypeId() == id)
                return c;
        }
        return null;
    }

    public CarType getCarTypeByName(String name) {
        Set<CarType> carTypes = getAllCarTypes();

        for (CarType c : carTypes) {
            if (c.getCarTypeName().equals(name))
                return c;
        }
        return null;
    }

    public CarType deleteCarType(CarType carType) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.delete(carType);
        } catch (Exception e) {
            logger.error("Error trying to delete carType " + carType, e);
        }
        return carType;
    }
}
