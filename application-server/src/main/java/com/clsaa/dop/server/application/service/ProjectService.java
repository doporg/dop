package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.ProjectRepository;
import com.clsaa.dop.server.application.model.bo.ProjectBoV1;
import com.clsaa.dop.server.application.model.po.Project;
import com.clsaa.dop.server.application.model.vo.ProjectV1;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service(value = "projectService")
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 分页查询项目
     *
     * @param pageNo          页号
     * @param pageSize        页大小
     * @param includeFinished 是否包含已结项目
     * @param queryKey        查询关键字
     * @return {@link Pagination< ProjectBoV1>}
     */
    public Pagination<ProjectV1> findProjectOrderByCtimeWithPage(Integer pageNo, Integer pageSize, Boolean includeFinished, String queryKey) {

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
                totalCount = projectRepository.findAllByTitleStartingWith(queryKey).size();
            } else {
                projectList = projectRepository.findAllByStatusAndTitleStartingWith(Project.Status.NORMAL, queryKey, pageable).getContent();
                totalCount = projectRepository.findAllByStatusAndTitleStartingWith(Project.Status.NORMAL, queryKey).size();
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

        //新建VO层对象 并赋值
        Pagination<ProjectV1> pagination = new Pagination<>();
        pagination.setTotalCount(totalCount);
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        if (projectList.size() == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }
        pagination.setPageList(projectList.stream().map(l -> BeanUtils.convertType(l, ProjectV1.class)).collect(Collectors.toList()));

        return pagination;
    }

    /**
     * 创建项目
     *
     * @param title       项目名称
     * @param description 项目描述
     * @return {@link Pagination< ProjectBoV1>}
     */
    public void createProjects(Long cuser, String title, Long origanizationId, String description) {

        LocalDateTime ctime = LocalDateTime.now().withNano(0);
        LocalDateTime mtime = LocalDateTime.now().withNano(0);
        Project project = Project.builder()
                .title(title)
                .description(description)
                .cuser(cuser)
                .muser(cuser)
                .is_deleted(false)
                .organizationId(origanizationId)
                .status(Project.Status.NORMAL)
                .ctime(ctime)
                .mtime(mtime)
                .build();
        this.projectRepository.saveAndFlush(project);
        return;
    }


}
