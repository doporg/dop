package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.Permission;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * 功能点类，对应功能点表中每条数据
 *
 * @author lzy
 *
 *
 * since :2019.3.1
 */

public interface PermissionRepository extends JpaRepository<Permission, Long>
{


}