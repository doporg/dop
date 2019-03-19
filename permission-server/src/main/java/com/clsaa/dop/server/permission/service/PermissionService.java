package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.PermissionRepository;
import com.clsaa.dop.server.permission.model.bo.PermissionBoV1;
import com.clsaa.dop.server.permission.model.bo.RoleBoV1;
import com.clsaa.dop.server.permission.model.po.Permission;
import com.clsaa.dop.server.permission.model.po.RolePermissionMapping;
import com.clsaa.dop.server.permission.model.po.UserRoleMapping;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;


/**
 *  功能点的增删改查
 *
 * @author lzy
 *
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleService roleService;

    @Autowired
    //关联关系service
    private RolePermissionMappingService rolePermissionMappingService;
    @Autowired
    //关联关系service
    private UserRoleMappingService userRoleMappingService;

/* *
 *
 *  * @param id 功能点ID
 *  * @param parentId 父功能点ID
 *  * @param name 功能点名称
 *  * @param isPrivate 是否私有
 *  * @param description 功能点描述
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 * since :2019.3.1
 */

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    //创建一个功能点
    public void createPermission(Long parentId,String name,Integer isPrivate,String description,
                                       Long cuser,Long muser)
    {

        Permission existPermission=this.permissionRepository.findByName(name);
        BizAssert.allowed(existPermission==null, BizCodes.REPETITIVE_PERMISSION_NAME);
        Permission permission= Permission.builder()
                .parentId(parentId)
                .name(name)
                .isPrivate(isPrivate)
                .description(description)
                .cuser(cuser)
                .muser(muser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .deleted(false)
                .build();
         permissionRepository.saveAndFlush(permission);

    }

    //根据ID查询功能点
    public Permission findById(Long id)
    {
        Optional<Permission> permission=permissionRepository.findById(id);
        if(permission.isPresent())
        {
            return permission.get();
        }
        return null;
    }
    //分页查询所有功能点
    public Pagination<PermissionV1> getPermissionV1Pagination(Integer pageNo, Integer pageSize)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "mtime");
        int count = (int) this.permissionRepository.count();

        Pagination<PermissionV1> pagination = new Pagination<>();
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.setTotalCount(count);

        if (count == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }

        Pageable pageRequest = PageRequest.of(pagination.getPageNo() - 1, pagination.getPageSize(), sort);
        List<Permission> permissionList = this.permissionRepository.findAll(pageRequest).getContent();
        pagination.setPageList(permissionList.stream().map(p -> BeanUtils.convertType(p, PermissionV1.class)).collect(Collectors.toList()));

        return pagination;
    }

    //根据name查询功能点
    public PermissionBoV1 findByName(String name)
    {
       return BeanUtils.convertType(this.permissionRepository.findByName(name), PermissionBoV1.class);
    }

    //根据ID删除功能点,并删除关联关系
    @Transactional
    public void deleteById(Long id)
    {
        rolePermissionMappingService.deleteByPermissionId(id);
        permissionRepository.deleteById(id);

    }

    //创建或编辑角色时，需要勾选该角色对应的功能点，所以要返回全部功能点
    public List<PermissionBoV1> findAll()
    {
        return permissionRepository.findAll().stream().map(p ->
                BeanUtils.convertType(p, PermissionBoV1.class)).collect(Collectors.toList());
    }

    //根据角色ID查询功能点
    public List<PermissionBoV1> findByRoleId(Long roleId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingService.findByRoleId(roleId);
        List<PermissionBoV1> permissionBoV1List=new ArrayList<>();
        for(RolePermissionMapping rolePermissionMapping:rolePermissionMappingList)
        {
            Optional<Permission> permission=permissionRepository.findById(rolePermissionMapping.getPermissionId());
            if(permission.isPresent())
            {
                permissionBoV1List.add(BeanUtils.convertType(permission.get(),PermissionBoV1.class));
            }
        }
        return permissionBoV1List;
    }

    //根据用户ID查询功能点
    public List<PermissionBoV1> findByUserId(Long userId)
    {
        List<RoleBoV1> roleBoV1List=roleService.findByUserId(userId);
        List<PermissionBoV1> permissionBoV1List=new ArrayList<>();
        /*用Set存功能点ID，再通过功能点ID去获取功能点，去重*/
        Set<Long> permissionIdSet=new HashSet<>();
        roleBoV1List.forEach(roleBoV1 -> {
            List<PermissionBoV1> permissionBoV1ListTmp= this.findByRoleId(roleBoV1.getId());
            permissionBoV1ListTmp.forEach(permissionBoV1 -> {
                permissionIdSet.add(permissionBoV1.getId());
            });
        });
        permissionIdSet.forEach(permissionId->{
           permissionBoV1List.add(BeanUtils.convertType(this.findById(permissionId),PermissionBoV1.class)) ;
        });

        return permissionBoV1List;
    }

    //判断用户是否拥有特定功能点
    public boolean checkUserPermission(Long permissionId,Long userId)
    {
        List<UserRoleMapping> userRoleMappingList=userRoleMappingService.findByUserId(userId);
        for (UserRoleMapping userRoleMapping : userRoleMappingList) {
            if (rolePermissionMappingService.findByRoleIdAndPermissionId(userRoleMapping.getRoleId(), permissionId) != null) {
                return true;
            }
        }
        return false;
    }
}
