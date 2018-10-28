package com.jba.dao.cars.entity;

import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void shouldNotCrashAndShowCollectionSize(){
        session.beginTransaction();
        int count = ((Long) session.createQuery("select count(*) from Car ").getSingleResult()).intValue();
        System.out.println("Select count(*) from Car result: "+count);
        List<Car> cars = session.createQuery("from Car ").getResultList();
        System.out.println("Result set count: "+cars.size());

        cars.forEach(car-> System.out.println(car.toString()));

        assertEquals(count, cars.size());
        session.getTransaction().commit();
    }

    @Test
    void shouldSuccessfullyAddNewCarType(){
        session.beginTransaction();
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
            if(session.getTransaction()!=null){
                session.getTransaction().rollback();
            }
            fail(e);
        }
        session.getTransaction().rollback();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}