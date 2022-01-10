package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository repository;

    @Test
    public void findAllIdByCuser() {
//        List<Long> all = repository.findAllByCuser(20l);
//        System.out.println(all);
        List<Long> idList = repository.findAllIdsByCuser(20l);
        System.out.println(idList.get(0));
        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(2, 8, sort);
        List<Project> projectList = repository.findAllByStatusAndIdIn(Project.Status.NORMAL, pageable, idList).getContent();
        int totalCount = repository.countAllByStatusAndIdIn(Project.Status.NORMAL, idList);
        System.out.println(projectList);
        System.out.println(totalCount);
    }
}
