package com.clsaa.dop.server.test.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 04/03/2019
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "manual_case", schema = "db_dop_test",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"case_name", "application_id"})},
        indexes = {@Index(columnList = "case_name,application_id", unique = true),
                @Index(columnList = "application_id,case_name", unique = false)})
public class ManualCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_name")
    private String caseName;

    @Column(name = "case_desc")
    private String caseDesc;

    @Column(name = "pre_condition")
    private String preCondition;

    @Column(name = "application_id")
    private Long applicationId;

    //todo 图片上传支持
    @Column(name = "comment")
    private String comment;

    @Enumerated(EnumType.STRING)
    private CaseStatus status;

    // ------ common property ------------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    @Column(name = "is_deleted")
    private boolean deleted;
}
