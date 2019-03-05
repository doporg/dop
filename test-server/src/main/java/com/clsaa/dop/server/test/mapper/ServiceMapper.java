package com.clsaa.dop.server.test.mapper;

public interface ServiceMapper<PO, DTO> {

    PO downgrade(DTO dto);

    DTO upgrade(PO po);
}
