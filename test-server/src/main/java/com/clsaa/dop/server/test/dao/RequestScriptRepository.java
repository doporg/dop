package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.InterfaceUrlInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xihao
 * @version 1.0
 * @since 08/03/2019
 */
public interface RequestScriptRepository extends JpaRepository<InterfaceUrlInfo, Long> {


}
