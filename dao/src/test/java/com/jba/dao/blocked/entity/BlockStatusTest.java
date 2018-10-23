package com.jba.dao.blocked.entity;

import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockStatusTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void runTest(){
        System.out.println("Running test for BlockStatus");

        session.beginTransaction();

        List<BlockStatus> result = session.createQuery("from BlockStatus ").getResultList();

        int arraySize = result.size();

        assertNotEquals(arraySize, 0);

        System.out.println("Result for select * from BlockStatus");
        result.forEach(blockStatus -> System.out.println(blockStatus.toString()));
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}