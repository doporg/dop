package com.clsaa.dop.server.permission.controller;


import com.clsaa.dop.server.permission.service.PermissionService;
import com.clsaa.dop.server.permission.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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
}
