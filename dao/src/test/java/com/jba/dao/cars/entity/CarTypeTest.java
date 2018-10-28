package com.jba.dao.cars.entity;

import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarTypeTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void shouldNotCrashAndShowCollectionSize(){
        session.beginTransaction();
        int count = ((Long) session.createQuery("select count(*) from CarType ").getSingleResult()).intValue();
        System.out.println("Select count(*) from BlockedUsers result: "+count);
        List<CarType> carTypes = session.createQuery("from CarType ").getResultList();
        System.out.println("Result set count: "+carTypes.size());

        carTypes.forEach(car-> System.out.println(car.toString()));

        assertEquals(count, carTypes.size());
        session.getTransaction().commit();
    }

    @Test
    void shouldSuccessfullyAddNewCarType(){
        CarType carType = new CarType("Test drive");

        session.beginTransaction();
        session.save(carType);

        CarType fromDB = (CarType) session.createQuery("from CarType c where c.carTypeName='Test drive'").getSingleResult();

        assertEquals(carType.getCarTypeId(),fromDB.getCarTypeId());

        System.out.println("Created: "+carType.toString());
        System.out.println("Generated: "+carType.toString());

        session.getTransaction().rollback();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}