package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.Project;
import javafx.application.Application;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AppRepository extends JpaRepository<App, Long> {


    /**
     * 根据projectId查询Application
     *
     * @param projectId 项目Id
     * @param pageable  分页
     * @return {@link Page<App>} 项目持久层对象
     */
    Page<App> findAllByProjectId(Long projectId, Pageable pageable);


    /**
     * 根据projectId查询Application
     *
     * @param projectId 项目Id
     * @return {@link List<App>} 项目持久层对象
     */
    List<App> findAllByProjectId(Long projectId);

    /**
     * 根据projectId和应用名称查询Application
     *
     * @param projectId 项目Id
     * @param title     应用名称
     * @return {@link List<App>} 项目持久层对象
     */
    List<App> findAllByProjectIdAndTitleStartingWith(Long projectId, String title);

    /**
     * 根据projectId和应用名称查询Application
     *
     * @param projectId 项目Id
     * @param title     应用名称
     * @param pageable  分页
     * @return {@link Page<App>} 项目持久层对象
     */
    Page<App> findAllByProjectIdAndTitleStartingWith(Long projectId, String title, Pageable pageable);
}
