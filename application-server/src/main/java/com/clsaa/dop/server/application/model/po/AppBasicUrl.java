package com.clsaa.dop.server.application.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 应用变量实体类（还未开始使用）
 *
 * @author ZhengBowen
 * @version v1
 * @summary 应用变量实体类
 * @since 2019-3-7
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@SQLDelete(sql = "update app_basic_url set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class AppBasicUrl {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "app_id")
    private Long appId;

    @Column(nullable = false, name = "cuser")
    private Long cuser;

    @Column(nullable = false, name = "muser")
    private Long muser;

    @Column(nullable = false, name = "ctime")
    private LocalDateTime ctime;

    @Column(nullable = false, name = "mtime")
    private LocalDateTime mtime;

    /**
     * 是否删除
     */
    @Column(nullable = false, name = "is_deleted")
    private boolean is_deleted;

    @Column(nullable = false, name = "warehouse_url")
    private String warehouseUrl;

    @Column(nullable = true, name = "production_db_url")
    private String productionDbUrl;

    @Column(nullable = true, name = "test_db_url")
    private String testDbUrl;

    @Column(nullable = true, name = "production_domain")
    private String productionDomain;

    @Column(nullable = true, name = "test_domain")
    private String testDomain;
    //@OneToOne(cascade = CascadeType.ALL,mappedBy = "AppBasicUrl")
    //private App app;

}
