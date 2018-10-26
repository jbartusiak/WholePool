package com.jba.dao.blocked.entity;

import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockedUsersTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void shouldNotCrashAndReturnShowNumberOfRecords(){
        session.beginTransaction();
        int count = ((Long) session.createQuery("select count(*) from BlockedUsers ").getSingleResult()).intValue();
        System.out.println("Select count(*) from BlockedUsers result: "+count);
        List<BlockedUsers> blockedUsers = session.createQuery("from BlockedUsers ").getResultList();
        System.out.println("Result set count: "+blockedUsers.size());

        blockedUsers.forEach(blocked-> System.out.println(blocked.toString()));

        assertEquals(count, blockedUsers.size());
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}