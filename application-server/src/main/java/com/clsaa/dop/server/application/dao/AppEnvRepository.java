package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.AppEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppEnvRepository extends JpaRepository<AppEnvironment, Long> {
}
