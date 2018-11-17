package com.jba.login;

import com.jba.dao2.user.enitity.User;
import com.jba.session.SessionInfo;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping
public class LoginController {

    @Value("${wholepool.rest.url.base.url}")
    private String wholepoolBaseUrl;

    private final String usersBase = "users";

    @Autowired
    SessionInfo userInSession;

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @PostMapping(value = "/login")
    public String doLogin(User user){
        RestTemplate restTemplate = new RestTemplate();

        String userSearchURL = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam(usersBase)
                .addParam("email", user.getEmailAddress())
                .build();

        String result = restTemplate.getForObject(userSearchURL, String.class);

        User userFromJson = Deserializer.getSingleItemFor(result, User.class);

        System.out.println(userFromJson.toString());

        String verifyPasswordURL = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam(usersBase)
                .addPathParam("verify")
                .addParam("hash", user.getPasswordHash())
                .addParam("userId", userFromJson.getUserId())
                .build();

        String verifyPassword = restTemplate.getForObject(verifyPasswordURL, String.class);

        Boolean isPasswordCorrect = Deserializer.getSingleItemFor(verifyPassword, Boolean.class);

        if(isPasswordCorrect){
            userInSession.setUserInSession(userFromJson);
            return "index";
        }
        else{
            return "login";
        }
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/logout")
    public String logout(){
        userInSession.setUserInSession(null);
        return "index";
    }
}
