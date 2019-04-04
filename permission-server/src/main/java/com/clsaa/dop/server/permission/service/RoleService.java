package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.PermissionRepository;
import com.clsaa.dop.server.permission.dao.RoleRepository;
import com.clsaa.dop.server.permission.model.bo.PermissionBoV1;
import com.clsaa.dop.server.permission.model.bo.RoleBoV1;
import com.clsaa.dop.server.permission.model.po.Permission;
import com.clsaa.dop.server.permission.model.po.Role;
import com.clsaa.dop.server.permission.model.po.RolePermissionMapping;
import com.clsaa.dop.server.permission.model.po.UserRoleMapping;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.model.vo.RoleV1;
import com.clsaa.dop.server.permission.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *  角色的增删改查
 *
 * @author lzy
 *
 * @since 2019.3.9
 */

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    //功能点service
    private PermissionService permissionService;

    @Autowired
    //关联关系service
    private RolePermissionMappingService rolePermissionMappingService;

    @Autowired
    //关联关系service
    private UserRoleMappingService userRoleMappingService;
    @Autowired
    //用户数据规则
    private UserRuleService userRuleService;
    @Autowired
    //用户数据
    private UserDataService userDataService;

    /* *
     *
     *  * @param id 角色ID
     *  * @param parentId 父角色ID
     *  * @param name 角色名称
     *
     *  * @param ctime 创建时间
     *  * @param mtime 修改时间
     *  * @param cuser 创建人
     *  * @param muser 修改人
     *  * @param deleted 删除标记
     *
     * since :2019.3.1
     */

    //创建一个角色
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Long createRole(Long parentId,String name, Long cuser,Long muser)
    {
            if(permissionService.checkUserPermission("创建角色",cuser))
            {
                Role existRole=this.roleRepository.findByName(name);
                BizAssert.allowed(existRole==null, BizCodes.REPETITIVE_ROLE_NAME);
                Role role= Role.builder()
                        .parentId(parentId)
                        .name(name)
                        .cuser(cuser)
                        .muser(muser)
                        .ctime(LocalDateTime.now())
                        .mtime(LocalDateTime.now())
                        .deleted(false)
                        .build();
                roleRepository.saveAndFlush(role);
                userDataService.addData(
                        userRuleService.findUniqueRule("in","roleId",
                                roleRepository.findByName("权限管理员").getId()).getId(),
                        cuser,role.getId(),cuser,muser);
                return role.getId();
            }
            return null;

    }

    //根据ID查询角色
    public Role findById(Long id)
    {
        Optional<Role> role=roleRepository.findById(id);
        if(role.isPresent())
        {
            return role.get();
        }
        return null;
    }
    //根据name查询角色
    public RoleBoV1 findByname(String name)
    {
        return BeanUtils.convertType(this.roleRepository.findByName(name), RoleBoV1.class);
    }
    //分页查询所有角色
    public Pagination<RoleV1> getRoleV1Pagination(Integer pageNo, Integer pageSize,Long userId)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "mtime");
        int count=0;

        Pagination<RoleV1> pagination = new Pagination<>();
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);

        List<Role> roleList = this.roleRepository.findAll(sort);
        List<Long> idList=userDataService.findAllIds("查询角色",userId,"roleId");

        List<Role> roleList1=new ArrayList<>();
        for(Role role :roleList)
        {
            for(Long id :idList)
            {
                if(role.getId()==id)
                {roleList1.add(role);count++;}
            }
        }
        pagination.setTotalCount(count);
        if (count == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }
        roleList1=roleList1.subList((pageNo-1)*pageSize, (pageNo*pageSize<count)? pageNo*pageSize:count);

        pagination.setPageList(roleList1.stream().map(p -> BeanUtils.convertType(p, RoleV1.class)).collect(Collectors.toList()));
        return pagination;
    }
    //根据name查询角色
    public RoleBoV1 findByName(String name)
    {
        return BeanUtils.convertType(this.roleRepository.findByName(name), RoleBoV1.class);
    }
    //根据ID删除角色,并删除关联关系和数据规则
    @Transactional
    public void deleteById(Long id ,Long userId)
    {
        if(permissionService.checkUserPermission("删除角色",userId))
        {
            if(userDataService.check("删除角色",userId,"roleId",id))
            {
                rolePermissionMappingService.deleteByRoleId(id);
                userRuleService.deleteByRoleId(id);
                roleRepository.deleteById(id);
            }
        }
    }

    //查询所有权限的ID和名称
    public List<PermissionBoV1> findAllPermission()
    {
        return permissionService.findAll();
    }

    //根据功能点ID查询角色
    public List<RoleBoV1> findByPermissionId(Long permissionId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingService.findByPermissionId(permissionId);
        List<RoleBoV1> roleBoV1List=new ArrayList<>();
        rolePermissionMappingList.forEach(rolePermissionMapping -> {
            Optional<Role> role=roleRepository.findById(rolePermissionMapping.getRoleId());
            if(role.isPresent())
            {
                roleBoV1List.add(BeanUtils.convertType(role.get(),RoleBoV1.class));
            }
        });
        return roleBoV1List;
    }

    //根据用户ID查询角色
    public List<RoleBoV1> findByUserId(Long userId)
    {
        List<UserRoleMapping> userRoleMappingList=userRoleMappingService.findByUserId(userId);
        List<RoleBoV1> roleBoV1List =new ArrayList<>();
        userRoleMappingList.forEach(userRoleMapping -> {
            Optional<Role> role=roleRepository.findById(userRoleMapping.getRoleId());
            if(role.isPresent())
            {
                roleBoV1List.add(BeanUtils.convertType(role.get(),RoleBoV1.class));
            }
        });
        return roleBoV1List;
    }

    //查询所有角色，为用户绑定时使用
    public List<RoleBoV1> findAllRole()
    {
        return roleRepository.findAll().stream().map(p ->
                BeanUtils.convertType(p, RoleBoV1.class)).collect(Collectors.toList());
    }

}
