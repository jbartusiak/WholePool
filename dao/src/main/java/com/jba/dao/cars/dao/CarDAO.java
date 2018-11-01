package com.jba.dao.cars.dao;

import com.jba.dao.cars.entity.Car;
import com.jba.dao.cars.entity.CarType;
import com.jba.dao.cars.entity.UsersCars;
import com.jba.dao.user.enitity.User;
import com.jba.session.DBUtils;
import com.jba.session.WPLSessionFactory;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CarDAO {

    private final static Logger logger = Logger.getLogger(CarDAO.class);

    public static Car addCar(Car car, User user){
        car = (Car) DBUtils.saveOrUpdate(car);
        logger.info("Persisted "+car);

        UsersCars usersCar = new UsersCars(user,car);
        usersCar = (UsersCars) DBUtils.saveOrUpdate(usersCar);
        logger.info("Persisted "+usersCar);

        return car;
    }

    public static CarType addNewCarType(CarType type){
        logger.info("Persisting "+type);
        return (CarType) DBUtils.saveOrUpdate(type);
    }

    public static CarType updateCarType(CarType carType){
        logger.info("Updating carType of id "+carType.getCarTypeId());
        return (CarType) DBUtils.saveOrUpdate(carType);
    }

    public static Car getCarById(int id){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            Car result = session.
                    createQuery("from Car c where c.carId=:id", Car.class).
                    setParameter("id", id).
                    getSingleResult();

            session.getTransaction().commit();

            return result;
        }
        catch (Exception e){
            logger.error("Error fetching car of id "+id, e);
            return null;
        }
    }

    public static Car updateCar(Car car){
        logger.info("Updateing car of id "+car.getCarId());
        return (Car) DBUtils.saveOrUpdate(car);
    }

    public static List<Car> getUsersCars(User user){
        Session session = WPLSessionFactory.getDBSession();

        try(session) {
            session.beginTransaction();
            List<UsersCars> usersCars = session.
                    createQuery("from UsersCars u where u.user=:user", UsersCars.class).
                    setParameter("user", user).
                    getResultList();

            logger.info("Fetched "+usersCars.size()+ " results.");

            List<Car> result = new ArrayList<>();

            usersCars.forEach(car->result.add(car.getCar()));
            session.getTransaction().commit();
            return result;
        }
        catch (Exception e){
            logger.error("Failed querying for users cars!",e);
            return new ArrayList<>();
        }
    }

    public static void deleteCar(Car car){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            UsersCars usersCar = session.
                    createQuery("from UsersCars u where u.car=:car", UsersCars.class).
                    setParameter("car", car).
                    getSingleResult();

            logger.info("Deleting user-car association: "+usersCar);

            session.delete(usersCar);

            session.getTransaction().commit();
            session.clear();
        }
        catch (Exception e){
            logger.error("Failed to delete user-car association", e);
        }

        Session session2 = WPLSessionFactory.getDBSession();
        try(session2){
            session2.beginTransaction();

            logger.info("Deleting car "+car);

            session2.delete(car);

            session2.getTransaction().commit();
        }
        catch (Exception e){
            logger.error("Failed to delete car "+car, e);
        }

        logger.info("Deletion of car "+car+" successfull!");
    }

    private static Set<CarType> getAllCarTypes(){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            Set<CarType> carTypes = session.
                    createQuery("from CarType ", CarType.class).
                    getResultStream().
                    collect(Collectors.toSet());

            session.getTransaction().commit();

            return carTypes;
        }
        catch (Exception e){
            logger.error("Error while fetching car types!", e);
            return new HashSet<>();
        }
    }

    public static CarType getCarTypeById(int id){
        Set<CarType> carTypes = getAllCarTypes();

        for(CarType c:carTypes){
            if(c.getCarTypeId()==id)
                return c;
        }
        return null;
    }

    public static CarType getCarTypeByName(String name){
        Set<CarType> carTypes = getAllCarTypes();

        for(CarType c:carTypes){
            if(c.getCarTypeName().equals(name))
                return c;
        }
        return null;
    }

    public static CarType deleteCarType(CarType carType){
        Session session = WPLSessionFactory.getDBSession();
        try(session){
            session.beginTransaction();

            session.delete(carType);

            session.getTransaction().commit();
        }
        catch (Exception e){
            logger.error("Error trying to delete carType "+carType, e);
        }
        return carType;
    }
}
