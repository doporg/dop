package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.AppEnvLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppEnvLogRepository extends JpaRepository<AppEnvLog, Long> {
    /**
     * 根据appEnvId查询对应的environmentId
     *
     * @param runningId appEnvId
     * @return {@link   Optional<AppEnvLog> } 对应关系
     */
    Optional<AppEnvLog> findByRunningId(String runningId);
}
