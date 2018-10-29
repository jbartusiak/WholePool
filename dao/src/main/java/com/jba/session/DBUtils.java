package com.jba.session;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;

@UtilityClass
public class DBUtils {

    public static Object saveOrUpdate(Session session, Object object){
        try{
            session.saveOrUpdate(object);
            session.getTransaction().commit();
            session.close();
            return object;
        }
        catch (Exception e){
            session.getTransaction().rollback();
            session.close();
            e.printStackTrace();
            return null;
        }
    }
}
