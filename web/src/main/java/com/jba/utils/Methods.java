package com.jba.utils;

import com.jba.dao2.user.enitity.User;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

public class Methods {

    public Boolean isUserAPassenger(User[] passengers, Integer userId){
        if(userId==null||passengers==null){
            return false;
        }
        for(User u: passengers){
            if(u.getUserId()==userId){
                return true;
            }
        }
        return false;
    }

    public Boolean isUserDriver(User offerer, Integer userId){
        return offerer.getUserId()==userId;
    }
}
