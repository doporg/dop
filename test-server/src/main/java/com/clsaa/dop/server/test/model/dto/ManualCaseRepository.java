package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.model.po.ManualCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManualCaseRepository extends JpaRepository<ManualCase, Long> {

}
