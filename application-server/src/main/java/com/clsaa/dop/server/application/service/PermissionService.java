package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.PermissionFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "PermissionService")
public class PermissionService {
    @Autowired
    private PermissionFeign permissionFeign;

    public boolean checkPermission(String permissionName, Long userId) {

        return this.permissionFeign.permissionCheck(permissionName, userId);

    }
}
