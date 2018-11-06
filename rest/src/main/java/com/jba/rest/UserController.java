package com.jba.rest;

import com.jba.dao2.user.dao.UserDAO;
import com.jba.dao2.user.enitity.User;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            @RequestParam(value = "email", required = false) String email){
        if(id!=null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUser(id));
        else if (name!=null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUserByUsername(name));
        else if (email!=null)
            return new WPLResponse<>(HttpStatus.OK, userService.getUserByEmail(email));
        else return new WPLResponse<>(HttpStatus.OK, userService.getAllUsers());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addUser(
            @RequestBody(required = true) User user
    ){
        return new WPLResponse<>(HttpStatus.CREATED, userService.addNewUser(user));
    }


}
