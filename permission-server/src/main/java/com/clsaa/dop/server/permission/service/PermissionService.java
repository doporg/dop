package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.dao.PermissionRepository;
import com.clsaa.dop.server.permission.model.po.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;


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
/* *
 *
 * @param id            功能点ID
 * @param parentId     父功能点ID
 * @param name     功能点名称
 * @param isPrivate         是否私有
 * @param description     功能点描述
 * @param ctime     创建时间
 * @param mtime     修改时间
 * @param cuser     创建人
 * @param muser     修改人
 * @param deleted     删除标记
 *
 * since :2019.3.1
 */


    //创建一个功能点
    public void createPermission(Long parentId,String name,Integer isPrivate,String description,
                                       Long cuser,Long muser,Boolean deleted)
    {
        Permission permission= Permission.builder().
                parentId(parentId)
                .name(name)
                .isPrivate(isPrivate)
                .description(description)
                .cuser(cuser)
                .muser(muser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .deleted(deleted)
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
    //查询所有功能点
    public List<Permission> findAll()
    {
        Sort sort = new Sort(Sort.Direction.DESC, "mtime");
        List<Permission> PermissionList=permissionRepository.findAll(sort);
        return PermissionList;
    }

    //根据ID删除功能点
    @Transactional

    public void deleteById(Long id)
    {
        permissionRepository.deleteById(id);
    }
    //删除所有功能点
    public void deleteAll()
    {
        permissionRepository.deleteAll();
    }
}
