package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.BuildTagRunningIdMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildTagRunningIdMappingRepository extends JpaRepository<BuildTagRunningIdMapping, Long> {
    /**
     * 根据运行Id查询对应关系
     *
     * @param runningId 运行Id
     * @param appEnvId  应用环境Id
     * @return {@link BuildTagRunningIdMapping} 版本号运行Id对应关系
     */
    Optional<BuildTagRunningIdMapping> findByRunningIdAndAppEnvId(String runningId, Long appEnvId);
}
