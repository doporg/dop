package com.clsaa.dop.server.user.model.po;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织持久层对象，用户组织是组织架构中的顶层结构，组织包含多个组{@link Group}
 *
 * @author joyren
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_organization", schema = "db_dop_user")
public class Organization implements Serializable {
    private static final long serialVersionUID = 6906097411517275871L;
    /**
     * 组织id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 组织名
     */
    @Basic
    @Column(name = "name")
    private String name;
    /**
     * 组织描述
     */
    @Basic
    @Column(name = "description")
    private String description;
    /**
     * 创建人
     */
    @Basic
    @Column(name = "cuser")
    private Long cuser;
    /**
     * 修改人
     */
    @Basic
    @Column(name = "muser")
    private Long muser;
    /**
     * 所属人(组织拥有者)
     */
    @Basic
    @Column(name = "ouser")
    private Long ouser;
    /**
     * 创建时间
     */
    @Basic
    @Column(name = "ctime")
    private LocalDateTime ctime;
    /**
     * 修改时间
     */
    @Basic
    @Column(name = "mtime")
    private LocalDateTime mtime;
    /**
     * 是否删除
     */
    @Basic
    @Column(name = "deleted")
    private Boolean deleted;
}
