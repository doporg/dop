package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wsy
 */
@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users/{username}")
    public String hh1(@PathVariable("username") String username){
        return userService.findUserAccessToken(username);
    }
}
