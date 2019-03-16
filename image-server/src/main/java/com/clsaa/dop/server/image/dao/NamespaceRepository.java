package com.clsaa.dop.server.image.dao;

import com.clsaa.dop.server.image.model.po.NameSpacePO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 命名空间Repository
 * @author xzt
 * @since 2019-3-7
 */
public interface NamespaceRepository extends JpaRepository<NameSpacePO,Long>{
    /**
     * 查找ouser的命名空间
     * @param ouser 用户的唯一凭据
     * @return {@link List<NameSpacePO>} 用户的namespace列表
     */
    List<NameSpacePO> findAllByOuser(Long ouser);

    /**
     * 查找用户凭据为identifier
     * @param name 命名空间的name
     * @param ouser 用户的唯一凭据
     * @return {@link NameSpacePO}
     */
    NameSpacePO findByNameAndOuser(String name, Long ouser);

}
