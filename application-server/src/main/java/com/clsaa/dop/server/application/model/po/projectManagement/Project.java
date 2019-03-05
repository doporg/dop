package com.clsaa.dop.server.application.model.po.projectManagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


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
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;
    //本来是 private String projectName

    @Column(nullable = false, name = "cuser")
    private Long cuser;

    @Column(nullable = false, name = "muser")
    private Long muser;

    @Column(nullable = false, name = "ctime")
    private LocalDateTime ctime;

    @Column(nullable = false, name = "mtime")
    private LocalDateTime mtime;

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


    //@Column(nullable = false,name="finished")
    //private boolean finished;

    @Column(nullable = false, name = "deleted")
    private boolean deleted;

    /**
     * 项目状态
     */

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "description")
    private String description;
}
