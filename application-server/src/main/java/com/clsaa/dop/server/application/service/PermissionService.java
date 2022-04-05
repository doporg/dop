package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.PermissionFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "PermissionService")
public class PermissionService {
    @Autowired
    private PermissionFeign permissionFeign;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean checkPermission(String permissionName, Long userId) {

        logger.info("[checkPermission] Request coming: permissionName={}, userId={}",permissionName,userId);
        return this.permissionFeign.permissionCheck(permissionName, userId);

    }

    public boolean check(String permissionName, Long userId, String fieldName, Long fieldValue) {
        logger.info("[check] Request coming: permissionName={}, userId={}, fieldName={}, fieldValue={}",permissionName,userId,fieldName,fieldValue);
        return this.permissionFeign.check(permissionName, userId, fieldName, fieldValue);

    }

    public List<Long> getProjectMembers(String fieldName, Long fieldValue) {
        logger.info("[getProjectMembers] Request coming: fieldName={}, fieldValue={}",fieldName,fieldValue);
        return this.permissionFeign.findUserByField(fieldName, fieldValue);
    }

    //void addRoleToUser(Long userId,Long roleId,Long loginUser){
    //   this.permissionFeign.addRoleToUser(userId,roleId,loginUser);
    //}

    public void addData(Long ruleId, Long userId, Long fieldValue, Long loginUser) {
        logger.info("[addData] Request coming: ruleId={}, userId={}, fieldValue={}, loginUser",ruleId,userId,fieldValue,loginUser);
        this.permissionFeign.addData(ruleId, userId, fieldValue, loginUser);
    }

    public List<Long> findAllIds(String permissionName, Long userId, String fieldName) {
        logger.info("[findAllIds] Request coming: permissionName={}, userId={}, fieldName={}",permissionName,userId,fieldName);
        return this.permissionFeign.findAllIds(permissionName, userId, fieldName);
    }

    public void deleteByFieldAndUserId(Long fieldValue, String fieldName, Long userId) {
        logger.info("[deleteByFieldAndUserId] Request coming: fieldValue={}, fieldName={}, userId={}",fieldValue,fieldName,userId);
        this.permissionFeign.deleteByFieldAndUserId(fieldValue, fieldName, userId);

    }
}
