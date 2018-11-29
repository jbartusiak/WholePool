package com.jba.utils;

import com.jba.dao2.user.enitity.User;
import com.jba.dao2.user.enitity.UserType;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Methods {

    @Value("${wholepool.rest.url.base.url}")
    private String wholepoolBaseUrl;

    @Autowired
    private Deserializer deserializer;

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

    public UserType getUserTypeByName(String name){
        RestTemplate template= new RestTemplate();
        String getUserTypeQuery = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam("users")
                .addPathParam("type")
                .build();

        UserType[] ut= deserializer.getResultArrayFor(template.getForObject(getUserTypeQuery, String.class), UserType[].class);
        List<UserType> userTypes = Arrays.asList(ut).stream().filter(userType -> userType.getTypeName().equals(name)).collect(Collectors.toList());
        return userTypes.get(0);
    }

    public UserType getUserTypeById(Integer id){
        RestTemplate template= new RestTemplate();
        String getUserTypeQuery = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam("users")
                .addPathParam("type")
                .build();

        UserType[] ut= deserializer.getResultArrayFor(template.getForObject(getUserTypeQuery, String.class), UserType[].class);
        List<UserType> userTypes = Arrays.asList(ut).stream().filter(userType -> userType.getTypeId()==id).collect(Collectors.toList());
        return userTypes.get(0);
    }
}
