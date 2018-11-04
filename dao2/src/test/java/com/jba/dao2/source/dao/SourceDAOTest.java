package com.jba.dao2.source.dao;

import com.jba.dao2.Dao2Application;
import com.jba.dao2.source.entity.Source;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Dao2Application.class })
@WebAppConfiguration
public class SourceDAOTest {

    private final static Logger logger = Logger.getLogger(SourceDAOTest.class);

    @Autowired
    private SourceDAO sourceDAO;
    
    @Test
    public void getSourceByName() {
        Source source = sourceDAO.getSourceByName("localhost");

        assertNotNull(source);
        assertEquals(1,source.getSourceId());
    }

    @Test
    public void addSource() {
        Source source = new Source("źródło", "awdawd", "awdawd");
        sourceDAO.addSource(source);

        assertNotEquals(0, source.getSourceId());

        sourceDAO.deleteSource(source);
    }

    @Test
    public void editSource() {
    }

    @Test
    public void deleteSource() {
        Source source = new Source("test2","test","test");

        sourceDAO.addSource(source);

        sourceDAO.deleteSource(source);

        Source fromDB = sourceDAO.getSourceByName("test2");
        assertNull("There should be no source of name test2",fromDB);
    }
}