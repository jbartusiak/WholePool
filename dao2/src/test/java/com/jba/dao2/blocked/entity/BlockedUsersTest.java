package com.jba.dao2.blocked.entity;

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
public class BlockedUsersTest {

    @Autowired
    SessionFactory sessionFactory;

    @Test
    @Transactional
    public void shouldNotCrashAndReturnShowNumberOfRecords(){
        Session session = sessionFactory.getCurrentSession();

        int count = session.createQuery("select count(*) from BlockedUsers ", Long.class).getSingleResult().intValue();
        System.out.println("Select count(*) from BlockedUsers result: "+count);
        List<BlockedUsers> blockedUsers = session.createQuery("from BlockedUsers ", BlockedUsers.class).getResultList();
        System.out.println("Result set count: "+blockedUsers.size());

        blockedUsers.forEach(blocked-> System.out.println(blocked.toString()));

        assertEquals(count, blockedUsers.size());
    }
}