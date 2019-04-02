package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.KubeCredentialRepository;
import com.clsaa.dop.server.application.model.bo.KubeCredentialBoV1;
import com.clsaa.dop.server.application.model.po.KubeCredential;
import com.clsaa.dop.server.application.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service(value = "KubeCredentialService")
public class KubeCredentialService {
    @Autowired
    KubeCredentialRepository kubeCredentialRepository;

    @Autowired
    AppEnvCredentialMappingService appEnvCredentialMappingService;

    /**
     * 更新url和token信息
     *
     * @param appEnvId 应用环境id
     * @param url      url
     * @param token    token
     */
    public void updateClusterInfo(Long muser, Long appEnvId, String url, String token) {
        Long credentialId = this.appEnvCredentialMappingService.findCredentialIdByAppEnvId(appEnvId);
        KubeCredential kubeCredential = this.kubeCredentialRepository.findById(credentialId).orElse(null);
        kubeCredential.setMtime(LocalDateTime.now());
        kubeCredential.setMuser(muser);
        kubeCredential.setTargetClusterUrl(url);
        kubeCredential.setTargetClusterToken(token);
        this.kubeCredentialRepository.saveAndFlush(kubeCredential);
    }

    public KubeCredentialBoV1 findByAppEnvId(Long appEnvId) {
        Long credentialId = this.appEnvCredentialMappingService.findCredentialIdByAppEnvId(appEnvId);
        KubeCredential credential = this.kubeCredentialRepository.findById(credentialId).orElse(null);
        return BeanUtils.convertType(this.kubeCredentialRepository.findById(credentialId).orElse(null), KubeCredentialBoV1.class);
    }



    public void createCredentialByAppEnvId(Long cuser, Long appEnvId) {
        KubeCredential kubeCredential = KubeCredential.builder()
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(cuser)
                .muser(cuser)
                .is_deleted(false)
                .targetClusterToken("")
                .targetClusterUrl("")
                .build();
        this.kubeCredentialRepository.saveAndFlush(kubeCredential);
        this.appEnvCredentialMappingService.createMapping(cuser, appEnvId, kubeCredential.getId());
    }
}
