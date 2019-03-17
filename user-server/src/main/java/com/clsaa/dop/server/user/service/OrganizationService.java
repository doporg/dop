package com.clsaa.dop.server.user.service;

import com.clsaa.dop.server.user.dao.OrganizationRepository;
import com.clsaa.dop.server.user.model.vo.OrganizationV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组织，业务实现类
 *
 * @author joyren
 */
@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * 添加一个组织
     *
     * @param name        组织名
     * @param description 组织描述
     * @param ouser       所属人
     * @param cuser       创建人
     * @param muser       修改人
     */
    public void addOrganization(String name, String description, Long ouser, Long cuser, Long muser) {
    }

    /**
     * 删除组织
     *
     * @param id    组织id
     * @param ouser 所属人
     */
    public void deleteOrganizationById(Long id, Long ouser) {
    }

    /**
     * 更新组织信息
     *
     * @param id          组织id
     * @param name        组织名
     * @param description 组织描述
     * @param muser       修改人
     */
    public void updateOrganization(Long id, String name, String description, Long muser) {
    }

    /**
     * 查询一个人全部所属组织
     *
     * @param userId 用户id
     * @return {@link List<OrganizationV1>}
     */
    public List<OrganizationV1> findOrganizationByUser(Long userId) {
        return null;
    }

    /**
     * 根据id查询一个组织信息
     *
     * @param id 组织id
     * @return {@link OrganizationV1}
     */
    public OrganizationV1 findOrganizationById(Long id) {
        return null;
    }
}
