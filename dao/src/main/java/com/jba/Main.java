package com.jba;

import com.jba.dao.user.dao.UserDAO;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = WPLSessionFactory.getDBSession();
    }
}
