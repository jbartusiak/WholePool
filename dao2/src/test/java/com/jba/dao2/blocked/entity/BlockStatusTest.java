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

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Dao2Application.class })
@WebAppConfiguration
public class BlockStatusTest {

    @Autowired
    SessionFactory sessionFactory;

    @Test
    @Transactional
    public void runTest(){
        System.out.println("Running test for BlockStatus");
        Session session = sessionFactory.getCurrentSession();

        List<BlockStatus> result = session.createQuery("from BlockStatus ", BlockStatus.class).getResultList();

        int arraySize = result.size();

        assertNotEquals(0, arraySize);

        System.out.println("Result for select * from BlockStatus");
        result.forEach(blockStatus -> System.out.println(blockStatus.toString()));
    }
}