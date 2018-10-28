package com.jba.dao.route.entity;

import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PopularRouteTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void addNewItemToPopularRoute(){
        session.beginTransaction();
        Route route = session.createQuery("from Route r",Route.class).getResultList().get(0);
        PopularRoute popularRoute = new PopularRoute(route,2.0,3.0,1.0,2);
        session.save(popularRoute);
        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}