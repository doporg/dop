package com.clsaa.dop.server.test.service;

import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public interface CreateService<DTO> {

    /**
     * @param data 需要创建的数据
     * @return 创建成功的数据
     */
    Optional<DTO> create(DTO data);
}
