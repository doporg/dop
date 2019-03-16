package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppEnvRepository;
import com.clsaa.dop.server.application.dao.AppVarRepository;
import com.clsaa.dop.server.application.model.bo.AppEnvBoV1;
import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppEnvironment;
import com.clsaa.dop.server.application.model.vo.AppEnvDetailV1;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import org.apache.commons.configuration.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "AppEnvService")
public class AppEnvService {


    @Autowired
    AppEnvRepository appEnvRepository;


    /**
     * 根据appId查询环境信息
     *
     * @param appID appID
     * @return{@link List<AppEnvBoV1> }
     */
    public List<AppEnvBoV1> findEnvironmentByAppId(Long appID) {
        return this.appEnvRepository.findAllByAppId(appID).stream().map(l -> BeanUtils.convertType(l, AppEnvBoV1.class)).collect(Collectors.toList());
    }

    /**
     * 创建环境信息
     *
     * @param appEnvironment appEnvironment
     * @return
     */
    public void createAppEnv(AppEnvironment appEnvironment) {
        this.appEnvRepository.saveAndFlush(appEnvironment);
    }


    /**
     * 根据ID查询环境详情信息
     *
     * @param id
     * @return AppEnvBoV1
     */
    public AppEnvBoV1 findEnvironmentDetailById(Long id) {
        return BeanUtils.convertType(this.appEnvRepository.findById(id), AppEnvBoV1.class);
    }

    /**
     * 创建环境
     *
     * @param appId              appId
     * @param cuser              创建者
     * @param title              名称
     * @param environmentLever   环境级别
     * @param deploymentStrategy 发布策略
     */
    public void createEnvironmentByAppId(Long appId, Long cuser, String title, String environmentLever, String deploymentStrategy) {
        AppEnvironment appEnvironment = AppEnvironment.builder()
                .appId(appId)
                .title(title)
                .cuser(cuser)
                .muser(cuser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .environmentLevel(AppEnvironment.EnvironmentLevel.valueOf(environmentLever))
                .deploymentStrategy(AppEnvironment.DeploymentStrategy.valueOf(deploymentStrategy))
                .build();
        this.appEnvRepository.saveAndFlush(appEnvironment);
    }

    /**
     * 根据ID删除环境信息
     *
     * @param id appId
     */
    public void deleteEnvironmentById(Long id) {
        this.appEnvRepository.deleteById(id);
    }

    /**
     * 更新环境信息
     *
     * @param id                 id
     * @param muser              修改者
     * @param targetCluster      目标集群
     * @param deploymentStrategy 部署策略
     * @param nameSpace          命名空偶就
     * @param service            服务
     * @param releaseBatch       发布批次
     */
    public void updateEnvironmentDetailById(Long id, Long muser, String targetCluster, String deploymentStrategy,
                                            String nameSpace, String service, Long releaseBatch) {
        AppEnvironment appEnvironment = this.appEnvRepository.findById(id).orElse(null);
        appEnvironment.setMtime(LocalDateTime.now());
        appEnvironment.setMuser(muser);
        appEnvironment.setTargetCluster(targetCluster);
        appEnvironment.setDeploymentStrategy(AppEnvironment.DeploymentStrategy.valueOf(deploymentStrategy));
        appEnvironment.setNameSpace(nameSpace);
        appEnvironment.setService(service);
        if (releaseBatch != 0) {
            appEnvironment.setReleaseBatch(releaseBatch);
        }
        this.appEnvRepository.saveAndFlush(appEnvironment);

    }


}
