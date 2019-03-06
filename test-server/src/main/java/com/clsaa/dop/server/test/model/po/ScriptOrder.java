package com.clsaa.dop.server.test.model.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class ScriptOrder {

    private Long operationId;

    private String type;

    //todo 脚本执行顺序定义，执行内容编排

    // ----------- common property ---------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    @Column(name = "is_deleted")
    private boolean deleted;
}
