package com.jba.home;

import com.jba.session.SessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    SessionInfo sessionInfo;

    @RequestMapping
    public String welcome(Model model){
        return "index";
    }
}
