package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.TestStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestStepRepository extends JpaRepository<TestStep, Long> {

}
