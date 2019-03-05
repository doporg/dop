package com.clsaa.dop.server.test.service;

public interface CreateService<DTO> {

    /**
     * @param data 需要创建的数据
     * @return 创建成功的数据
     */
    DTO create(DTO data);
}
