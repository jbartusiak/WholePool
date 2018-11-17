package com.jba.login;

import com.jba.dao2.user.enitity.User;
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

    @Autowired
    SessionInfo userInSession;

    @GetMapping(value = "/login")
    public String login(Model model){
        RestTemplate restTemplate = new RestTemplate();
        return "login";
    }

    @PostMapping(value = "/login")
    public String doLogin(User user){
        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(wholepoolBaseUrl+usersBase+"?email="+user.getEmailAddress(), String.class);

        User userFromJson = Deserializer.getSingleItemFor(result, User.class);

        System.out.println(userFromJson.toString());

        String verifyPasswordURL =
                wholepoolBaseUrl+usersBase+"/verify?"+
                "hash="+user.getPasswordHash()+"&"+
                "userId="+userFromJson.getUserId();

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
