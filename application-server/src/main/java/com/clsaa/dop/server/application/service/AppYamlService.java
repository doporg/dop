package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppYamlRepository;
import com.clsaa.dop.server.application.model.bo.AppYamlDataBoV1;
import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppEnvironment;
import com.clsaa.dop.server.application.model.po.AppYamlData;
import com.clsaa.dop.server.application.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service(value = "AppYamlService")
public class AppYamlService {
    @Autowired
    AppYamlRepository appYamlRepository;


    /**
     * 创建YAML信息
     *
     * @param appEnvId        应用环境id
     * @param cuser           创建者
     * @param nameSpace       命名空间
     * @param service         服务
     * @param deployment      部署
     * @param containers      容器
     * @param releaseStrategy 发布策略
     * @param releaseBatch    发布批次
     * @param replicas        副本数量
     * @param imageUrl       镜像地址
     */
    public void CreateYamlData(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String containers, String releaseStrategy, Integer replicas
            , Long releaseBatch, String imageUrl, String yamlFilePath) {


        AppYamlData appYamlData = AppYamlData.builder()
                    .appEnvId(appEnvId)
                    .ctime(LocalDateTime.now())
                    .mtime(LocalDateTime.now())
                    .cuser(cuser)
                    .muser(cuser)
                    .is_deleted(false)
                    .nameSpace(nameSpace)
                    .service(service)
                    .deployment(deployment)
                    .containers(containers)
                    .replicas(replicas)
                    .releaseBatch(releaseBatch)
                    .releaseStrategy(AppYamlData.ReleaseStrategy.valueOf(releaseStrategy))
                .imageUrl(imageUrl)
                .yamlFilePath(yamlFilePath)
                    .build();

        this.appYamlRepository.saveAndFlush(appYamlData);
    }

    /**
     * 更新YAML信息
     *
     * @param appEnvId        应用环境id
     * @param cuser           创建者
     * @param nameSpace       命名空间
     * @param service         服务
     * @param deployment      部署
     * @param containers      容器
     * @param releaseStrategy 发布策略
     * @param releaseBatch    发布批次
     * @param replicas        副本数量
     * @param imageUrl        镜像地址
     */
    public void UpdateYamlData(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String containers, String releaseStrategy, Integer replicas
            , Long releaseBatch, String imageUrl, String yamlFilePath) {
        AppYamlData appYamlData = this.appYamlRepository.findByAppEnvId(appEnvId).orElse(null);
        appYamlData.setMtime(LocalDateTime.now());
        appYamlData.setMuser(cuser);
        appYamlData.setNameSpace(nameSpace);
        appYamlData.setService(service);
        appYamlData.setDeployment(deployment);
        appYamlData.setContainers(containers);
        appYamlData.setReplicas(replicas);
        appYamlData.setReleaseBatch(releaseBatch);
        appYamlData.setImageUrl(imageUrl);
        appYamlData.setYamlFilePath(yamlFilePath);
        appYamlData.setReleaseStrategy(AppYamlData.ReleaseStrategy.valueOf(releaseStrategy));
    }

    /**
     * 判断Yaml是否存在
     */
    public Boolean isExistYamlData(Long appEnvId) {
        Long yamlCount = this.appYamlRepository.countByAppEnvId(appEnvId);
        return yamlCount == 1;
    }

    public AppYamlDataBoV1 findYamlDataByEnvId(Long appEnvId) {
        return BeanUtils.convertType(this.appYamlRepository.findByAppEnvId(appEnvId).orElse(null), AppYamlDataBoV1.class);
    }

}
