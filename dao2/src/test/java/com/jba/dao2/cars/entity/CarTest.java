package com.jba.dao2.cars.entity;

import com.jba.dao2.Dao2Application;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.fail;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Dao2Application.class })
@WebAppConfiguration
public class CarTest {

    @Autowired
    SessionFactory sessionFactory;

    @Test
    @Transactional
    public void shouldNotCrashAndShowCollectionSize(){
        Session session = sessionFactory.getCurrentSession();
        int count = ((Long) session.createQuery("select count(*) from Car ").getSingleResult()).intValue();
        System.out.println("Select count(*) from Car result: "+count);
        List<Car> cars = session.createQuery("from Car ").getResultList();
        System.out.println("Result set count: "+cars.size());

        cars.forEach(car-> System.out.println(car.toString()));

        assertEquals(count, cars.size());
    }

    @Test
    @Transactional
    public void shouldSuccessfullyAddNewCarType(){
        Session session = sessionFactory.getCurrentSession();
        CarType carType = session.createQuery("from CarType c where c.carTypeId=1", CarType.class).getSingleResult();

        Car newCarToInsert = new Car(carType, "My fancy car", "AKG123456789", 2015);

        try {
            session.save(newCarToInsert);
            Car fromDB = session.createQuery("from Car c where c.carName='My fancy car'", Car.class).getSingleResult();

            assertEquals(newCarToInsert, fromDB);

            System.out.println("Created: " + newCarToInsert.toString());
            System.out.println("Fetched: " + fromDB.toString());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}