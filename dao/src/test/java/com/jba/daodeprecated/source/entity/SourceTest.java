package com.jba.daodeprecated.source.entity;

import com.jba.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SourceTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = WPLSessionFactory.getDBSession();
    }

    @Test
    void shouldReturnCollectionSize(){
        session.beginTransaction();

        int count = ((Long) session.createQuery("select count(*) from Source ").getSingleResult()).intValue();
        System.out.println("Select count(*) from Source result: "+count);
        List<Source> sources = session.createQuery("from Source ", Source.class).getResultList();
        System.out.println("Result set count: "+sources.size());

        sources.forEach(source-> System.out.println(source.toString()));

        assertEquals(count, sources.size());
        assertNotEquals(count,0,"Collection should always have at least one localhost");
        session.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        WPLSessionFactory.closeAndFinalize();
    }
}