package com.jba.rest;

import com.jba.dao2.user.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @RequestMapping("/user")
    public String getUser(){
        return "user";
    }
}
