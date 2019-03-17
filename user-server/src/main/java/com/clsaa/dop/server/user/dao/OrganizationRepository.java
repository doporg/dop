package com.clsaa.dop.server.user.dao;


import com.clsaa.dop.server.user.model.po.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author joyren
 */
public interface OrganizationRepository extends JpaRepository<Group, Long> {

}
