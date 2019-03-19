package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.dao.UserDataDAO;
import com.clsaa.dop.server.permission.model.po.UserData;
import com.clsaa.dop.server.permission.model.po.UserRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  用户数据表的增删改查
 *
 * @author lzy
 *
 * @since 2019.3.19
 */

@Service
public class UserDataService {
    @Autowired
    private UserDataDAO userDataDAO;
    @Autowired
    private UserRuleService userRuleService;

    /* *
     *
     *  * @param ruleId 规则ID
     *  * @param userId 用户ID
     *  * @param fieldValue 作用域参数值
     *
     *  * @param ctime 创建时间
     *  * @param mtime 修改时间
     *  * @param cuser 创建人
     *  * @param muser 修改人
     *  * @param deleted 删除标记
     *
     * since :2019.3.19
     */
    public void addData(Long ruleId,Long userId,Long fieldValue,Long cuser,Long muser)
    {
        UserData userData=UserData.builder()
                .ruleId(ruleId)
                .userId(userId)
                .fieldValue(fieldValue)
                .cuser(cuser)
                .muser(muser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .build();
        userDataDAO.saveAndFlush(userData);
    }

    public boolean check(Long permissionId,Long userId,String fieldName,Long fieldValue)
    {
        Long ruleId=userRuleService.findUniqueRule("equals",fieldName,permissionId).getId();
        if(ruleId==null)return false;
        List<UserData> userDataList= userDataDAO.findByUserIdAndFieldValue(userId,fieldValue);
        for(UserData userData :userDataList)
        {
            if(userData.getRuleId()==ruleId) return true;
        }
        return false;
    }

    public List<Long> findAllIds(Long permissionId, Long userId,String fieldName)
    {
        Long ruleId=userRuleService.findUniqueRule("in",fieldName,permissionId).getId();
        List<Long> IdList=new ArrayList<>();
        List<UserData> userDataList=userDataDAO.findByRuleId(ruleId);
        for(UserData userData:userDataList){
            if(userData.getUserId()==userId)
            {
                IdList.add(userData.getFieldValue());
            }
        }
        return IdList;
    }
}
