package com.clsaa.dop.server.image.model.dto;

import com.clsaa.dop.server.image.model.po.ImageInfoPO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 镜像的传输层对象
 * @author xzt
 * @since 2019-3-7
 */
@Getter
@Setter
public class ImageInfoDTO {
    /**
     * 镜像id
     */
    private Long id;

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
