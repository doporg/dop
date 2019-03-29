package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.feign.UserFeign;
import com.clsaa.dop.server.code.model.dto.user.UserDto;
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

    @Autowired
    private UserFeign userFeign;




    @GetMapping("/users/publickey")
    public String hh1(){
        return userFeign.getAccountRSAPublicKey();
    }


    @PutMapping("/users")
    public void hh2(@RequestParam String username,@RequestParam String password){
        userService.updateUserPassword(username,password);
    }


    @ApiOperation(value = "新增一个用户",notes = "用用户名、密码和邮箱新建一个gitlab用户，并且创建access_token插入数据库")
    @PostMapping("/users")
    public void addUser(@ApiParam(value = "用户信息") @RequestBody UserDto userDto){
        userService.addUser(userDto.getName(),userDto.getPassword(),userDto.getEmail());
    }
}
