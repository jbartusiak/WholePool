package com.jba.daodeprecated.route.entity;

import com.jba.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RouteTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void addNewEntryToRoute(){
        Route route = new Route("adsfafafaf", "fafafafasas");
        session.beginTransaction();
        session.save(route);
        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}