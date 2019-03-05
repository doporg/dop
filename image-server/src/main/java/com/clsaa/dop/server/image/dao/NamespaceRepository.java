package com.clsaa.dop.server.image.dao;

import com.clsaa.dop.server.image.model.po.NameSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 命名空间Repository
 * @author xzt
 */
public interface NamespaceRepository extends JpaRepository<NameSpace,Long>{

    /**
     *
     * @param email
     * @return
     */
    List<NameSpace> findAllByEmail(String email);

}
