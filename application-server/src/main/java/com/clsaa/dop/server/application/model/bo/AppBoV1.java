package com.clsaa.dop.server.application.model.bo;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * 项目业务层对象
 *
 * @author Bowen
 * @since 2019-3-7
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppBoV1 {
    /**
     * 项目id
     */
    private Long id;
    /**
     * 拥有者
     */
    private Long ouser;
    /**
     * 名称
     */
    private String title;

    /**
     * 创建日期
     */
    private LocalDateTime ctime;
    /**
     * 描述
     */
    private String description;

}
