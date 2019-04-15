package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Bowen
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @param title  搜索字符串
     * @return {@link Page<Project>} 项目持久层对象
     */
    //@Query(value = "select p from Project p where p.status=:status and p.searchString like :searchString % ORDER BY ?#{#pageable}",
    //        countQuery = "SELECT count(*) FROM Project WHERE STATUS=:status")
    Page<Project> findAllByStatusAndTitleStartingWith(Project.Status status, String title, Pageable pageable);

    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @param title  搜索字符串
     * @return {@link Integer} 项目持久层对象
     */
    //@Query(value = "select p from Project p where p.status=:status and p.searchString like :searchString % ORDER BY ?#{#pageable}",
    //        countQuery = "SELECT count(*) FROM Project WHERE STATUS=:status")
    Integer countAllByStatusAndTitleStartingWith(Project.Status status, String title);

    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @return {@link Page<Project>} 项目持久层对象
     */
    //@Query(value = "select p from Project p where p.status=:status and p.searchString like :searchString % ORDER BY ?#{#pageable}",
    //        countQuery = "SELECT count(*) FROM Project WHERE STATUS=:status")
    Page<Project> findAllByStatus(Project.Status status, Pageable pageable);

    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @return {@link Page<Project>} 项目持久层对象
     */
    //@Query(value = "select p from Project p where p.status=:status and p.searchString like :searchString % ORDER BY ?#{#pageable}",
    //        countQuery = "SELECT count(*) FROM Project WHERE STATUS=:status")
    List<Project> findAllByStatus(Project.Status status);

    /**
     * 根据关键词查询Project
     *
     * @param Title 项目名称
     * @return {@link Integer} 项目持久层对象
     */

    Integer countAllByTitleStartingWith(String Title);

    /**
     * 根据关键词查询Project
     *
     * @param Title 项目名称
     * @return {@link Page<Project>} 项目持久层对象
     */
    Page<Project> findAllByTitleStartingWith(String Title, Pageable pageable);
}
