package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.PermissionConfig;
import com.clsaa.dop.server.application.dao.KubeCredentialRepository;
import com.clsaa.dop.server.application.model.bo.KubeCredentialBoV1;
import com.clsaa.dop.server.application.model.po.KubeCredential;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "KubeCredentialService")
public class KubeCredentialService {
    @Autowired
    KubeCredentialRepository kubeCredentialRepository;
    @Autowired
    private PermissionConfig permissionConfig;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    AppEnvCredentialMappingService appEnvCredentialMappingService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 更新url和token信息
     *
     * @param appEnvId 应用环境id
     * @param url      url
     * @param token    token
     */
    public void updateClusterInfo(Long loginUser, Long appEnvId, String url, String token) {
//        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getEditCluster(), loginUser)
//                , BizCodes.NO_PERMISSION);


        logger.info("[updateClusterInfo] Request coming: loginUser={}, appEnvId={}, url={}, token={}",loginUser,appEnvId,url,token);
        ApiClient client = Config.fromToken(url,
                token,
                false);
        CoreV1Api api = new CoreV1Api(client);
        try {
            api.listNamespace(false, null, null, null, null, Integer.MAX_VALUE, null, null, false);
        } catch (ApiException e) {
            logger.error("[updateClusterInfo] 无法获得命名空间列表！Exception",e);
            BizAssert.justNotFound(BizCodes.ERROR_ACCESS);
        }

        Long credentialId = this.appEnvCredentialMappingService.findCredentialIdByAppEnvId(appEnvId);
        KubeCredential kubeCredential = this.kubeCredentialRepository.findById(credentialId).orElse(null);
        kubeCredential.setMtime(LocalDateTime.now());
        kubeCredential.setMuser(loginUser);
        kubeCredential.setTargetClusterUrl(url);
        kubeCredential.setTargetClusterToken(token);
        this.kubeCredentialRepository.saveAndFlush(kubeCredential);
    }

    public KubeCredentialBoV1 findByAppEnvId(Long loginUser, Long appEnvId) {
//        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewCluster(), loginUser)
//                , BizCodes.NO_PERMISSION);
        logger.info("[findByAppEnvId] Request coming: loginUser={}, appEnvId={}",loginUser,appEnvId);
        Long credentialId = this.appEnvCredentialMappingService.findCredentialIdByAppEnvId(appEnvId);
        KubeCredential credential = this.kubeCredentialRepository.findById(credentialId).orElse(null);
        //credential.setTargetClusterToken(this.DecryptValue(credential.getTargetClusterToken()));
        return BeanUtils.convertType(this.kubeCredentialRepository.findById(credentialId).orElse(null), KubeCredentialBoV1.class);
    }

    public KubeCredentialBoV1 queryByAppEnvId(Long appEnvId) {

        logger.info("[queryByAppEnvId] Request coming: appEnvId={}",appEnvId);
        Long credentialId = this.appEnvCredentialMappingService.findCredentialIdByAppEnvId(appEnvId);
        logger.info("[queryByAppEnvId] Get credentialId: credentialId={}",credentialId);
        KubeCredential credential = this.kubeCredentialRepository.findById(credentialId).orElse(null);
        //credential.setTargetClusterToken(this.DecryptValue(credential.getTargetClusterToken()));
        return BeanUtils.convertType(this.kubeCredentialRepository.findById(credentialId).orElse(null), KubeCredentialBoV1.class);
    }

    //public String EncryptValue(String value) {
    //    return DESUtil.getEncryptString(value);
    //}
    //
    //public String DecryptValue(String value) {
    //    return DESUtil.getDecryptString(value);
    //}

    public void createCredentialByAppEnvId(Long loginUser, Long appEnvId) {
        logger.info("[createCredentialByAppEnvId] Request coming: loginUser={}, appEnvId={}",loginUser,appEnvId);
        KubeCredential kubeCredential = KubeCredential.builder()
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(loginUser)
                .muser(loginUser)
                .is_deleted(false)
                .targetClusterToken("")
                .targetClusterUrl("")
                .build();
        this.kubeCredentialRepository.saveAndFlush(kubeCredential);
        this.appEnvCredentialMappingService.createMapping(loginUser, appEnvId, kubeCredential.getId());
    }
}
