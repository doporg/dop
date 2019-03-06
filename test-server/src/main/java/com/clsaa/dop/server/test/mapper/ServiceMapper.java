package com.clsaa.dop.server.test.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public interface ServiceMapper<PO, DTO> {

    Optional<PO> downgrade(DTO dto);

    List<PO> downgrade(Collection<DTO> dtos);

    Optional<DTO> upgrade(PO po);

    List<DTO> upgrade(Collection<PO> pos);
}
