package com.clsaa.dop.server.permission.model.po;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 功能点持久层对象，对应功能点表中每条数据
 *
 * @author lzy
 *

 *
 * since :2019.3.1
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_permission") //引入@Table注解，name赋值为表名
//重写SQL删除语句
@SQLDelete(sql = "update t_permission set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class Permission implements Serializable {
    /*
 * @param id            功能点ID
 * @param parentId     父功能点ID
 * @param name     功能点名称
 * @param isPrivate         是否私有
 * @param description     功能点描述
 * @param ctime     创建时间
 * @param mtime     修改时间
 * @param cuser     创建人
 * @param muser     修改人
 * @param deleted     删除标记
 * */
    private static final long serialVersionUID = 552000264L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Basic
    @Column(name="parent_id")
    private Long parentId;
    @Basic
    @Column(name="name")
    private String name;
    @Basic
    @Column(name="is_private")
    private Integer isPrivate;

    @Basic
    @Column(name="description")
    private String description;

    /* 表里都要有的字段*/
    @Basic
    @Column(name = "ctime")
    private LocalDateTime ctime;
    @Basic
    @Column(name = "mtime")
    private LocalDateTime mtime;
    @Basic
    @Column(name = "cuser")
    private Long cuser;
    @Basic
    @Column(name = "muser")
    private Long muser;
    @Basic
    @Column(name="is_deleted")
    private Boolean deleted;
    /* 表里都要有的字段*/
}
