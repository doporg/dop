package com.clsaa.dop.server.user.service;

import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.dao.OrganizationRepository;
import com.clsaa.dop.server.user.model.bo.OrganizationBoV1;
import com.clsaa.dop.server.user.model.po.Organization;
import com.clsaa.dop.server.user.model.vo.OrganizationV1;
import com.clsaa.dop.server.user.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        Organization organization = Organization.builder()
                .name(name)
                .description(description)
                .deleted(Boolean.FALSE)
                .code((UUID.randomUUID().toString().replace("-", "") +
                        UUID.randomUUID().toString().replace("-", "")).toUpperCase())
                .ouser(ouser)
                .cuser(cuser)
                .muser(muser)
                .mtime(LocalDateTime.now())
                .ctime(LocalDateTime.now())
                .build();
        this.organizationRepository.saveAndFlush(organization);
    }

    /**
     * 删除组织
     *
     * @param id    组织id
     * @param ouser 所属人
     */
    public void deleteOrganizationById(Long id, Long ouser) {
        Organization existOrganization = this.organizationRepository.findOrganizationById(id);
        BizAssert.found(existOrganization != null, BizCodes.ERROR_DELETE);
        BizAssert.found(existOrganization.getOuser().equals(ouser), new BizCode(BizCodes.ERROR_DELETE.getCode(),
                "只有组织拥有者可以删除"));
        this.organizationRepository.delete(existOrganization);
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
        Organization existOrganization = this.organizationRepository.findOrganizationById(id);
        BizAssert.found(existOrganization != null, BizCodes.ERROR_UPDATE);
        existOrganization.setName(name);
        existOrganization.setDescription(description);
        existOrganization.setMtime(LocalDateTime.now());
        this.organizationRepository.saveAndFlush(existOrganization);
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
    public OrganizationBoV1 findOrganizationById(Long id) {
        return BeanUtils
                .convertType(this.organizationRepository.findOrganizationById(id), OrganizationBoV1.class);
    }
}
