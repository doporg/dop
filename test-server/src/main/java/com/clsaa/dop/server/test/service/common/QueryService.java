package com.clsaa.dop.server.test.service.common;

import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 12/03/2019
 */
public interface QueryService<DTO, ID> {

    /**
     * 根据主键查询
     * @param id 主键
     * @return 查询结果,无结果时返回Optional#empty()
     */
    Optional<DTO> selectByPk(ID id);

    /**
     * 根据多个主键查询
     * @param id 主键集合
     * @return 查询结果,如果无结果返回空集合,而不是返回{@code null}
     */
    List<DTO> selectByPk(List<ID> id);

    /**
     * 查询所有结果
     * @return 所有结果,如果无结果则返回空集合,而不是返回{@code null}
     */
    List<DTO> select();

    /**
     * 查询结果总数
     * @return 结果总数
     */
    Long count();
}
