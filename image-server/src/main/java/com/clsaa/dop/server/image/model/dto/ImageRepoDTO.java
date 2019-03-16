package com.clsaa.dop.server.image.model.dto;

import com.clsaa.dop.server.image.model.po.ImageRepoPO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *  镜像仓库的传输层对象
 *  @author xzt
 *  @since 2019-3-7
 */
@Getter
@Setter
public class ImageRepoDTO {
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
