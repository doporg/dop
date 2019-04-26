package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.UserDataDAO;
import com.clsaa.dop.server.permission.model.bo.RoleBoV1;
import com.clsaa.dop.server.permission.model.po.UserData;
import com.clsaa.dop.server.permission.model.po.UserRule;
import com.clsaa.dop.server.permission.model.vo.UserDataV1;
import com.clsaa.dop.server.permission.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;



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
        UserData existUserData=userDataDAO.findByUserIdAndFieldValueAndRuleId(userId,fieldValue,ruleId);
        BizAssert.allowed(existUserData==null, BizCodes.REPETITIVE_DATA);
        UserRule userRule=userRuleService.findById(ruleId);
        UserData userData=UserData.builder()
                .ruleId(ruleId)
                .userId(userId)
                .fieldValue(fieldValue)
                .description("身为 "+roleService.findById(userRule.getRoleId()).getName()+
                " 有权操作 "+userRule.getFieldName()+" "+userRule.getRule()+" {作用域参数值} 的数据")
                .cuser(cuser)
                .muser(muser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .build();
        userDataDAO.saveAndFlush(userData);
    }

    //验证某个功能点操作的数据是否允许操作
    public boolean check(String permissionName,Long userId,String fieldName,Long fieldValue)
    {
        Long permissionId=permissionService.findByName(permissionName).getId();
        //查找用户所有角色
        List<RoleBoV1> roleBoV1List1=roleService.findByUserId(userId);
        //查找有该功能点的所有角色
        List<RoleBoV1> roleBoV1List2=roleService.findByPermissionId(permissionId);
        //查找用户用来实现该功能点的所有角色
        List<RoleBoV1> roleBoV1List=new ArrayList<>();
        for(RoleBoV1 roleBoV1:roleBoV1List1)
        {
            for(RoleBoV1 roleBoV11:roleBoV1List2)
            {
                if(roleBoV1.getName().equals(roleBoV11.getName()))
                {roleBoV1List.add(roleBoV1);}
            }
        }
        Long ruleId=0L;
        for (RoleBoV1 roleBoV1 : roleBoV1List) {
            if (userRuleService.findUniqueRule("equals", fieldName, roleBoV1.getId()) != null) {
                ruleId = userRuleService.findUniqueRule("equals", fieldName, roleBoV1.getId()).getId();
            }
        }
        if(ruleId==0L)return false;
        return userDataDAO.findByUserIdAndFieldValueAndRuleId(userId, fieldValue, ruleId) != null;
    }

    //得到某个功能点操作允许操作的数据范围（返回ID列表形式）
    public List<Long> findAllIds(String permissionName, Long userId,String fieldName)
    {
        Long permissionId=permissionService.findByName(permissionName).getId();
        //查找用户所有角色
        List<RoleBoV1> roleBoV1List1=roleService.findByUserId(userId);

        //查找有该功能点的所有角色
        List<RoleBoV1> roleBoV1List2=roleService.findByPermissionId(permissionId);

        //查找用户用来实现该功能点的所有角色
        List<RoleBoV1> roleBoV1List=new ArrayList<>();

        //ID list
        List<Long> IdList=new ArrayList<>();
        for(RoleBoV1 roleBoV1:roleBoV1List1)
        {
            for(RoleBoV1 roleBoV11:roleBoV1List2)
            {
                if(roleBoV1.getName().equals(roleBoV11.getName()))
                {roleBoV1List.add(roleBoV1);}
            }
        }
        if(roleBoV1List.isEmpty())return IdList;
        for(RoleBoV1 roleBoV1 :roleBoV1List)
        {System.out.println(roleBoV1.getName());}
        Long ruleId=0L;
        for (RoleBoV1 roleBoV1 : roleBoV1List) {
            if (userRuleService.findUniqueRule("in", fieldName, roleBoV1.getId()) != null) {
                ruleId = userRuleService.findUniqueRule("in", fieldName, roleBoV1.getId()).getId();
            }
        }
        if(ruleId==0L)return IdList;
        System.out.println(ruleId);
        List<UserData> userDataList=userDataDAO.findByRuleId(ruleId);
        for(UserData userData:userDataList){
            if(userData.getUserId()==userId)
            {
                IdList.add(userData.getFieldValue());
            }
        }
        return IdList;
    }

    //根据用户ID查找数据
    public List<UserDataV1> findByUserId(Long userId,String key)
    {
        return userDataDAO.findByUserIdAndDescriptionLike(userId,"%"+key+"%")
                .stream().map(p-> BeanUtils.convertType(p, UserDataV1.class)).collect(Collectors.toList());
    }

    //根据规则ID删除数据
    public void deleteByRuleId(Long ruleId)
    {
        userDataDAO.deleteByRuleId(ruleId);
    }

    //根据ID删除数据
    public void deleteById(Long id)
    {
        userDataDAO.deleteById(id);
    }

    //根据字段值查找用户ID列表
    public List<Long> findUserByField(Long fieldValue,String fieldName)
    {
        HashSet<Long> hashSet=new HashSet<>();
        List<Long> userList=new ArrayList<>();
        List<UserData> userDataList=userDataDAO.findByFieldValue(fieldValue);
        if(userDataList==null)return null;
        for(UserData userData :userDataList)
        {
            UserRule userRule=userRuleService.findById(userData.getRuleId());
            if(userRule!=null&&userRule.getFieldName().equals(fieldName))
            {
                hashSet.add(userData.getUserId());
            }
        }

        userList.addAll(hashSet);
        return userList;
    }

}
