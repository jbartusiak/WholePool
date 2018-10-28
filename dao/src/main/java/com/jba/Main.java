package com.jba;

import com.jba.dao.user.dao.UserDAO;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = WPLSessionFactory.getDBSession();

        try{
            System.out.println("Creating a new user type.");

            UserDAO.getAllUsers().forEach(
                    userType -> System.out.println(userType.toString())
            );

        }
        finally {
            WPLSessionFactory.closeAndFinalize();
        }

    }
}
