package com.clsaa.dop.server.image.model.po;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *     镜像基本信息的持久层对象
 * </p>
 * @author xzt
 * @since 2019-3-7
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_image_info",schema = "db_dop_image_server")
public class ImageInfoPO implements Serializable {
    private static final long serialVersionUID = 6906097418517275871L;
    /**
     * 镜像状态
     * @author xzt
     */
    public enum Status{
        /**
         * 镜像属于正常状态
         */
        NORMAL("NORMAL"),
        /**
         *  镜像异常
         */
        ABNORMAL("ABNORMAL");
        private String code;
        private Status(String code){ }

    }
    /**
     * 镜像id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 镜像的tag（类似于版本号）
     */
    @Basic
    @Column(name="tag")
    private String tag;

    /**
     * 镜像的大小
     */
    @Basic
    @Column(name = "size")
    private String size;
    /**
     * 镜像的状态
     */
    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * 镜像的创建时间
     */
    @Basic
    @Column(name = "ctime")
    private LocalDateTime ctime;

    /**
     * 镜像的最后修改时间
     */
    @Basic
    @Column(name = "mtime")
    private LocalDateTime mtime;


}
