package com.clsaa.dop.server.permission.controller;


import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.model.vo.RoleV1;
import com.clsaa.dop.server.permission.service.PermissionService;
import com.clsaa.dop.server.permission.service.RoleService;
import com.clsaa.dop.server.permission.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * 功能点管理控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param id 角色ID
 *  * @param name 角色名称
 *  * @param description 角色描述
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 *
 *since :2019.3.9
 */
@CrossOrigin
@RestController
@EnableAutoConfiguration
public class RoleController {
    @Autowired
    private RoleService roleService;
    @ApiOperation(value = "创建角色", notes = "创建角色")
    @PostMapping("/v1/roles")

    public void  createRole(
            @ApiParam(name = "parentId",value = "父角色ID",required = false,defaultValue = "0")
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Long parentId,
            @ApiParam(name = "name",value = "名称",required = true)
            @RequestParam(value = "name", required = true) String name,
            @RequestHeader(HttpHeaders.X_LOGIN_USER) Long cuser,
            @RequestHeader(HttpHeaders.X_LOGIN_USER) Long muser
    )
    {
        roleService.createRole(parentId,name,cuser,muser);
    }

    @ApiOperation(value = "根据ID查询角色", notes = "根据ID查询角色")
    @GetMapping("/v1/roles/{id}")
    public RoleV1 findById(@ApiParam(name = "id",value = "角色ID",required = true)
                                 @RequestParam(value = "id", required = true)Long id)
    {
        return BeanUtils.convertType(this.roleService.findById(id), RoleV1.class);
    }

    @ApiOperation(value = "分页查询所有角色", notes = "分页查询所有角色")
    @GetMapping("/v1/roles")
    public Pagination<RoleV1> getRoleV1Pagination(
            @ApiParam(name = "pageNo",value = "页号",required = false,defaultValue = "1")
            @RequestParam(value = "pageNo", required = false,defaultValue = "1")Integer page,
            @ApiParam(name = "pageSize",value = "页大小",required = false,defaultValue = "8")
            @RequestParam(value = "pageSize", required = false,defaultValue = "8")Integer size)
    {
        return this.roleService.getRoleV1Pagination(page,size);
    }
    @ApiOperation(value = "根据名称查询角色", notes = "根据名称查询角色")
    @GetMapping("/v1/roles/byName")
    public RoleV1 findByName(@ApiParam(name = "name",value = "角色名称",required = true)
                                   @RequestParam(value = "name", required = true)String name)
    {
        return BeanUtils.convertType(this.roleService.findByName(name), RoleV1.class);
    }

    @ApiOperation(value="根据ID删除角色",notes = "根据ID删除角色")
    @DeleteMapping("v1/roles/{id}")
    public void deleteById(@ApiParam(name = "id",value = "角色ID",required = true)
                           @RequestParam(value = "id", required = true)Long id)
    {
        roleService.deleteById(id);
    }

}
