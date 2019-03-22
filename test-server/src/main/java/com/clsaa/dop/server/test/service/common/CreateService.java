package com.clsaa.dop.server.test.service.common;

import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public interface CreateService<PARAM> {

    /**
     * @param param 需要创建的数据
     * @return 创建成功的数据
     */
    Optional<PARAM> create(PARAM param);

    List<PARAM> create(List<PARAM> params);
}
