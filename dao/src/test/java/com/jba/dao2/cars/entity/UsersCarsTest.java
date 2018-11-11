package com.jba.dao2.cars.entity;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.user.enitity.User;
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
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class UsersCarsTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void shouldNotCrashAndShowCollectionSize(){
        Session session = sessionFactory.getCurrentSession();
        int count = ((Long) session.createQuery("select count(*) from UsersCars ").getSingleResult()).intValue();
        System.out.println("Select count(*) from UsersCars result: "+count);
        List<UsersCars> usersCars = session.createQuery("from UsersCars ", UsersCars.class).getResultList();
        System.out.println("Result set count: "+usersCars.size());

        usersCars.forEach(car-> System.out.println(car.toString()));

        assertEquals(count, usersCars.size());
    }

    @Test
    @Transactional
    public void shouldInsertAndGetBackANewCarForAUser(){
        Session session = sessionFactory.getCurrentSession();
        User theUser = session.createQuery("from User u where u.userName='ambiguous1991'", User.class).getSingleResult();
        CarType carType = new CarType("a super racer");
        Car car = new Car(carType, "satanicus1991 car", "ABCABC123", 2018);

        try {
            session.save(carType);
            session.save(car);

            car = session.createQuery("from Car c where c.carName='satanicus1991 car'", Car.class).getSingleResult();

            UsersCars usersCar = new UsersCars(theUser,car);

            session.save(usersCar);

            List<UsersCars> fromDB = session.createQuery("from UsersCars ", UsersCars.class).getResultList();

            fromDB.forEach(uc -> System.out.println(uc.toString()));
        }
        catch (Exception e){
            if(session.getTransaction()!=null){
                session.getTransaction().rollback();
            }
            fail(e.getMessage());
        }
    }
}