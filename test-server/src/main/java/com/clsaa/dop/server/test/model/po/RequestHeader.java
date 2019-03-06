package com.clsaa.dop.server.test.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "interface_request_header", schema = "db_dop_test",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"url_id", "name"})},
        indexes = {@Index(columnList = "url_id,name", unique = true)})
public class RequestHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----------- main property ---------

    private String name;

    private String value;

    @Column(name = "url_id")
    private Long urlId;

    // ----------- common property ---------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    @Column(name = "is_deleted")
    private boolean deleted;
}
