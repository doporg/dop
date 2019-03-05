package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.dao.projectManagement.ProjectRepository;
import com.clsaa.dop.server.application.model.bo.ProjectBoV1;
import com.clsaa.dop.server.application.model.po.projectManagement.Project;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service(value = "projectService")
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectBoV1> findProjectOrderByCtimeWithPage(Integer pageNo, Integer pageSize, Boolean includeFinished) {

        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        //
        Page<Project> projectPage = projectRepository.findAllByStatus(Project.Status.NORMAL, pageable);
        List<Project> projectList = projectPage.getContent();
        Integer totalPage = projectPage.getTotalPages();
        if (includeFinished) {
            projectList = projectRepository.findAll(pageable).getContent();
        }
        List<ProjectBoV1> projectBoV1List = new ArrayList<>();
        for (Project item : projectList) {
            projectBoV1List.add(BeanUtils.convertType(item, ProjectBoV1.class));
        }

        return projectBoV1List;
    }

    public boolean createProjects(String title, String description) {
        Long cuser = 1234L;
        Long muser = 1234L;
        LocalDateTime ctime = LocalDateTime.now().withNano(0);
        LocalDateTime mtime = LocalDateTime.now().withNano(0);
        Project project = Project.builder()
                .title(title)
                .description(description)
                .cuser(cuser)
                .muser(muser)
                .deleted(false)
                .organizationId(123L)
                .status(Project.Status.NORMAL)
                .ctime(ctime)
                .mtime(mtime)
                .build();
        this.projectRepository.saveAndFlush(project);
        return true;
    }


}
