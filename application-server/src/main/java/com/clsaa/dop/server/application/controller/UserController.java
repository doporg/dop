package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.model.vo.UserV1;
import com.clsaa.dop.server.application.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * <p>
 * 用户API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-5
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "创建项目", notes = "创建项目")
    @GetMapping(value = "/userInfo")
    public UserV1 findUserByUserId(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") Long userId) {
        return this.userService.findUserById(userId);
    }

}
