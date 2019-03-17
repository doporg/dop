package com.clsaa.dop.server.user.service;

import com.clsaa.dop.server.user.dao.OrgUserMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 组织用户关联，服务实现类
 *
 * @author joyren
 */
@Service
public class OrgUserMappingService {
    @Autowired
    private OrgUserMappingRepository orgUserMappingRepository;
}
