package com.clsaa.dop.server.image.model.po;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  镜像仓库基本信息持久层对象
 * </p>
 * @author xzt
 * @since 2019-3-6
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_Image_repo",schema = "dp_dop_image_server",
        uniqueConstraints = {@UniqueConstraint(columnNames ={"name"})},
        indexes = {@Index(columnList = "name")}
        )
public class ImageRepoPO implements Serializable {
    private static final long serialVersionUID = 6906097418517275871L;

    /**
     * 镜像仓库状态
     */
    public enum Status{
        /**
         * 镜像仓库属于正常状态
         */
        NORMAL("NORMAL"),
        /**
         *  镜像仓库异常
         */
        ABNORMAL("ABNORMAL");
        private String code;
        private Status(String code){ }

    }
    /**
     * 镜像仓库的类型
     *
     */
    public enum Type{
        /**
         * 公开
         */
        PUBLIC("PUBLIC"),
        /**
         * 私有
         */
        PRIVATE("PRIVATE");
        private String code;
        private Type(String code){ }
    }
    /**
     * 镜像仓库的id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 镜像仓库的名称
     */
    @Basic
    @Column(name = "name")
    private String name;

    /**
     *  镜像仓库的基本描述
     */
    @Basic
    @Column(name = "description")
    private String description;
    /**
     * 镜像仓库中的镜像
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<ImageInfoPO> imageInfos;
    /**
     * 镜像仓库的状态
     */
    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    /**
     * 镜像仓库的类型
     */
    @Basic
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    /**
     *  镜像仓库的创建时间
     *
     */
    @Basic
    @Column(name = "ctime")
    private LocalDateTime ctime;
    /**
     * 镜像仓库的最后修改时间
     */
    @Basic
    @Column(name = "mtime")
    private LocalDateTime mtime;
}
