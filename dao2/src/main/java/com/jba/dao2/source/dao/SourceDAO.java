package com.jba.dao2.source.dao;

import com.jba.dao2.source.entity.Source;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UtilityClass
public class SourceDAO {

    private final static Logger logger = Logger.getLogger(SourceDAO.class);

    @Autowired
    private SessionFactory sessionFactory;

    public static Source getSourceByName(String name){
        Session session = sessionFactory.getCurrentSession();
        try{
            session.beginTransaction();

            List<Source> sourceList = session
                    .createQuery("from Source s where s.sourceName=:name", Source.class)
                    .setParameter("name", name).getResultList();

            session.getTransaction().commit();

            session.close();

            if(sourceList.size()==0){
                throw new IllegalArgumentException("No sources found of name "+name);
            }
            else if(sourceList.size()>1){
                throw new IllegalArgumentException("There are multiple sources with name "+name);
            }
            else{
                return sourceList.get(0);
            }
        }
        catch (Exception e){
            logger.error("Error retrieving source with name+ "+name, e);
            if(session.isOpen())
                session.close();
            throw e;
        }
    }

    public static void addSource(String sourceName, String searchBaseURL, String resultsParseRules){
        Source source = new Source(sourceName,searchBaseURL,resultsParseRules);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(source);
    }

    public static void addSource(Source source){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(source);
    }

    public static Source editSource(Source source){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(source);
        return source;
    }

    public static Source deleteSource(Source source){
        Session session = sessionFactory.getCurrentSession();

        try{
            session.beginTransaction();

            session.delete(source);

            session.getTransaction().commit();

            session.close();

            return source;
        }
        catch (Exception e){
            logger.error("Error trying to delete item "+source,e);
            if(session.isOpen())
                session.close();
            throw e;
        }
    }
}
