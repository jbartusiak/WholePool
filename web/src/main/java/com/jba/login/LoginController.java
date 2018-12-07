package com.jba.login;

import com.jba.dao2.user.enitity.User;
import com.jba.dao2.user.enitity.UserType;
import com.jba.utils.Deserializer;
import com.jba.utils.Mailer;
import com.jba.utils.Methods;
import com.jba.utils.RestRequestBuilder;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class LoginController {

    @Value("${wholepool.rest.url.base.url}")
    private String wholepoolBaseUrl;

    private final String usersBase = "users";

    @Autowired
    Logger logger;

    @Autowired
    Deserializer deserializer;

    @Autowired
    private Methods methods;

    @Autowired
    Mailer mailer;

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @PostMapping(value = "/login")
    public String doLogin(User user, HttpSession session, RedirectAttributes attributes){
        RestTemplate restTemplate = new RestTemplate();

        String userSearchURL = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam(usersBase)
                .addParam("email", user.getEmailAddress())
                .build();

        String result = restTemplate.getForObject(userSearchURL, String.class);

        String hash="";



        if(result.contains("404")&&result.contains("Not found")) {
            logger.error("User " + user.getEmailAddress() + " not found!");
            attributes.addAttribute("message", "login-error");
            return "redirect:/login";
        }

        User userFromJson = deserializer.getSingleItemFor(result, User.class);

        logger.info("User found: "+userFromJson.getEmailAddress());

        try {
            hash=generatePasswordHash(user.getPasswordHash());
        }
        catch (NoSuchAlgorithmException e){
            logger.error("Error occured while creating SHA-256 hash short for password.", e);
            attributes.addAttribute("message", "login-error");
            return "redirect:/login";
        }

        String verifyPasswordURL = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam(usersBase)
                .addPathParam("verify")
                .addParam("hash", hash)
                .addParam("userId", userFromJson.getUserId())
                .build();

        String verifyPassword = restTemplate.getForObject(verifyPasswordURL, String.class);

        Boolean isPasswordCorrect = deserializer.getSingleItemFor(verifyPassword, Boolean.class);

        if(isPasswordCorrect){
            session.setAttribute("user", userFromJson);
            return "redirect:/user/dashboard";
        }
        else{
            attributes.addAttribute("message", "password-incorrect");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register(){


        return "register";
    }

    @PostMapping("/register")
    public String doRegister(User user, HttpSession session, RedirectAttributes attributes){
        String postNewUserQuery = RestRequestBuilder
                .builder(wholepoolBaseUrl)
                .addPathParam("users")
                .build();

        RestTemplate template = new RestTemplate();

        String password = user.getPasswordHash();

        String hash="";

        try {
            hash=generatePasswordHash(password);
        }
        catch (NoSuchAlgorithmException e){
            logger.error("Error occured while creating SHA-256 hash short for password.", e);
            attributes.addAttribute("message", "registration-error");
            return "redirect:/register";
        }

        UserType passenger = methods.getUserTypeByName("Pasażer");

        user.setUserType(passenger);

        user.setFirstName(StringUtils.capitalize(user.getFirstName()));
        user.setLastName(StringUtils.capitalize(user.getLastName()));

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

        mailer.sendAccountCreatedMessage(user.getEmailAddress(), user.getFirstName());

        attributes.addAttribute("message", "registration-successfull");

        return "redirect:/user/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "index";
    }

    public String generatePasswordHash(String password) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] table = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(table));
    }
}
