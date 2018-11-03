package com.jba.daodeprecated.search.entity;

import com.jba.daodeprecated.route.entity.Route;
import com.jba.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void createNewSearch(){
        session.beginTransaction();
        Route route = new Route("routeFrom","routeTo");
        session.save(route);
        Search search = new Search(route, "searchcriteria", "orderCriteria");
        session.save(search);
        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}