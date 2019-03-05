package com.clsaa.dop.server.application.dao.projectManagement;

import com.clsaa.dop.server.application.model.po.projectManagement.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Bowen
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    /**
     * 根据title查询Project
     *
     * @param title 项目名称
     * @return {@link Project} 项目持久层对象
     */
    Project findByTitle(String title);

    /**
     * 根据status查询Project
     *
     * @param status 项目状态
     * @return {@link Page<Project>} 项目持久层对象
     */
    Page<Project> findAllByStatus(Project.Status status, Pageable pageable);

    /**
     * 根据Cuser查询Project
     *
     * @param cuser UserId
     * @return {@link Project} 项目持久层对象
     */
    Project findByCuser(Long cuser);


    //@Query("from App u where u.title=:title")
    //Project findUser(@Param("title") String title);

}
