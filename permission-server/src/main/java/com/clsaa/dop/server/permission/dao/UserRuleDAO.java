package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.UserRoleMapping;
import com.clsaa.dop.server.permission.model.po.UserRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户数据规则表DAO层，用来与数据库进行交互
 *
 * @author lzy
 *
 *
 * since :2019.3.19
 */
public interface UserRuleDAO extends JpaRepository<UserRule, Long> {

    /**
     * 根据功能点ID和作用域名称查询规则
     *
     * @param permissionId  功能点ID
     * @param fieldName  作用域名称
     *
     * @return {@link List<UserRule>}
     */
    public UserRule findByPermissionIdAndFieldName(Long permissionId,String fieldName);
    /**
     * 根据规则类型查询规则
     *
     * @param rule  规则类型
     *
     * @return {@link List<UserRule>}
     */
    public List<UserRule> findByRule(String rule);

}
