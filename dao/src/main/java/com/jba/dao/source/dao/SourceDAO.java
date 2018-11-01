package com.jba.dao.source.dao;

import com.jba.dao.source.entity.Source;
import com.jba.session.DBUtils;
import com.jba.session.WPLSessionFactory;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

@UtilityClass
public class SourceDAO {

    private final static Logger logger = Logger.getLogger(SourceDAO.class);

    public static Source getSourceByName(String name){
        Session session = WPLSessionFactory.getDBSession();
        try(session){
            session.beginTransaction();

            List<Source> sourceList = session
                    .createQuery("from Source s where s.sourceName=:name", Source.class)
                    .setParameter("name", name).getResultList();

            session.getTransaction().commit();

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
            throw e;
        }
    }

    public static void addSource(String sourceName, String searchBaseURL, String resultsParseRules){
        Source source = new Source(sourceName,searchBaseURL,resultsParseRules);
        DBUtils.saveOrUpdate(source);
    }

    public static void addSource(Source source){
        DBUtils.saveOrUpdate(source);
    }

    public static Source editSource(Source source){
        return (Source) DBUtils.saveOrUpdate(source);
    }

    public static Source deleteSource(Source source){
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            session.delete(source);

            session.getTransaction().commit();

            return source;
        }
        catch (Exception e){
            logger.error("Error trying to delete item "+source,e);
            throw e;
        }
    }
}
