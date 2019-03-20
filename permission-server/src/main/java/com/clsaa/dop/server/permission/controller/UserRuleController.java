package com.clsaa.dop.server.permission.controller;

import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.model.po.UserRule;
import com.clsaa.dop.server.permission.service.UserRuleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * 用户数据规则控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param roleId 角色ID
 *  * @param fieldName 权限作用域参数名
 *  * @param rule 数据规则
 *
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 *
 *since :2019.3.19
 */
@CrossOrigin
@RestController
@EnableAutoConfiguration
public class UserRuleController {
    @Autowired
    private UserRuleService userRuleService;

    //添加一个用户数据规则
    @ApiOperation(value = "添加一个用户数据规则", notes = "添加一个用户数据规则")
    @PostMapping("/v1/users/userrule")
    public void addRule(
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId,
            @ApiParam(name = "fieldName",value = "权限作用域参数名",required = true)
            @RequestParam(value = "fieldName", required = true)String fieldName,
            @ApiParam(name = "rule",value = "规则",required = true)
            @RequestParam(value = "rule", required = true)String rule,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long loginUser
    )
    {
        userRuleService.addRule(roleId,fieldName,rule,loginUser,loginUser);
    }

}
