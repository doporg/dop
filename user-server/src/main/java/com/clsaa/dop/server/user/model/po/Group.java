package com.clsaa.dop.server.user.model.po;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组持久层对象，组属于某个组织 {@link Organization}，组相互之间有上下级关系可形成树状结构
 *
 * @author joyren
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_organization", schema = "db_dop_user",
        indexes = {@Index(columnList = "path"), @Index(columnList = "parent_id")})
public class Group implements Serializable {
    private static final long serialVersionUID = 6106097411517275871L;
    /**
     * 组id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 20)
    private Long id;
    /**
     * 组织id
     */
    @Basic
    @Column(name = "organization_id")
    private Long organizationId;
    /**
     * 组名
     */
    @Basic
    @Column(name = "name", nullable = false, length = 25)
    private String name;
    /**
     * 组描述
     */
    @Basic
    @Column(name = "description", nullable = false, length = 180)
    private String description;
    /**
     * 组织架构树路径，如0.1.4.8。其中，0是顶级结构，第一个*是顶级结构下的结构，第二个*是顶级结构下的结构下的结构。
     */
    @Basic
    @Column(name = "path", nullable = false, length = 80)
    private String path;
    /**
     * 父组id
     */
    @Basic
    @Column(name = "parent_id", nullable = false, length = 20)
    private Long parentId;
    /**
     * 排序，由小到大
     */
    @Basic
    @Column(name = "seq", nullable = false, length = 20)
    private Long seq;
    /**
     * 创建人
     */
    @Basic
    @Column(name = "cuser", nullable = false, length = 20)
    private Long cuser;
    /**
     * 修改人
     */
    @Basic
    @Column(name = "muser", nullable = false, length = 20)
    private Long muser;
    /**
     * 创建时间
     */
    @Basic
    @Column(name = "ctime", nullable = false)
    private LocalDateTime ctime;
    /**
     * 修改时间
     */
    @Basic
    @Column(name = "mtime", nullable = false)
    private LocalDateTime mtime;
}
