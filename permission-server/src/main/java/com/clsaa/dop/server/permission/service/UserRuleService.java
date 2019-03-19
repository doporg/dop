package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.dao.UserRuleDAO;
import com.clsaa.dop.server.permission.model.po.UserRule;
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
     *  * @param permissionId 功能点ID
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
    public void addRule(Long permissionId,String fieldName,String rule,Long cuser,Long muser)
    {
        UserRule userRule=UserRule.builder()
                .permissionId(permissionId)
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
    public UserRule findUniqueRule(String rule,String fieldName,Long permissionId)
    {
        List<UserRule> userRuleList= userRuleDAO.findByRule(rule);
        for (UserRule userRule : userRuleList) {
            if (userRule.getFieldName().equals(fieldName) && userRule.getPermissionId() == permissionId) {
                return userRule;
            }
        }
        return null;
    }

}
