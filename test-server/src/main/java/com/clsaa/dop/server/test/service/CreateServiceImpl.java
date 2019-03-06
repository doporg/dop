package com.clsaa.dop.server.test.service;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public class CreateServiceImpl<PO, DTO, ID> implements CreateService<DTO> {

    private ServiceMapper<PO, DTO> serviceMapper;
    private JpaRepository<PO, ID> jpaRepository;

    public CreateServiceImpl(ServiceMapper<PO, DTO> serviceMapper, JpaRepository<PO, ID> repository) {
        this.serviceMapper = serviceMapper;
        this.jpaRepository = repository;
    }

    @Override
    public Optional<DTO> create(DTO data) {
        Optional<PO> poCreated = serviceMapper.downgrade(data).map(po -> jpaRepository.save(po));
        return serviceMapper.upgrade(poCreated.orElse(null));
    }

}
