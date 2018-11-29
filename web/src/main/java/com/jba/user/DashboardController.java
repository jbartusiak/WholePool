package com.jba.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String getUserDashboard(HttpSession session){
        if(session.getAttribute("user")!=null) {



            return "dashboard";
        }
        else return "error/401";
    }
}
