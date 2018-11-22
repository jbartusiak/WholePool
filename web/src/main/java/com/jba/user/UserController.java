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

    @GetMapping("/settings/preferences")
    public String getPreferencesSettings(){return "user-settings-preferences";}

    @GetMapping("/settings/data")
    public String getDataSettings(){
        return "user-settings-data";
    }

    @GetMapping("/settings/accountType")
    public String getAccountType(){return "user-settings-accountType";}

    @GetMapping("/settings/changePassword")
    public String getChangePassword(){
        return "user-settings-changePassword";
    }

    @GetMapping("/settings/myCars")
    public String getCars(){return "user-settings-myCars";}

    @GetMapping("/settings/addCar")
    public String getAddCar(){
        return "user-settings-addCar";
    }

    @GetMapping("/rides")
    public String getUsersRides(){
        return "user-rides";
    }

    @GetMapping("/searchHistory")
    public String getUserSearchHistory(){
        return "user-search-history";
    }
}
