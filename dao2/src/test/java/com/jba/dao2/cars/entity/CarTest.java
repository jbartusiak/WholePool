package com.jba.dao2.cars.entity;

import org.hibernate.Session;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.fail;
import static org.junit.Assert.assertEquals;

class CarTest {

    private Session session;

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
            fail(e.getMessage());
        }
        session.getTransaction().rollback();
    }
}