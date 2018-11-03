package com.jba.daodeprecated.source.dao;

import com.jba.daodeprecated.source.entity.Source;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceDAOTest {

    private final static Logger logger = Logger.getLogger(SourceDAOTest.class);

    @Test
    void getSourceByName() {
        Source source = SourceDAO.getSourceByName("localhost");

        assertNotNull(source);
        assertEquals(1,source.getSourceId());
    }

    @Test
    void addSource() {
        Source source = new Source("test", "awdawd", "awdawd");
        SourceDAO.addSource(source);

        assertNotEquals(0, source.getSourceId());

        SourceDAO.deleteSource(source);
    }

    @Test
    void editSource() {
    }

    @Test
    void deleteSource() {
        Source source = new Source("test2","test","test");

        SourceDAO.addSource(source);

        SourceDAO.deleteSource(source);

        try {
            Source fromDB = SourceDAO.getSourceByName("test2");
            fail("There should be no source of name test2");
        }
        catch (IllegalArgumentException e){
            logger.info("Exception occured - this is expected!", e);
        }
    }
}