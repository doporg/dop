package com.clsaa.dop.server.permission.controller;

import com.clsaa.dop.server.permission.model.po.Permission;

import com.clsaa.dop.server.permission.service.PermissionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 功能点管理控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param id            功能点ID
 *  * @param parentId     父功能点ID
 *  * @param name          功能点名称
 *  * @param isPrivate         是否私有
 *  * @param description     功能点描述
 *  * @param ctime     创建时间
 *  * @param mtime     修改时间
 *  * @param cuser     创建人
 *  * @param muser     修改人
 *  * @param deleted     删除标记
 *
 *
 *
 *since :2019.3.2
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@EnableAutoConfiguration
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "创建功能点", notes = "创建功能点")
    @PostMapping("/v1/permissions")
    public void  createPermission(
            @ApiParam(name = "parentId",value = "父功能点ID",required = false,defaultValue = "0")
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Long parentId,
            @ApiParam(name = "name",value = "名称",required = true)
            @RequestParam(value = "name", required = true) String name,
            @ApiParam(name = "isPrivate",value = "是否私有",required = true)
            @RequestParam(value = "isPrivate", required = true) Integer isPrivate,
            @ApiParam(name = "description",value = "功能点描述",required = true)
            @RequestParam(value = "description", required = true) String description,
            @ApiParam(name = "cuser",value = "创建者",required = true)
            @RequestParam(value = "cuser", required = true) Long cuser,
            @ApiParam(name = "muser",value = "修改者",required = true)
            @RequestParam(value = "muser", required = true) Long muser,
            @ApiParam(name = "deleted",value = "删除标记",required = false,defaultValue = "false")
            @RequestParam(value = "deleted", required = false,defaultValue ="false") Boolean deleted
        )
    {
        permissionService.createPermission(parentId,name,isPrivate,
                description,cuser,muser,false);

    }

    @ApiOperation(value = "根据ID查询功能点", notes = "根据ID查询功能点")
    @GetMapping("/v1/permissions")
    public Permission findById(@ApiParam(name = "id",value = "功能点ID",required = true)
                                   @RequestParam(value = "id", required = true)Long id)
    {
        return permissionService.findById(id);
    }

    @ApiOperation(value = "查询所有功能点", notes = "查询所有功能点")
    @GetMapping("/v1/permissions/alldata")
    public List<Permission> findAll()
    {
        return permissionService.findAll();
    }


    @ApiOperation(value="根据ID删除功能点",notes = "根据ID删除功能点")
    @DeleteMapping("v1/permissions")
    public void deleteById(@ApiParam(name = "id",value = "功能点ID",required = true)
                               @RequestParam(value = "id", required = true)Long id)
    {
        permissionService.deleteById(id);
    }

    @ApiOperation(value="删除所有功能点",notes = "删除所有功能点")
    @DeleteMapping("v1/permissions/alldata")
    public void deleteAll()
    {
        permissionService.deleteAll();
    }


}
