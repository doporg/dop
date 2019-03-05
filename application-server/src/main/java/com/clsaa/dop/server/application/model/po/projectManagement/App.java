package com.clsaa.dop.server.application.model.po.projectManagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * 应用实体类
 *
 * @author ZhengBowen
 * @version v1
 * @summary 应用实体类
 * @since 2019-3-1
 */
@Data
@Entity
@Builder
@AllArgsConstructor
public class App {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String cuser;

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    private Project project;


}
