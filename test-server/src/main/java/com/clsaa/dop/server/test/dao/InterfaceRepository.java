package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.InterfaceCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterfaceRepository extends JpaRepository<InterfaceCase, Long> {

}
