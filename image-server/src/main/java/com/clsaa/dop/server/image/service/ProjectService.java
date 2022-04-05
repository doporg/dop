package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.ProjectFeign;
import com.clsaa.dop.server.image.model.bo.ProjectBO;
import com.clsaa.dop.server.image.model.dto.ProjectDto1;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.Project;
import com.clsaa.dop.server.image.model.po.ProjectMetadata;
import com.clsaa.dop.server.image.model.po.ProjectReq;
import com.clsaa.dop.server.image.model.po.PublicStatus;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.dop.server.image.util.TimeConvertUtil;
import com.clsaa.rest.result.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  项目的业务实现
 * </p>
 * @author xzt
 * @since 2019-3-23
 */
@Service
public class ProjectService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProjectFeign projectFeign;
    private final UserFeign userFeign;

    @Autowired
    public ProjectService(ProjectFeign projectFeign, UserFeign userFeign) {
        this.projectFeign = projectFeign;
        this.userFeign = userFeign;
    }

    /**
     * 返回对应条件的项目列表
     * @param name 项目名称
     * @param publicStatus 类型
     * @param owner 创建人
     * @param page 页号
     * @param pageSize 页大小
     * @return {@link List<ProjectBO>} 项目信息列表
     */
    public Pagination<ProjectBO> getProjects(String name, Boolean publicStatus, String owner, Integer page, Integer pageSize, Long userId){
        logger.info("[getProjects] Request coming: name={}, publicStatus={}, owner={}, page={}, pageSize={}, userId={}",name,publicStatus,owner,page,pageSize,userId);
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        logger.info("[getProjects] Get the user auth of repo: auth={}",auth);
        ResponseEntity<List<Project>> responseEntity = projectFeign.projectsGet(name,publicStatus,owner,page,pageSize,auth);

        Pagination<ProjectBO> pagination = new Pagination<>();
        List<Project> projects = responseEntity.getBody();
        List<String> httpHeader = responseEntity.getHeaders().get("X-Total-Count");
        int count = 0;
        if (httpHeader!=null){
            count = Integer.parseInt(httpHeader.get(0));
        }

        pagination.setPageSize(pageSize);
        pagination.setPageNo(page);
        pagination.setTotalCount(count);
        if (count==0){
            pagination.setPageList(Collections.emptyList());
            return  pagination;
        }else {
            List<ProjectBO> projectBOS = new ArrayList<>();
            if (projects!=null){
                for(Project project:projects){
                    ProjectBO projectBO = BeanUtils.convertType(project,ProjectBO.class);
                    projectBO.setCreationTime(TimeConvertUtil.convertTime(project.getCreationTime()));
                    if (project.getCurrentUserRoleId()==1) {
                        projectBO.setCurrentUserRole("Namespace Manager");
                    }else if(project.getCurrentUserRoleId()==0){
                        projectBO.setCurrentUserRole("Tourist");
                    }else {
                        projectBO.setCurrentUserRole("Developer");
                    }
                    projectBOS.add(projectBO);
                }
            }
            pagination.setPageList(projectBOS);
            return pagination;
        }
    }

    /**
     * 获取项目id为id的项目的信息
     * @param id 项目id
     * @return {@link ProjectBO} 项目信息
     */
    public ProjectBO getProjectById(Long id,Long userId){
        logger.info("[getProjectById] Request coming: id={}, userId={}",id,userId);
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        logger.info("[getProjectById] Get the user auth of repo: auth={}",auth);
        Project project = projectFeign.projectsProjectIdGet(id,auth);
        ProjectBO projectBO = BeanUtils.convertType(project,ProjectBO.class);
        if (project.getCurrentUserRoleId()==1) {
            projectBO.setCurrentUserRole("Namespace Manager");
        }else if(project.getCurrentUserRoleId()==2){
            projectBO.setCurrentUserRole("Developer");
        }else{
            projectBO.setCurrentUserRole("Tourist");
        }
        return projectBO;
    }

    /**
     * 创建新项目
     * @param project 项目信息
     */
    public void addProject(ProjectDto1 project,Long userId){
        logger.info("[addProject] Request coming: userId={}, project",userId);
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        logger.info("[addProject] Get the user auth of repo: auth={}",auth);
        try {
            ProjectReq project1 = BeanUtils.convertType(project,ProjectReq.class);
            projectFeign.projectsPost(project1, auth);
        } catch (Exception e) {
            logger.error("[addProject] failed to create a new project: exception={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 修改项目的基本信息
     * @param projectId 项目id
     * @param projectDto1 项目内容
     */
    public void putProject(Long projectId,ProjectDto1 projectDto1,Long userId){
        logger.info("[putProject] Request coming: projectId={}, userId={}, projectDto1",projectId,userId);
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        logger.info("[putProject] Get the user auth of repo: auth={}",auth);
        ProjectReq projectReq = BeanUtils.convertType(projectDto1,ProjectReq.class);
        projectFeign.projectsProjectIdPut(projectId,projectReq,auth);
    }

    /**
     * 通过项目id删除项目
     * @param projectId
     */
    public void deleteProject(Long projectId,Long userId){
        logger.info("[deleteProject] Request coming: projectId={}, userId={}",projectId,userId);
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        logger.info("[deleteProject] Get the user auth of repo: auth={}",auth);
        projectFeign.projectsProjectIdDelete(projectId,auth);
    }

    /**
     * 通过项目id检索项目信息
     * @param projectId 项目id
     * @return {@link ProjectMetadata} 项目基本信息
     */
    public ProjectMetadata getProjectMetadata(Long projectId,Long userId){
        logger.info("[getProjectMetadata] Request coming: projectId={}, userId={}",projectId,userId);
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        logger.info("[getProjectMetadata] Get the user auth of repo: auth={}",auth);
        return projectFeign.projectsProjectIdMetadatasGet(projectId,auth);
    }

    /**
     * 为项目添加基本信息
     * @param projectMetadata 项目的基本信息
     */
    public void addProjectMetadata(Long projectId, ProjectMetadata projectMetadata,Long userId){
        logger.info("[addProjectMetadata] Request coming: projectId={}, userId={}, projectMetadata",projectId,userId);
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        logger.info("[addProjectMetadata] Get the user auth of repo: auth={}",auth);
        projectFeign.projectsProjectIdMetadatasPost(projectId,projectMetadata,auth);
    }
    /**
     * 通过项目id和metadata名称删除对应属性
     * @param projectId 项目id
     * @param mataName 项目名称
     */
    public void deleteProjectMetadata(Long projectId, String mataName,Long userId){
        logger.info("[deleteProjectMetadata] Request coming: projectId={}, userId={}, mataName={}",projectId,userId,mataName);
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);
        logger.info("[deleteProjectMetadata] Get the user auth of repo: auth={}",auth);
        projectFeign.projectsProjectIdMetadatasMetaNameDelete(projectId,mataName,auth);
    }

    /**
     * 修改项目的公开状态
     * @param projectId 项目id
     * @param metaName 属性名称
     * @param userId 用户id
     * @param publicStatus 修改之后的状态
     */
    public void updatePublicStatus(Long projectId,String metaName,Long userId,String publicStatus){
        logger.info("[updatePublicStatus] Request coming: projectId={}, userId={}, metaName={}, publicStatus={}",projectId,userId,metaName,publicStatus);
        UserCredentialDto userCredentialDto= userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        logger.info("[updatePublicStatus] Get the user auth of repo: auth={}",auth);
        PublicStatus publicStatus1 = new PublicStatus();
        publicStatus1.setPublicStatus(publicStatus);
        projectFeign.projectsProjectIdMetadatasMetaNamePut(projectId,metaName,auth,publicStatus1);
    }
}
