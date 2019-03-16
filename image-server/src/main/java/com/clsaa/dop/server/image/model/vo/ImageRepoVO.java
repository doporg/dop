package com.clsaa.dop.server.image.model.vo;

import com.clsaa.dop.server.image.model.po.ImageRepoPO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *  <p>
 *   镜像仓库的视图层对象
 *  </p>
 *  @author xzt
 *  @since 2019-3-7
 */
@Getter
@Setter
public class ImageRepoVO {
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
