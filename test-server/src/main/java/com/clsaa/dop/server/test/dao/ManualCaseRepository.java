package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.ManualCase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public interface ManualCaseRepository extends JpaRepository<ManualCase, Long> {

}
