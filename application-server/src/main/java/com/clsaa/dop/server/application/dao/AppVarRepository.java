package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.AppVariable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVarRepository extends JpaRepository<AppVariable, Long> {
}
