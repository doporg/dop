package com.clsaa.dop.server.test.service;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.model.po.ManualCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class ManualCaseCreateService extends CreateServiceImpl<ManualCase, ManualCaseDto, Long> {

    @Autowired
    public ManualCaseCreateService(ServiceMapper<ManualCase, ManualCaseDto> serviceMapper, JpaRepository<ManualCase, Long> repository) {
        super(serviceMapper, repository);
    }

}
