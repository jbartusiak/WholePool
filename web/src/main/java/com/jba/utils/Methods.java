package com.jba.utils;

import com.jba.dao2.user.enitity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Methods {

    public static Boolean isUserAPassenger(User[] passengers, Integer userId){
        for(User u: passengers){
            if(u.getUserId()==userId){
                return true;
            }
        }
        return false;
    }

    public static Boolean isUserDriver(Integer rideId, Integer userId){
        return false;
    }
}
