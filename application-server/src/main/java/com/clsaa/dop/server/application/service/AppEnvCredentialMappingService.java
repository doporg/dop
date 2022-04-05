package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppEnvCredentialMappingRepository;
import com.clsaa.dop.server.application.model.po.AppEnvCredentialMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "AppEnvCredentialMappingService")
public class AppEnvCredentialMappingService {
    @Autowired
    AppEnvCredentialMappingRepository appEnvCredentialMappingRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void createMapping(Long cuser, Long appEnvId, Long credentialId) {
        logger.info("[createMapping] Request coming: cuser={}, appEnvId={}, credentialId={}",cuser,appEnvId,credentialId);
        AppEnvCredentialMapping appEnvCredentialMapping = AppEnvCredentialMapping.builder()
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(cuser)
                .muser(cuser)
                .is_deleted(false)
                .appEnvId(appEnvId)
                .credentialId(credentialId)
                .build();
        this.appEnvCredentialMappingRepository.saveAndFlush(appEnvCredentialMapping);
    }


    public Long findCredentialIdByAppEnvId(Long appEnvId) {
        logger.info("[findCredentialIdByAppEnvId] Request coming: appEnvId={}",appEnvId);
        return this.appEnvCredentialMappingRepository.findByAppEnvId(appEnvId).orElse(null).getCredentialId();

    }
}
