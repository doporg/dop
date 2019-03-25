package com.clsaa.dop.server.user.controller;

import com.clsaa.dop.server.user.model.po.UserCredential;
import com.clsaa.dop.server.user.service.UserCredentialService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户凭证接口
 *
 * @author joyren
 * @since 2019-03-25
 */
@RestController
@CrossOrigin
public class UserCredentialController {
    @Autowired
    private UserCredentialService userCredentialService;

    @ApiOperation(value = "添加用户凭证", notes = "添加一个用户凭证，比如GitlabToken，JenkinsToken，Harbor用户名密码等")
    @PostMapping("/v1/users/{userId}/credential")
    public void addUserCredential(@ApiParam(value = "用户id") @PathVariable("userId") Long userId,
                                  @ApiParam(value = "凭证标识，如邮箱、用户名等") @RequestParam("identifier") String identifier,
                                  @ApiParam(value = "凭证，如token，密码等") @RequestParam("credential") String credential,
                                  @ApiParam(value = "凭证类型，用于标识凭证的用途") @RequestParam("type") UserCredential.Type type) {
        this.userCredentialService.addUserCredential(userId, identifier, credential, type);
    }

    @ApiOperation(value = "删除用户凭证", notes = "根据用户id和凭证类型删除一个用户凭证")
    @PutMapping("/v1/users/{userId}/credential")
    @DeleteMapping
    void deleteUserCredential(@ApiParam(value = "用户id") @PathVariable("userId") Long userId,
                              @ApiParam(value = "凭证类型，用于标识凭证的用途") @RequestParam("type") UserCredential.Type type) {
        this.userCredentialService.deleteUserCredentialByUserIdAndType(userId, type);
    }

    @ApiOperation(value = "更新用户凭证", notes = "根据用户id和凭证类型更新一个用户凭证")
    @PutMapping("/v1/users/{userId}/credential")
    public void updateUserCredential(@ApiParam(value = "用户id") @PathVariable("userId") Long userId,
                                     @ApiParam(value = "凭证，如token，密码等") @RequestParam("credential") String credential,
                                     @ApiParam(value = "凭证类型，用于标识凭证的用途") @RequestParam("type") UserCredential.Type type) {
        this.userCredentialService.updateUserCredentialByUserIdAndType(userId, credential, type);
    }
}
