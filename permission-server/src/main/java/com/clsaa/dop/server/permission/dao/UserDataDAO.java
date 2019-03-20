package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.UserData;
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
     * 根据规则id查询数据列表
     *
     * @param ruleId  规则ID
     *
     * @return {@link List<UserData>}
     */
    public List<UserData> findByRuleId(Long ruleId);

    /**
     * 根据用户ID和作用域参数值和规则ID查找数据
     *
     * @param userId  用户ID
     * @param fieldValue  作用域值
     * @param ruleId  规则ID
     *
     * @return {@link UserData}
     */
    public UserData findByUserIdAndFieldValueAndRuleId(Long userId,Long fieldValue,Long ruleId);

}
