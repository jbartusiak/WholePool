package com.jba.login;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jba.dao2.user.enitity.User;
import com.jba.dao2.entity.WPLResponse;
import com.jba.session.SessionInfo;
import com.jba.utils.Deserializer;
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


    private final String usersBase = "/users";

    @GetMapping(value = "/login")
    public String login(Model model){
        RestTemplate restTemplate = new RestTemplate();
        return "login";
    }

    @PostMapping(value = "/login")
    public String doLogin(User user, Model model){
        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(wholepoolBaseUrl+usersBase+"?email="+user.getEmailAddress(), String.class);

        User userFromJson = Deserializer.getSingleItemFor(result, User.class);

        System.out.println(userFromJson.toString());
        return "index";
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "logout";
    }
}
