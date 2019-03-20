package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.UserRuleDAO;
import com.clsaa.dop.server.permission.model.po.UserRule;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  用户数据规则表的增删改查
 *
 * @author lzy
 *
 * @since 2019.3.19
 */

@Service
public class UserRuleService {

    @Autowired
    private UserRuleDAO userRuleDAO;

    /* *
     *
     *  * @param roleId 角色ID
     *  * @param fieldName 权限作用域参数名
     *  * @param rule 数据规则
     *
     *  * @param ctime 创建时间
     *  * @param mtime 修改时间
     *  * @param cuser 创建人
     *  * @param muser 修改人
     *  * @param deleted 删除标记
     *
     * since :2019.3.19
     */
    public void addRule(Long roleId,String fieldName,String rule,Long cuser,Long muser)
    {
        UserRule existUserRule=userRuleDAO.findByRoleIdAndFieldNameAndRule(roleId,fieldName,rule);
        BizAssert.allowed(existUserRule==null, BizCodes.REPETITIVE_RULE);
        UserRule userRule=UserRule.builder()
                .roleId(roleId)
                .fieldName(fieldName)
                .rule(rule)
                .cuser(cuser)
                .muser(muser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .build();
        userRuleDAO.saveAndFlush(userRule);
    }

    //查找唯一规则
    public UserRule findUniqueRule(String rule,String fieldName,Long roleId)
    {
        return userRuleDAO.findByRoleIdAndFieldNameAndRule(roleId,fieldName,rule);
    }
}
