package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.UserData;
import com.clsaa.dop.server.permission.model.po.UserRoleMapping;
import com.clsaa.dop.server.permission.model.po.UserRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户数据表DAO层，用来与数据库进行交互
 *
 * @author lzy
 *
 *
 * since :2019.3.19
 */
public interface UserDataDAO extends JpaRepository<UserData, Long>{

    /**
     * 根据用户ID和作用域参数值查找数据
     *
     * @param userId  功能点ID
     * @param fieldValue  作用域名称
     *
     * @return {@link List UserData}
     */
    public List<UserData> findByUserIdAndFieldValue(Long userId,Long fieldValue);

    /**
     * 根据规则id查询数据列表
     *
     * @param ruleId  规则ID
     *
     * @return {@link List<UserData>}
     */
    public List<UserData> findByRuleId(Long ruleId);

}
