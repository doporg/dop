package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.dto.UserDto;
import com.clsaa.dop.server.code.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @ApiOperation(value = "新增一个用户",notes = "用用户名、密码和邮箱新建一个gitlab用户，并且创建access_token插入数据库")
    @PostMapping("/users")
    public void addUser(@ApiParam(value = "用户信息") @RequestBody UserDto userDto){
        userService.addUser(userDto.getUsername(),userDto.getPassword(),userDto.getEmail());
    }
}
