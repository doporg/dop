package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.util.BeanUtils;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public abstract class AbstractCommonServiceMapper<PO, DTO> implements ServiceMapper<PO, DTO> {

    abstract Class<PO> getPOClass();

    abstract Class<DTO> getDTOClass();

    public Optional<PO> downgrade(DTO dto){
        return Optional.of(dto)
                .map(d -> BeanUtils.convertType(d, getPOClass()));
    }

    public List<PO> downgrade(Collection<DTO> dtos){
        return isEmpty(dtos) ?
                Lists.newArrayList() :
                dtos.stream()
                        .map(this::downgrade)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }

    public Optional<DTO> upgrade(PO po){
        return Optional.of(po).map(p -> BeanUtils.convertType(p, getDTOClass()));
    }

    public List<DTO> upgrade(Collection<PO> pos){
        return isEmpty(pos) ?
                Lists.newArrayList() :
                pos.stream()
                        .map(this::upgrade)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }
}
