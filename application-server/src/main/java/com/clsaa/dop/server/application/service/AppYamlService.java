package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppYamlRepository;
import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppEnvironment;
import com.clsaa.dop.server.application.model.po.AppYamlData;
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
     * @param image_url       镜像地址
     */
    public void updataOrCreateYamlData(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String containers, String releaseStrategy, Integer replicas
            , Long releaseBatch, String image_url) {
        AppYamlData appYamlData = this.appYamlRepository.findByAppEnvId(appEnvId).orElse(null);

        if (appYamlData == null) {
            appYamlData = AppYamlData.builder()
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
                    .image_url(image_url)
                    .build();
        } else {
            appYamlData.setMtime(LocalDateTime.now());
            appYamlData.setMuser(cuser);
            appYamlData.setNameSpace(nameSpace);
            appYamlData.setService(service);
            appYamlData.setDeployment(deployment);
            appYamlData.setContainers(containers);
            appYamlData.setReplicas(replicas);
            appYamlData.setReleaseBatch(releaseBatch);
            appYamlData.setReleaseStrategy(AppYamlData.ReleaseStrategy.valueOf(releaseStrategy));
        }
        this.appYamlRepository.saveAndFlush(appYamlData);
    }

}
