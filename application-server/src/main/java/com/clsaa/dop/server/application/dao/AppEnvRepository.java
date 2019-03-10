package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.AppEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppEnvRepository extends JpaRepository<AppEnvironment, Long> {


    /**
     * 根据projectId查询Application
     *
     * @param appId 应用Id
     * @return {@link  List<AppEnvironment> } 应用变量列表
     */
    List<AppEnvironment> findAllByAppId(Long appId);
}
