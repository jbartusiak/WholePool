package com.jba.dao.search.entity;

import com.jba.dao.route.entity.Route;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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