package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.Permission;
import com.clsaa.dop.server.permission.model.po.Role;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 角色DAO层，用来与数据库进行交互
 *
 * @author lzy
 *
 *
 * since :2019.3.7
 */

public interface RoleRepository extends JpaRepository<Role, Long>
{
    /**
     * 根据name查询角色
     *
     * @param name 角色名称
     * @return {@link Role}
     */
    Role findByName(String name);

}