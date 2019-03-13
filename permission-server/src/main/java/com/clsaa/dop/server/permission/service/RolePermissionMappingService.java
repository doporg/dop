package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.PermissionRepository;
import com.clsaa.dop.server.permission.dao.RolePermissionMappingDAO;
import com.clsaa.dop.server.permission.dao.RoleRepository;
import com.clsaa.dop.server.permission.model.bo.PermissionBoV1;
import com.clsaa.dop.server.permission.model.bo.RoleBoV1;
import com.clsaa.dop.server.permission.model.po.Permission;
import com.clsaa.dop.server.permission.model.po.Role;
import com.clsaa.dop.server.permission.model.po.RolePermissionMapping;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.SysexMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *  角色功能点关联关系 的增删改查
 *
 * @author lzy
 *
 * @since 2019.3.9
 */
@Service
public class RolePermissionMappingService {
    @Autowired
    //关联表DAO
    private RolePermissionMappingDAO rolePermissionMappingDAO;
    @Autowired
    //功能点service
    private PermissionService permissionService;
    @Autowired
    //角色service
    private RoleService roleService;

    /* *
     *
     *  * @param id 关联
     *  * @param permissionId 功能点id
     *  * @param roleId 角色id
     *
     *  * @param ctime 创建时间
     *  * @param mtime 修改时间
     *  * @param cuser 创建人
     *  * @param muser 修改人
     *  * @param deleted 删除标记
     *
     * since :2019.3.11
     */

    //添加一个关联关系
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void addPermissionToRole(Long roleId,Long permissionId,Long cuser,Long muser)
    {
        RolePermissionMapping existRolePermissionMapping=this.rolePermissionMappingDAO.
                findByRoleIdAndPermissionId(roleId,permissionId);
        BizAssert.allowed(existRolePermissionMapping==null, BizCodes.REPETITIVE_MAPPING);

        RolePermissionMapping rolePermissionMapping=RolePermissionMapping.builder()
                .roleId(roleId)
                .permissionId(permissionId)
                .cuser(cuser)
                .muser(muser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .deleted(false)
                .build();
        rolePermissionMappingDAO.saveAndFlush(rolePermissionMapping);
    }

    //根据角色ID查询功能点
    public List<PermissionBoV1> findByroleId(Long roleId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingDAO.findByRoleId(roleId);
        List<PermissionBoV1> permissionList=new ArrayList<>();
        for(RolePermissionMapping rolePermissionMapping:rolePermissionMappingList)
        {
            PermissionBoV1 permission= BeanUtils.convertType(
                    permissionService.findById(rolePermissionMapping.getPermissionId()),PermissionBoV1.class);
            permissionList.add(permission);
        }
        return permissionList;
    }
    //根据功能点ID查询角色
    public List<RoleBoV1> findByPermissionId(Long permissionId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingDAO.findByPermissionId(permissionId);
        List<RoleBoV1> roleList=new ArrayList<>();
        rolePermissionMappingList.forEach(rolePermissionMapping -> {
            RoleBoV1 role=BeanUtils.convertType(
                    roleService.findById(rolePermissionMapping.getRoleId()),RoleBoV1.class);
            roleList.add(role);
        });
        return roleList;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    //删除特定角色特定功能点关联关系
    public void delete(Long roleId,Long permissionId)
    {
        RolePermissionMapping existRolePermissionMapping=this.rolePermissionMappingDAO.
                findByRoleIdAndPermissionId(roleId,permissionId);
        rolePermissionMappingDAO.delete(existRolePermissionMapping);
    }

    //根据功能点ID删除关联关系
    public void deleteByPermissionId(Long permissionId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingDAO.findByPermissionId(permissionId);
        rolePermissionMappingList.forEach(rolePermissionMapping -> {
            rolePermissionMappingDAO.delete(rolePermissionMapping);
        });
    }
    //根据角色ID删除关联关系
    public void deleteByRoleId(Long roleId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingDAO.findByRoleId(roleId);
        rolePermissionMappingList.forEach(rolePermissionMapping -> {
            rolePermissionMappingDAO.delete(rolePermissionMapping);
        });
    }

}
