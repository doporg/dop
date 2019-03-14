package com.clsaa.dop.server.permission.controller;

import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.model.bo.PermissionBoV1;
import com.clsaa.dop.server.permission.model.po.Permission;
import com.clsaa.dop.server.permission.model.po.RolePermissionMapping;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.model.vo.RoleV1;
import com.clsaa.dop.server.permission.service.PermissionService;
import com.clsaa.dop.server.permission.service.RolePermissionMappingService;
import com.clsaa.dop.server.permission.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色功能点关联管理控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param id 关联关系ID
 *  * @param roleId 角色ID
 *  * @param permissionId 功能点ID
 *
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 *
 *since :2019.3.11
 */

@CrossOrigin
@RestController
@EnableAutoConfiguration
public class RolePermissionMappingController {
    @Autowired
    private RolePermissionMappingService rolePermissionMappingService;

    //添加一个关联关系
    @ApiOperation(value = "创建角色功能点关联关系", notes = "创建角色功能点关联关系")
    @PostMapping("/v1/roles/permissionmap")
    public void addPermissionToRole(
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId,
            @ApiParam(name = "permissionId",value = "功能点ID",required = true)
            @RequestParam(value = "permissionId", required = true)Long permissionId,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long cuser,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long muser)
    {
            rolePermissionMappingService.addPermissionToRole(roleId,permissionId,cuser,muser);
    }

    @ApiOperation(value = "根据角色ID查询功能点", notes = "根据角色ID查询功能点")
    @GetMapping("/v1/roles/permissions/{id}")
    //根据角色ID查询功能点
    public List<PermissionV1> findByroleId(@ApiParam(name = "roleId",value = "角色ID",required = true)
                                               @RequestParam(value = "roleId", required = true)Long roleId)
    {
        return rolePermissionMappingService.findByroleId(roleId)
                .stream().map(p -> BeanUtils.convertType(p, PermissionV1.class)).collect(Collectors.toList());
    }

    @ApiOperation(value = "根据功能点ID查询角色", notes = "根据功能点ID查询角色")
    @GetMapping("/v1/permissions/roles/{id}")
    //根据功能点ID查询角色
    public List<RoleV1> findBypermissionId(@ApiParam(name = "permissionId",value = "功能点ID",required = true)
                                           @RequestParam(value = "permissionId", required = true)Long permissionId)
    {
        return rolePermissionMappingService.findByPermissionId(permissionId)
                .stream().map(p -> BeanUtils.convertType(p, RoleV1.class)).collect(Collectors.toList());
    }

    @ApiOperation(value = "删除角色功能点关联关系", notes = "删除角色功能点关联关系")
    @DeleteMapping ("/v1/roles/permissions")
    //删除角色功能点关联关系
    public void delete(
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId,
            @ApiParam(name = "permissionId",value = "功能点ID",required = true)
            @RequestParam(value = "permissionId", required = true)Long permissionId)
    {
        rolePermissionMappingService.delete(roleId,permissionId);
    }


}
