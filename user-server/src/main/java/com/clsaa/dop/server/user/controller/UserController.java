package com.clsaa.dop.server.user.controller;

import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.model.dto.ResetDtoV1;
import com.clsaa.dop.server.user.model.vo.UserV1;
import com.clsaa.dop.server.user.util.BeanUtils;
import com.clsaa.dop.server.user.service.UserService;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户API接口实现类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "添加用户", notes = "用户必须先进行注册，然后通过接收到的激活码添加一个用户")
    @PostMapping("/v1/users")
    public void addUserV1(@ApiParam(value = "激活码") @RequestParam(value = "code") String code) {
        this.userService.addUser(code);
    }

    @GetMapping("/test")
    public boolean test() {
        BizAssert.justDenied(BizCodes.ERROR_DELETE);
        return true;
    }

    @ApiOperation(value = "更新用户密码", notes = "用户必须先获取修改密码所需验证码，通过验证码更新密码")
    @PutMapping("/v1/users/password")
    public void updateUserPassword(@RequestBody ResetDtoV1 resetDtoV1) {
        this.userService.updateUserPassword(resetDtoV1.getEmail(), resetDtoV1.getPassword(), resetDtoV1.getCode());
    }


    @ApiOperation(value = "查询用户信息", notes = "根据id查询用户信息，若用户不存在返回null")
    @GetMapping("/v1/users/{id}")
    public UserV1 findUserByIdV1(@ApiParam(value = "用户id") @PathVariable("id") Long id) {
        return BeanUtils.convertType(this.userService.findUserById(id), UserV1.class);
    }
}
