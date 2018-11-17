package com.jba.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/settings")
    public String setting(){
        return "user-settings";
    }

    @GetMapping("/settings/data")
    public String getDataSettings(){
        return "user-settings-data";
    }
}
