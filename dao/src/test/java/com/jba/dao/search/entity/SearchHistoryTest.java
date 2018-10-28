package com.jba.dao.search.entity;

import com.jba.dao.user.enitity.User;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchHistoryTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void createNewSearchUserAssociation(){
        session.beginTransaction();
        User user = session.createQuery("from User ",User.class).getResultList().get(0);
        Search search = session.createQuery("from Search", Search.class).getResultList().get(0);

        SearchHistory history = new SearchHistory(user,search);
        session.save(history);

        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}