package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


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

    /**
     * 查询全部
     *
     * @param {List <Long> idList}
     * @param  pageable 分页
     * @return {@link Page<Role>}
     */

    Page<Role> findByIdIn(List<Long> idList,Pageable pageable);

    /**
     * 根据关键字key过滤查询
     *
     * @param   key 关键字
     * @param  {List <Long> idList}
     * @param  pageable 分页
     * @return {@link Page < Role >}
     */

    Page<Role> findAllByNameLikeAndIdIn(String key, List<Long> idList, Pageable pageable);
}