package com.clsaa.dop.server.image.model.po;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 命名空间持久层对象
 * </p>
 * @author xzt
 * @since 2019-3-5
 */
@Getter
@Builder
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "t_namespace", schema = "db_dop_image_server",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name","identifier"})},
        indexes = {@Index(columnList ="name")})
public class NameSpacePO implements Serializable{
    private static final long serialVersionUID = 6906097418517275871L;

    /**
     * @author xzt
     * 用户命名空间的状态
     */
    public enum Status{
        /**
         * 正常状态
         */
        NORMAL("NORMAL"),
        ABNORMAL("ABNORMAL");
        private String code;
        Status(String code){}
    }
    /**
     * 命名空间id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 命名空间名称
     */
    @Basic
    @Column(name = "name")
    private String name;
    /**
     * 创建人的唯一标识
     */
    @Basic
    @Column(name = "identifier")
    private String identifier;
    /**
     * 命名空间里的镜像仓库
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<ImageRepoPO> imageRepoPOS;
    /**
     * 命名空间创建时间
     */
    @Basic
    @Column(name = "ctime")
    private LocalDateTime ctime;

    /**
     * 命名空间修改时间
     */
    @Basic
    @Column(name = "mtime")
    private LocalDateTime mtime;

    /**
     * 命名空间的状态
     */
    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
