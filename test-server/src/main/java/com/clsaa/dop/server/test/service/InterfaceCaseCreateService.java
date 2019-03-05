package com.clsaa.dop.server.test.service;

import com.clsaa.dop.server.test.mapper.InterfaceCaseMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class InterfaceCaseCreateService extends CreateServiceImpl<InterfaceCase, InterfaceCaseDto, Long> {

    @Autowired
    public InterfaceCaseCreateService(JpaRepository<InterfaceCase, Long> repository) {
        super(InterfaceCaseMapper.MAPPER, repository);
    }
}
