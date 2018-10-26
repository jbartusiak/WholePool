package com.jba.dao.cars.entity;

import com.jba.dao.user.enitity.User;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsersCarsTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void shouldNotCrashAndShowCollectionSize(){
        session.beginTransaction();
        int count = ((Long) session.createQuery("select count(*) from UsersCars ").getSingleResult()).intValue();
        System.out.println("Select count(*) from UsersCars result: "+count);
        List<UsersCars> usersCars = session.createQuery("from UsersCars ", UsersCars.class).getResultList();
        System.out.println("Result set count: "+usersCars.size());

        usersCars.forEach(car-> System.out.println(car.toString()));

        assertEquals(count, usersCars.size());
        session.getTransaction().commit();
    }

    @Test
    void shouldInsertAndGetBackANewCarForAUser(){
        session.beginTransaction();
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
            fail(e);
        }
        session.getTransaction().rollback();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}