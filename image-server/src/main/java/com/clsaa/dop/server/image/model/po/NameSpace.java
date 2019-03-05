package com.clsaa.dop.server.image.model.po;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 命名空间持久层对象
 * </p>
 * @author xzt
 * @since 2019-3-5
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "t_namespace", schema = "db_dop_image_server",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email","name"} )},
        indexes = {@Index(columnList ="name")})
public class NameSpace implements Serializable{
    private static final long serialVersionUID = 6906097418517275871L;
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
     * 创建人的邮箱地址
     */
    @Basic
    @Column(name = "email")
    private String email;
    /**
     * 命名空间创建时间
     */
    @Basic
    @Column(name="ctime")
    private LocalDateTime ctime;
    /**
     * 命名空间修改时间
     */
    @Basic
    @Column(name = "mtime")
    private LocalDateTime mtime;
}
