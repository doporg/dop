package com.clsaa.dop.server.test.service;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import org.springframework.data.jpa.repository.JpaRepository;

public class CreateServiceImpl<PO, DTO, ID> implements CreateService<DTO> {

    private ServiceMapper<PO, DTO> serviceMapper;
    private JpaRepository<PO, ID> jpaRepository;

    public CreateServiceImpl(ServiceMapper<PO, DTO> serviceMapper, JpaRepository<PO, ID> repository) {
        this.serviceMapper = serviceMapper;
        this.jpaRepository = repository;
    }

    @Override
    public DTO create(DTO data) {
        PO po = serviceMapper.downgrade(data);
        jpaRepository.save(po);
        return data;
    }

}
