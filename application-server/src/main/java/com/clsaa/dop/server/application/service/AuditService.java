package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.AuditFeign;
import com.clsaa.dop.server.application.model.vo.LogDtoV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "AuditService")
public class AuditService {
    @Autowired
    private AuditFeign auditFeign;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void addAuditFeign(String serviceId, String functionId, String userId, Long otime, Map<String, Object> values) {
        logger.info("[addAuditFeign] Request coming: serviceId={}, functionId={}, otime={}, values",serviceId,functionId,otime);
        LogDtoV1 logDtoV1 = new LogDtoV1();
        logDtoV1.setServiceId(serviceId);
        logDtoV1.setFunctionId(functionId);
        logDtoV1.setUserId(userId);
        logDtoV1.setOtime(otime);
        logDtoV1.setValues(values);
        this.auditFeign.addAuditLog(logDtoV1);
    }


}
