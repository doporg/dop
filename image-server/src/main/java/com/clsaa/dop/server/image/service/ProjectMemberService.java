package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.ProjectFeign;
import com.clsaa.dop.server.image.model.bo.ProjectMemberBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.*;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目成员的业务实现
 * @author  xzt
 * @since 2019-4-18
 */
@Service
public class ProjectMemberService {
    private final ProjectFeign projectFeign;

    private final UserFeign userFeign;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProjectMemberService(ProjectFeign projectFeign, UserFeign userFeign) {
        this.projectFeign = projectFeign;
        this.userFeign = userFeign;
    }

    /**
     * 获取项目成员分页列表
     * @param pageNo 页号
     * @param pageSize 页大小
     * @param projectId 项目id
     * @param entityName  检索名称
     * @param userId 当前登录用户id
     * @return {@link Pagination<ProjectMemberBO>} 成员的分页类
     */
    public Pagination<ProjectMemberBO> getProjectMembers(Integer pageNo,Integer pageSize,Long projectId, String entityName, Long userId){
        logger.info("[getProjectMembers] Request coming: projectId={}, userId={}, pageNo={}, pageSize={}, entityName={}",projectId,userId,pageNo,pageSize,entityName);
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        logger.info("[getProjectMembers] Get the user auth of repo: auth={}",auth);
        ResponseEntity<List<ProjectMemberEntity>> responseEntity = projectFeign.projectsProjectIdMembersGet(projectId,entityName,auth);
        List<ProjectMemberEntity> projectMemberEntities = responseEntity.getBody();
        Pagination<ProjectMemberBO> pagination = new Pagination<>();

        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        int count = 0 ;
        if (projectMemberEntities!=null){
            count = projectMemberEntities.size();
        }
        pagination.setTotalCount(count);

        if (count==0){
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }else {
            pagination.setPageList(BeanUtils.convertList(projectMemberEntities,ProjectMemberBO.class));
            return pagination;
        }

    }

    /**
     * 向项目中添加成员
     * @param projectId 项目id
     * @param userName 添加的用户名称
     * @param roleId 角色id
     * @param userId 目前登录用户id
     */
    public void addMember(Integer projectId,String userName,Integer roleId,Long userId){

        logger.info("[addMember] Request coming: projectId={}, userName={}, roleId={}, userId={}",projectId,userName,roleId,userId);
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        logger.info("[addMember] Get the user auth of repo: auth={}",auth);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userName);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setRoleId(roleId);
        projectMember.setMemberUser(userEntity);
        projectFeign.projectsProjectIdMembersPost(projectId,projectMember,auth);
    }

    /**
     * @param projectId 项目id
     * @param mid 成员id
     * @param userId 登录用户id
     */

    public void deleteMember(Integer projectId,Long mid,Long userId){
        logger.info("[deleteMember] Request coming: projectId={}, userId={}, mid={}",projectId,userId,mid);
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        logger.info("[deleteMember] Get the user auth of repo: auth={}",auth);
        projectFeign.projectsProjectIdMembersMidDelete(projectId,mid,auth);
    }

    /**
     *
     * @param projectId 项目id
     * @param mid 成员id
     * @param roleId 角色id
     * @param userId 登录用户id
     */
    public void putMember(Integer projectId,Long mid,Integer roleId,Long userId){
        logger.info("[putMember] Request coming: projectId={}, mid={}, roleId={}, userId={}",projectId,mid,roleId,userId);
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        logger.info("[putMember] Get the user auth of repo: auth={}",auth);
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRoleId(roleId);
        projectFeign.projectsProjectIdMembersMidPut(projectId,mid,roleRequest,auth);
    }


}
