package com.clsaa.dop.server.image.model.vo;

import com.clsaa.dop.server.image.model.po.ImageInfoPO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 镜像视图层对象
 * @author xzt
 * @since 2019-3-7
 */
@Getter
@Setter
public class ImageInfoVO {
    /**
     * 镜像的tag（类似于版本号）
     */
    private String tag;

    /**
     * 镜像的大小
     */
    private String size;
    /**
     * 镜像的状态
     */
    private ImageInfoPO.Status status;

    /**
     * 镜像的创建时间
     */
    private LocalDateTime ctime;

    /**
     * 镜像的最后修改时间
     */
    private LocalDateTime mtime;
}
