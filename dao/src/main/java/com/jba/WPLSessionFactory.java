package com.jba;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@SpringBootApplication
@Configuration
@PropertySource({"classpath:dao-config.properties"})
public class WPLSessionFactory {

    private final static Logger logger = Logger.getLogger(WPLSessionFactory.class);

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(WPLSessionFactory.class, args);
    }

    private int getIntProperty(String propertyName){
        String value = env.getProperty(propertyName);

        return Integer.parseInt(value);
    }

    public static Session getDBSession(){
        /*Session session = getInstance().openSession();
        sessnionRegister.add(session);
        return session;*/
        return null;
    }

    public static void closeAndFinalize(){

    }

    public static void closeSession(Session session){

    }
}
