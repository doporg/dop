package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.PermissionConfig;
import com.clsaa.dop.server.application.dao.ProjectRepository;
import com.clsaa.dop.server.application.model.bo.ProjectBoV1;
import com.clsaa.dop.server.application.model.po.Project;
import com.clsaa.dop.server.application.model.vo.ProjectV1;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service(value = "projectService")
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionConfig permissionConfig;

    @Autowired
    private PermissionService permissionService;
    /**
     * 分页查询项目
     *
     * @param pageNo          页号
     * @param pageSize        页大小
     * @param includeFinished 是否包含已结项目
     * @param queryKey        查询关键字
     * @return {@link Pagination<ProjectBoV1>}
     */
    public Pagination<ProjectV1> findProjectOrderByCtimeWithPage(Long loginUser, Integer pageNo, Integer pageSize, Boolean includeFinished, String queryKey) {

        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewProject(), loginUser)
                , BizCodes.NO_PERMISSION);

        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);


        Page<Project> projectPage;
        List<Project> projectList;
        Integer totalCount;

        /*
         *如果有查询关键字 则调用可以带检索的查询
         */
        if (!queryKey.equals("")) {
            //如果包含已结项目则不带状态查询
            if (includeFinished) {
                projectList = projectRepository.findAllByTitleStartingWith(queryKey, pageable).getContent();

                //这一句是为了获得所有条目的数量
                totalCount = projectRepository.countAllByTitleStartingWith(queryKey);
            } else {
                projectList = projectRepository.findAllByStatusAndTitleStartingWith(Project.Status.NORMAL, queryKey, pageable).getContent();
                totalCount = projectRepository.countAllByStatusAndTitleStartingWith(Project.Status.NORMAL, queryKey);
            }
        }
        /*
         *如果没有，则调用全部查询
         */
        else {
            if (includeFinished) {

                projectList = projectRepository.findAll(pageable).getContent();
                totalCount = projectRepository.findAll().size();
            } else {
                projectList = projectRepository.findAllByStatus(Project.Status.NORMAL, pageable).getContent();
                totalCount = projectRepository.findAllByStatus(Project.Status.NORMAL).size();
            }
        }

        //projectList.stream().map(l -> BeanUtils.convertType(l, ProjectV1.class)).collect(Collectors.toList());
        List<ProjectV1> projectV1List = projectList.stream().map(l -> BeanUtils.convertType(l, ProjectV1.class)).collect(Collectors.toList());

        Set userIdList = new HashSet();
        Map<Long, String> idNameMap = new HashMap<>();
        for (int i = 0; i < projectV1List.size(); i++) {
            Long id = projectV1List.get(i).getCuser();

            if (!userIdList.contains(id)) {
                userIdList.add(id);
                try {
                    String userName = this.userService.findUserNameById(id);
                    idNameMap.put(id, userName);
                } catch (Exception e) {
                    System.out.print(e);
                    throw e;
                }

            }

            ProjectV1 projectV1 = projectV1List.get(i);
            projectV1.setCuserName(idNameMap.get(projectV1.getCuser()));
            projectV1List.set(i, projectV1);
        }


        //新建VO层对象 并赋值
        Pagination<ProjectV1> pagination = new Pagination<>();
        pagination.setTotalCount(totalCount);
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        if (projectList.size() == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }
        pagination.setPageList(projectV1List);

        return pagination;
    }

    /**
     * 创建项目
     *
     * @param title       项目名称
     * @param description 项目描述
     */
    public void createProjects(Long loginUser, String title, Long origanizationId, String description) {

        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getCreateProject(), loginUser)
                , BizCodes.NO_PERMISSION);

        LocalDateTime ctime = LocalDateTime.now().withNano(0);
        LocalDateTime mtime = LocalDateTime.now().withNano(0);
        Project project = Project.builder()
                .title(title)
                .description(description)
                .cuser(loginUser)
                .muser(loginUser)
                .is_deleted(false)
                .organizationId(origanizationId)
                .status(Project.Status.NORMAL)
                .ctime(ctime)
                .mtime(mtime)
                .build();
        this.projectRepository.saveAndFlush(project);


    }
}
