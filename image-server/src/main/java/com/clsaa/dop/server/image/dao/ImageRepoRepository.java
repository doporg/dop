package com.clsaa.dop.server.image.dao;

import com.clsaa.dop.server.image.model.po.ImageRepoPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 镜像仓库repository
 * @author  xzt
 */
public interface ImageRepoRepository extends JpaRepository<ImageRepoPO,Long> {

}
