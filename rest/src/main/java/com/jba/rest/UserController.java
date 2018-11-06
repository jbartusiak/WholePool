package com.jba.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.preferences.entity.UsersPreference;
import com.jba.dao2.user.dao.UserDAO;
import com.jba.dao2.user.enitity.User;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getUser(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email) {
        if (id != null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUser(id));
        else if (name != null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUserByUsername(name));
        else if (email != null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUserByEmail(email));
        else return new WPLResponse<>(HttpStatus.OK, userService.getAllUsers());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addUser(
            @RequestBody(required = true) User user
    ) {
        return new WPLResponse<>(HttpStatus.CREATED, userService.addNewUser(user));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(
            @RequestBody(required = true) User user
    ) {
        userService.updateUser(user);
    }

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse verifyUserPasswordHash(
            @RequestParam(value = "userId", required = true) Integer userId,
            @RequestParam(value = "hash", required = true) String hash
    ) {
        return new WPLResponse<>(HttpStatus.OK, userService.verifyPasswordHash(User.of(userId), hash));
    }

    @GetMapping("/preferences")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getUsersPreferences(
            @RequestParam(name = "userId", required = true) Integer userId
    ) {
        return new WPLResponse<>(HttpStatus.OK, userService.getUsersPreferences(User.of(userId)));
    }

    @PostMapping("/preferences/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addUserPreference(
            @PathVariable(name = "userId", required = true) Integer userId,
            @RequestParam(name = "value", required = true) String prefValue,
            @RequestParam(name = "preferenceId") Integer preferenceId
    ) {
        UsersPreference result = userService.addPreference(User.of(userId), Preference.of(preferenceId), prefValue);

        return new WPLResponse<>(HttpStatus.OK, result);

    }
}
