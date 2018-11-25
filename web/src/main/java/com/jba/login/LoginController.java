package com.jba.login;

import com.jba.dao2.user.enitity.User;
import com.jba.dao2.user.enitity.UserType;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class LoginController {

    @Value("${wholepool.rest.url.base.url}")
    private String wholepoolBaseUrl;

    private final String usersBase = "users";

    @Autowired
    Deserializer deserializer;

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @PostMapping(value = "/login")
    public String doLogin(User user, HttpSession session){
        RestTemplate restTemplate = new RestTemplate();

        String userSearchURL = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam(usersBase)
                .addParam("email", user.getEmailAddress())
                .build();

        String result = restTemplate.getForObject(userSearchURL, String.class);

        User userFromJson = deserializer.getSingleItemFor(result, User.class);

        System.out.println(userFromJson.toString());

        String verifyPasswordURL = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam(usersBase)
                .addPathParam("verify")
                .addParam("hash", user.getPasswordHash())
                .addParam("userId", userFromJson.getUserId())
                .build();

        String verifyPassword = restTemplate.getForObject(verifyPasswordURL, String.class);

        Boolean isPasswordCorrect = deserializer.getSingleItemFor(verifyPassword, Boolean.class);

        if(isPasswordCorrect){
            session.setAttribute("user", userFromJson);
            return "index";
        }
        else{
            return "login";
        }
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(User user, HttpSession session, RedirectAttributes redirectAttributes){
        String postNewUserQuery = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam("users")
                .build();

        RestTemplate template = new RestTemplate();

        String hash = user.getPasswordHash();

        user.setUserType(UserType.of(2));

        user = deserializer.getSingleItemFor(template.postForObject(postNewUserQuery, user, String.class), User.class);

        user.setPasswordHash(hash);

        String changePasswordRequest = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam("users")
                .addPathParam("password")
                .addParam("userId", user.getUserId())
                .addParam("hash", hash)
                .build();

        template.put(changePasswordRequest, user);

        session.setAttribute("user", user);

        redirectAttributes.addAttribute("message", "Twoje konto zostało utworzone!");

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "index";
    }
}
