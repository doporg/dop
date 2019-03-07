package com.clsaa.dop.server.application.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang.StringUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * 项目实体类
 *
 * @author Bowen
 * @version v2
 * @summary 项目实体类
 * @since 2019-3-1
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project implements Serializable {

    private static final long serialVersionUID = 6906097418517275444L;
    /**
     * ID
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 项目名称
     */
    @Column(nullable = false, name = "title")
    private String title;

    /**
     * 项目创建者
     */
    @Column(nullable = false, name = "cuser")
    private Long cuser;

    /**
     * 项目修改者
     */
    @Column(nullable = false, name = "muser")
    private Long muser;

    /**
     * 创建时间
     */
    @Column(nullable = false, name = "ctime")
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    @Column(nullable = false, name = "mtime")
    private LocalDateTime mtime;

    /**
     * 组织ID
     */
    @Column(nullable = false, name = "organization_id")
    private Long organizationId;

    //public enum PrivateStatus {
    //
    //    /**
    //     * 正常的
    //     */
    //    NORMAL("NORMAL"),
    //    /**
    //     * 结束的
    //     */
    //    FINISHED("FINISHED"),
    //    ;
    //
    //    private String code;
    //
    //    Status(String code) {
    //    }
    //};


    public enum Status {

        /**
         * 正常的
         */
        NORMAL("NORMAL"),
        /**
         * 结束的
         */
        FINISHED("FINISHED"),
        ;

        private String code;

        Status(String code) {
        }
    }

    ;
    /**
     * 是否删除
     */
    @Column(nullable = false, name = "is_deleted")
    private boolean is_deleted;

    /**
     * 项目状态
     */
    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * 项目描述
     */
    @Column(name = "description")
    private String description;

}
