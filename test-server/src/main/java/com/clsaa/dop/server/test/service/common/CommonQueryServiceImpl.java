package com.clsaa.dop.server.test.service.common;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 12/03/2019
 */
public class CommonQueryServiceImpl<PO,DTO,ID> implements QueryService<DTO, ID> {

    private ServiceMapper<PO, DTO> serviceMapper;
    private JpaRepository<PO, ID> jpaRepository;

    public CommonQueryServiceImpl(ServiceMapper<PO, DTO> serviceMapper, JpaRepository<PO, ID> jpaRepository) {
        this.serviceMapper = serviceMapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<DTO> selectByPk(ID id) {
        Optional<PO> poOptional = jpaRepository.findById(id);
        return poOptional.isPresent() ? serviceMapper.convert(poOptional.get()) : Optional.empty();
    }

    @Override
    public List<DTO> selectByPk(List<ID> id) {
        List<PO> pos = jpaRepository.findAllById(id);
        return serviceMapper.convert(pos);
    }

    @Override
    public List<DTO> select() {
        List<PO> pos = jpaRepository.findAll();
        return serviceMapper.convert(pos);
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }

}
