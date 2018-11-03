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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Dao2Application.class })
@WebAppConfiguration
public class CarTypeTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void shouldNotCrashAndShowCollectionSize(){
        Session session = sessionFactory.getCurrentSession();
        int count = ((Long) session.createQuery("select count(*) from CarType ").getSingleResult()).intValue();
        System.out.println("Select count(*) from BlockedUsers result: "+count);
        List<CarType> carTypes = session.createQuery("from CarType ").getResultList();
        System.out.println("Result set count: "+carTypes.size());

        carTypes.forEach(car-> System.out.println(car.toString()));

        assertEquals(count, carTypes.size());
    }

    @Test
    @Transactional
    public void shouldSuccessfullyAddNewCarType(){
        CarType carType = new CarType("Test drive");

        Session session = sessionFactory.getCurrentSession();
        session.save(carType);

        CarType fromDB = (CarType) session.createQuery("from CarType c where c.carTypeName='Test drive'").getSingleResult();

        assertEquals(carType.getCarTypeId(),fromDB.getCarTypeId());

        System.out.println("Created: "+carType.toString());
        System.out.println("Generated: "+carType.toString());
    }
}