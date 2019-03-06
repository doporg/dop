package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.HttpMethod;
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
@Table(name = "interface_url_info", schema = "db_dop_test")
public class InterfaceUrlInfo {

    // ----------- main property ---------
    @Column(name = "raw_url")
    private String rawUrl;

    @Enumerated(value = EnumType.STRING)
    private HttpMethod httpMethod;

    @Column(name = "request_body")
    private String requestBody;

    // check point concerned
    @Column(name = "retry_times")
    private int retryTimes;

    @Column(name = "retry_interval")
    private Long retryInterval;

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
