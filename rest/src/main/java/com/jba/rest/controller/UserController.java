package com.jba.rest.controller;

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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "Get user(s)", notes = "Fetches user(s) from database. Consumes three optional parameters. " +
            "If id is given, fetches user of that id. If name is given, fetches user of that name. If email is given, " +
            "fetches user of that email.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getUser(
            @ApiParam(name = "id", value="An Integer referring to User entity", required = false,
                    type = "Integer")
            @RequestParam(value = "id", required = false) Integer id,
            @ApiParam(name = "name", value="A String referring to User entity", required = false,
                    type = "String")
            @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "name", value="A String referring to User entity", required = false,
                    type = "String")
            @RequestParam(value = "email", required = false) String email) {
        if (id != null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUser(id));
        else if (name != null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUserByUsername(name));
        else if (email != null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUserByEmail(email));
        else return new WPLResponse<>(HttpStatus.OK, userService.getAllUsers());
    }

    @ApiOperation(value = "Add a new user", notes = "Adds a new user to the database.", consumes = "application/json")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addUser(
            @ApiParam(name = "user", value="A JSON representing the User entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) User user
    ) {
        return new WPLResponse<>(HttpStatus.CREATED, userService.addNewUser(user));
    }

    @ApiOperation(value = "Update user", notes = "Updates user in the database.", consumes = "application/json")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(
            @ApiParam(name = "user", value="A JSON representing the User entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) User user
    ) {
        userService.updateUser(user);
    }

    @ApiOperation(value = "Verify password", notes = "Verifies password for given user")
    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse verifyUserPasswordHash(
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
            @RequestParam(value = "userId", required = true) Integer userId,
            @ApiParam(name = "hash", value="A String generated with login method", required = true,
                    type = "String")
            @RequestParam(value = "hash", required = true) String hash
    ) {
        return new WPLResponse<>(HttpStatus.OK, userService.verifyPasswordHash(User.of(userId), hash));
    }

    @ApiOperation(value = "Get users preferences", notes = "Fetches users preferences from the database")
    @GetMapping("/preferences")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getUsersPreferences(
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
            @RequestParam(name = "userId", required = true) Integer userId
    ) {
        return new WPLResponse<>(HttpStatus.OK, userService.getUsersPreferences(User.of(userId)));
    }

    @ApiOperation(value = "Add new users preference", notes = "Adds a new preference to a user.")
    @PostMapping("/preferences/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addUserPreference(
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
            @PathVariable(name = "userId", required = true) Integer userId,
            @ApiParam(name = "prefValue", value="A string value", required = true,
                    type = "String")
            @RequestParam(name = "value", required = true) String prefValue,
            @ApiParam(name = "preferenceId", value="An Integer referring to Preference entity", required = true,
                    type = "Integer")
            @RequestParam(name = "preferenceId") Integer preferenceId
    ) {
        UsersPreference result = userService.addPreference(User.of(userId), Preference.of(preferenceId), prefValue);

        return new WPLResponse<>(HttpStatus.OK, result);

    }

    @ApiOperation(value = "Delete users preference", notes = "Deletes users preference from the database.",
            consumes = "application/json")
    @DeleteMapping("/preferences")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse deleteUsersPreference(
            @ApiParam(name = "usersPreference", value="A JSON representing the UsersPreference entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) UsersPreference usersPreference
    ){
        return new WPLResponse<>(HttpStatus.OK, userService.deletePreference(usersPreference));
    }
}
