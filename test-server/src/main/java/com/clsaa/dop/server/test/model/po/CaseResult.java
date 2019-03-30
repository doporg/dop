package com.clsaa.dop.server.test.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 05/03/2019
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "case_result", schema = "db_dop_test",
        indexes = {@Index(columnList = "case_id,rtime", unique = false)})
public class CaseResult implements Po{

    public enum Status{
        SUCCESS("成功"),
        FAIL("失败"),
        TOCHECK("待核查"),
        NONUSE("不可用"),
        BLOCKED("阻塞")
        ;

        private String comment;

        Status(String comment) {
            this.comment = comment;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id")
    private Long caseId;

    @Column(name = "ruser_id")
    private Long ruserId;

    private LocalDateTime rtime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String comment;

    // ----------- common property ---------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    @Column(name = "is_deleted")
    private boolean deleted;
}
