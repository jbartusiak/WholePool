package com.jba.dao2.source.entity;

import com.jba.dao2.DAOConfig;
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
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class SourceTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void shouldReturnCollectionSize(){
        Session session = sessionFactory.getCurrentSession();

        int count = ((Long) session.createQuery("select count(*) from Source ").getSingleResult()).intValue();
        System.out.println("Select count(*) from Source result: "+count);
        List<Source> sources = session.createQuery("from Source ", Source.class).getResultList();
        System.out.println("Result set count: "+sources.size());

        sources.forEach(source-> System.out.println(source.toString()));

        assertEquals(count, sources.size());
        assertNotEquals("Collection should always have at least one localhost",0,count);
    }
}