package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xihao
 * @version 1.0
 * @since 30/03/2019
 */
public interface InterfaceCaseLogRepository extends JpaRepository<InterfaceExecuteLog, Long> {

}
