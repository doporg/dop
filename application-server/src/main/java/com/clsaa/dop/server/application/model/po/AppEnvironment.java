package com.clsaa.dop.server.application.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

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
public class AppEnvironment {

    @Id
    @GeneratedValue
    private Long id;
    //
    //@OneToOne(cascade = CascadeType.ALL,mappedBy = "AppBasicUrl")
    //private App app;

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
        }
    }

    ;


    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private AppEnvironment.ProductMode productMode;
}
