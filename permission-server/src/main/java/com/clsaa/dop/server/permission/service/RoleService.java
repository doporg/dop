package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.PermissionRepository;
import com.clsaa.dop.server.permission.dao.RoleRepository;
import com.clsaa.dop.server.permission.model.bo.PermissionBoV1;
import com.clsaa.dop.server.permission.model.bo.RoleBoV1;
import com.clsaa.dop.server.permission.model.po.Permission;
import com.clsaa.dop.server.permission.model.po.Role;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.model.vo.RoleV1;
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

    /* *
     *
     *  * @param id 角色ID
     *  * @param parentId 父角色ID
     *  * @param name 角色名称
     *  * @param description 角色描述
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
    public void createRole(Long parentId,String name,String description, Long cuser,Long muser)
    {
        Role existRole=this.roleRepository.findByName(name);
        BizAssert.allowed(existRole==null, BizCodes.REPETITIVE_ROLE_NAME);
        Role role= Role.builder()
                .parentId(parentId)
                .name(name)
                .description(description)
                .cuser(cuser)
                .muser(muser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .deleted(false)
                .build();
        roleRepository.saveAndFlush(role);

    }

    //根据ID查询角色
    public RoleBoV1 findById(Long id)
    {
        return BeanUtils.convertType(this.roleRepository.findById(id),RoleBoV1.class);
    }
    //根据name查询角色
    public RoleBoV1 findByname(String name)
    {
        return BeanUtils.convertType(this.roleRepository.findByName(name), RoleBoV1.class);
    }
    //分页查询所有角色
    public Pagination<RoleV1> getRoleV1Pagination(Integer pageNo, Integer pageSize)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "mtime");
        int count = (int) this.roleRepository.count();

        Pagination<RoleV1> pagination = new Pagination<>();
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.setTotalCount(count);

        if (count == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }

        Pageable pageRequest = PageRequest.of(pagination.getPageNo() - 1, pagination.getPageSize(), sort);
        List<Role> roleList = this.roleRepository.findAll(pageRequest).getContent();
        pagination.setPageList(roleList.stream().map(p -> BeanUtils.convertType(p, RoleV1.class)).collect(Collectors.toList()));

        return pagination;
    }
    //根据name查询角色
    public RoleBoV1 findByName(String name)
    {
        return BeanUtils.convertType(this.roleRepository.findByName(name), RoleBoV1.class);
    }
    //根据ID删除角色
    @Transactional
    public void deleteById(Long id)
    {
        roleRepository.deleteById(id);
    }

}
