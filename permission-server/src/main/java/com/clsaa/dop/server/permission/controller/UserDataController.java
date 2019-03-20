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
    //使用方法： roleId
    @ApiOperation(value = "验证某个功能点操作的数据是否允许操作", notes = "验证某个功能点操作的数据是否允许操作")
    @GetMapping("/v1/users/data")
    public boolean check(
            @ApiParam(name = "permissionName",value = "功能点名称",required = true)
            @RequestParam(value = "permissionName", required = true) String permissionName,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "fieldName",value = "作用域参数名称",required = true)
            @RequestParam(value = "fieldName", required = true)String fieldName,
            @ApiParam(name = "fieldValue",value = "作用域参数值",required = true)
            @RequestParam(value = "fieldValue", required = true)Long fieldValue
    )
    {
      return   userDataService.check(permissionName,userId,fieldName,fieldValue);
    }

    //得到某个功能点操作允许操作的数据范围（返回ID列表形式）
    @ApiOperation(value = "得到某个功能点操作允许操作的数据范围（返回ID列表形式）", notes = "得到某个功能点操作允许操作的数据范围（返回ID列表形式）")
    @GetMapping("/v1/users/datalist")
    public List<Long> findAllIds(
            @ApiParam(name = "permissionName",value = "功能点名称",required = true)
            @RequestParam(value = "permissionName", required = true) String permissionName,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "fieldName",value = "作用域参数名称",required = true)
            @RequestParam(value = "fieldName", required = true)String fieldName
    )
    {
       return userDataService.findAllIds(permissionName,userId,fieldName);
    }




}
