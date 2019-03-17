package com.clsaa.dop.server.user.dao;


import com.clsaa.dop.server.user.model.po.OrgUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author joyren
 */
public interface OrgUserMappingRepository extends JpaRepository<OrgUserMapping, Long> {

}
