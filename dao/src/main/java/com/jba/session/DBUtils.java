package com.jba.session;

import com.jba.WPLSessionFactory;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;

@UtilityClass
public class DBUtils {

    public static Object saveOrUpdate(Object object){
        Session session = WPLSessionFactory.getDBSession();

        try{
            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();
        }
        catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
        finally {
            session.close();
        }
        if (session.isOpen() != false) throw new AssertionError();
        return object;
    }
}
