package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppYamlData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppYamlRepository extends JpaRepository<AppYamlData, Long> {

    /**
     * 根据应用环境id查询yaml数据
     *
     * @param appEnvId 应用环境id
     * @return {@link Optional<AppYamlData>} 项目持久层对象
     */
    Optional<AppYamlData> findByAppEnvId(Long appEnvId);

}
