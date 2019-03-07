package com.clsaa.dop.server.image.model.bo;

import com.clsaa.dop.server.image.model.po.ImageRepoPO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *  镜像仓库的业务层对象
 *  @author xzt
 *
 */
@Getter
@Setter
public class ImageRepoBO {
    /**
     * 镜像仓库id
     */
    private Long id;
    /**
     * 镜像仓库的名称
     */
    private String name;
    /**
     * 镜像仓库的描述
     */
    private String description;
    /**
     * 镜像仓库的状态
     */
    private ImageRepoPO.Status status;
    /**
     * 镜像仓库的类型
     */
    private ImageRepoPO.Type type;
    /**
     * 镜像仓库的创建时间
     */
    private LocalDateTime ctime;
    /**
     * 镜像仓库的最后修改时间
     */
    private LocalDateTime mtime;
}
