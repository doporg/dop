package com.clsaa.dop.server.application.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 应用环境信息实体类
 *
 * @author ZhengBowen
 * @version v1
 * @summary 应用环境信息实体类（还未使用）
 * @since 2019-3-7
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@SQLDelete(sql = "update app_environment set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class AppEnvironment {

    @Id
    @GeneratedValue
    private Long id;

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

    @Column(nullable = false, name = "app_id")
    private Long appId;

    public enum ProductMode {

        /**
         * 分支模式
         */
        BRANCH("BRANCH"),
        /**
         * 自由模式
         */
        FREE("FREE"),
        ;

        private String code;

        ProductMode(String code) {
            this.code = code;
        }
    }

    ;


    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private AppEnvironment.ProductMode productMode;
}
