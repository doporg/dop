package com.clsaa.dop.user.server.model.po;

import com.clsaa.dop.user.server.enums.UserType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 用户持久层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user", schema = "db_dop_user_server")
public class User implements Serializable {
    private static final long serialVersionUID = 6906097418517275871L;
    /**
     * 用户id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 用户姓名
     */
    @Basic
    @Column(name = "name")
    private String name;
    /**
     * 用户email地址
     */
    @Basic
    @Column(name = "email")
    private String email;
    /**
     * 创建时间
     */
    @Basic
    @Column(name = "ctime")
    private Timestamp ctime;
    /**
     * 修改时间
     */
    @Basic
    @Column(name = "mtime")
    private Timestamp mtime;
    /**
     * 用户类型
     */
    @Basic
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;
}