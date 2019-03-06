package com.clsaa.dop.server.test.service;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class InterfaceCaseCreateService extends CreateServiceImpl<InterfaceCase, InterfaceCaseDto, Long> {

    @Autowired
    public InterfaceCaseCreateService(ServiceMapper<InterfaceCase, InterfaceCaseDto> serviceMapper, JpaRepository<InterfaceCase, Long> repository) {
        super(serviceMapper, repository);
    }
}
