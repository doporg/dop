package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.Permission;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * 功能点DAO层，用来与数据库交互
 *
 * @author lzy
 *
 *
 * since :2019.3.1
 */

public interface PermissionRepository extends JpaRepository<Permission, Long>
{


    /**
     * 根据name查询功能点
     *
     * @param name 功能点名称
     * @return {@link Permission}
     */
    Permission findByName(String name);

    /**
     * 根据关键字key过滤查询
     *
     * @param key 关键字
     * @return {@link List < Permission >}
     */

    List<Permission> findByNameLike(String key);
}