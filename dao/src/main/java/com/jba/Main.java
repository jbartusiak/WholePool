package com.jba;

import com.jba.dao.user.dao.UserDAO;
import com.jba.dao.user.enitity.User;
import com.jba.dao.user.enitity.UserType;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = WPLSessionFactory.getDBSession();

        try{
            System.out.println("Creating a new user type.");
            UserType userType = new UserType("Example user");

            System.out.println("Opening session");
            session.beginTransaction();

            System.out.println("Saving user type");
            session.save(userType);

            System.out.println("Commiting");
            session.getTransaction().commit();

            System.out.println("Done");

        }
        finally {
            WPLSessionFactory.closeAndFinalize();
        }

    }
}
