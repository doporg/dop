package com.clsaa.dop.server.test.service;

import com.clsaa.dop.server.test.model.param.update.UpdatedInterfaceCase;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.service.common.CommonUpdateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 16/04/2019
 */
@Component
public class InterfaceCaseUpdateServiceImpl extends CommonUpdateServiceImpl<Long, UpdatedInterfaceCase, InterfaceCase>{

    @Autowired
    public InterfaceCaseUpdateServiceImpl(JpaRepository<InterfaceCase, Long> repository) {
        super(repository);
    }

    @Override
    protected InterfaceCase doUpdate(InterfaceCase old, UpdatedInterfaceCase param) {
        return old;
    }
}
