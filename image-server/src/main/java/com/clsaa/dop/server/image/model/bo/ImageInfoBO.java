package com.clsaa.dop.server.image.model.bo;

import com.clsaa.dop.server.image.model.po.ImageInfoPO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>镜像业务层对象
 * </p>
 *  @author xzt
 *  @since 2019-3-7
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfoBO {
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
