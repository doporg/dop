package com.clsaa.dop.server.permission.controller;


import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.service.UserDataService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户数据控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param ruleId 规则ID
 *  * @param userId 用户ID
 *  * @param fieldValue 作用域参数值
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
public class UserDataController {
    @Autowired
    private UserDataService userDataService;

    //添加一个用户数据
    @ApiOperation(value = "添加一个用户数据", notes = "添加一个用户数据")
    @PostMapping("/v1/users/userdata")
    public void addData(
            @ApiParam(name = "ruleId",value = "规则ID",required = true)
            @RequestParam(value = "ruleId", required = true)Long ruleId,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "fieldValue",value = "权限作用域参数值",required = true)
            @RequestParam(value = "fieldValue", required = true)Long fieldValue,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long loginUser
    )
    {
        userDataService.addData(ruleId,userId,fieldValue,loginUser,loginUser);
    }

    //判断该用户当前功能点是否可在该条数据执行
    @ApiOperation(value = "判断该用户当前功能点是否可在该条数据执行", notes = "判断该用户当前功能点是否可在该条数据执行")
    @GetMapping("/v1/users/data")
    public boolean check(
            @ApiParam(name = "permissionId",value = "功能点ID",required = true)
            @RequestParam(value = "permissionId", required = true) Long permissionId,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "fieldName",value = "作用域参数名称",required = true)
            @RequestParam(value = "fieldName", required = true)String fieldName,
            @ApiParam(name = "fieldValue",value = "作用域参数值",required = true)
            @RequestParam(value = "fieldValue", required = true)Long fieldValue
    )
    {
      return   userDataService.check(permissionId,userId,fieldName,fieldValue);
    }

    //得到该用户当前功能点可执行的所有数据ID
    @ApiOperation(value = "得到该用户当前功能点可执行的所有数据", notes = "得到该用户当前功能点可执行的所有数据")
    @GetMapping("/v1/users/datalist")
    public List<Long> findAllIds(
            @ApiParam(name = "permissionId",value = "功能点ID",required = true)
            @RequestParam(value = "permissionId", required = true) Long permissionId,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "fieldName",value = "作用域参数名称",required = true)
            @RequestParam(value = "fieldName", required = true)String fieldName
    )
    {
       return userDataService.findAllIds(permissionId,userId,fieldName);
    }




}
