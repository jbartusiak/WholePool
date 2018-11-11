package com.jba.dao2.source.dao;

import com.jba.dao2.source.entity.Source;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class SourceDAOMySQLRepository implements SourceDAO{

    private final Logger logger = Logger.getLogger(SourceDAOMySQLRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Source> getAllSources() {
        Session session = sessionFactory.getCurrentSession();

        return session.
                createQuery("from Source s", Source.class).
                getResultList();
    }

    public Source getSourceByName(String name){
        Session session = sessionFactory.getCurrentSession();
        try{
            List<Source> sourceList = session
                    .createQuery("from Source s where s.sourceName=:name", Source.class)
                    .setParameter("name", name).getResultList();

            if(sourceList.size()==0){
                logger.info("No sources found of name "+name);
                return null;
            }
            if(sourceList.size()>1){
                throw new IllegalArgumentException("There are multiple sources with name "+name);
            }
            else{
                return sourceList.get(0);
            }
        }
        catch (NoResultException e){
            logger.info("No sources found of name "+name);
            return null;
        }
        catch (Exception e){
            logger.error("Error retrieving source with name+ "+name, e);
            throw e;
        }
    }

    public void addSource(String sourceName, String searchBaseURL, String resultsParseRules){
        Source source = new Source(sourceName,searchBaseURL,resultsParseRules);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(source);
    }

    public void addSource(Source source){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(source);
    }

    public Source editSource(Source source){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(source);
        return source;
    }

    public Source deleteSource(Source source){
        Session session = sessionFactory.getCurrentSession();

        try{
            session.delete(source);
            return source;
        }
        catch (Exception e){
            logger.error("Error trying to delete item "+source,e);
            throw e;
        }
    }
}
