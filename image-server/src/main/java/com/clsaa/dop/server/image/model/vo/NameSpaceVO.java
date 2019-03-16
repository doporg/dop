package com.clsaa.dop.server.image.model.vo;

import com.clsaa.dop.server.image.model.po.NameSpacePO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * <p>
 *     命名空间的视图层对象
 * </p>
 * @author  xzt
 * @since 2019-3-6
 */
@Getter
@Setter
public class NameSpaceVO {
    /**
     * 命名空间的name
     */
    private String name;
    /**
     * 命名空间的创建人
     */
    private Long ouser;
    /**
     * 命名空间内的仓库数
     */
    private Long repoNum;
    /**
     * 命名空间的创建时间
     */
    private LocalDateTime ctime;
    /**
     * 命名空间的最后修改时间
     */
    private LocalDateTime mtime;
    /**
     * 命名空间的状态
     */
    private NameSpacePO.Status status;

}
