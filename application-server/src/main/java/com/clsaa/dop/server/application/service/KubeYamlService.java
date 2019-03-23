package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.KubeYamlRepository;
import com.clsaa.dop.server.application.model.bo.KubeYamlDataBoV1;
import com.clsaa.dop.server.application.model.po.KubeYamlData;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.models.V1Container;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1DeploymentBuilder;
import io.kubernetes.client.util.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service(value = "AppYamlService")
public class KubeYamlService {
    @Autowired
    KubeYamlRepository kubeYamlRepository;

    @Autowired
    AppEnvService appEnvService;


    /**
     * 创建YAML信息
     *
     * @param appEnvId        应用环境id
     * @param cuser           创建者
     * @param nameSpace       命名空间
     * @param service         服务
     * @param deployment      部署
     * @param container    容器
     * @param releaseStrategy 发布策略
     * @param releaseBatch    发布批次
     * @param replicas        副本数量
     * @param imageUrl       镜像地址
     */
    public void CreateYamlData(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String container, String releaseStrategy, Integer replicas
            , Long releaseBatch, String imageUrl, String yamlFilePath) throws Exception {


        KubeYamlData kubeYamlData = KubeYamlData.builder()
                    .appEnvId(appEnvId)
                    .ctime(LocalDateTime.now())
                    .mtime(LocalDateTime.now())
                    .cuser(cuser)
                    .muser(cuser)
                    .is_deleted(false)
                    .nameSpace(nameSpace)
                    .service(service)
                    .deployment(deployment)
                .containers(container)
                    .replicas(replicas)
                    .releaseBatch(releaseBatch)
                .releaseStrategy(KubeYamlData.ReleaseStrategy.valueOf(releaseStrategy))
                .imageUrl(imageUrl)
                .yamlFilePath(yamlFilePath)
                    .build();

        if (kubeYamlData.getYamlFilePath() != "") {
            AppsV1Api appsApi = appEnvService.getAppsApi(appEnvId);
            List<V1Deployment> deploymentList = appsApi.listNamespacedDeployment(nameSpace, false, null, null, null, "apps=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            if (deploymentList.size() != 0) {
                for (int i = 0; i < deploymentList.size(); i++) {
                    V1Deployment v1Deployment = deploymentList.get(i);
                    if (v1Deployment.getMetadata().getName().equals(deployment)) {
                        List<V1Container> containerList = v1Deployment.getSpec().getTemplate().getSpec().getContainers();
                        for (int j = 0; i < containerList.size(); j++) {
                            if (containerList.get(i).getName().equals(container)) {
                                containerList.get(i).setImage(imageUrl);
                            }
                        }
                        v1Deployment.getSpec().getTemplate().getSpec().setContainers(containerList);

                        //appsApi.createNamespacedDeployment(nameSpace, v1Deployment, false, null, null);
                    }
                    kubeYamlData.setDeploymentEditableYaml(Yaml.dump(v1Deployment));
                }

            } else {
                V1Deployment v1Deployment = new V1DeploymentBuilder()
                        .withNewMetadata()
                        .withName(service)
                        .addToLabels("app", service)
                        .endMetadata()
                        .withNewSpec()
                        .withReplicas(replicas)
                        .withNewSelector()
                        .addToMatchLabels("app", service)
                        .endSelector()
                        .withNewTemplate()
                        .withNewMetadata()
                        .addToLabels("app", service)
                        .endMetadata()
                        .withNewSpec()
                        .addNewContainer()
                        .withName(service)
                        .withImage(imageUrl)
                        .endContainer()
                        .endSpec()
                        .endTemplate()
                        .endSpec()
                        .build();
                kubeYamlData.setDeploymentEditableYaml(Yaml.dump(v1Deployment));

                //appsApi.createNamespacedDeployment(nameSpace, v1Deployment, false, null, null);
            }

        }

        this.kubeYamlRepository.saveAndFlush(kubeYamlData);
    }


    /**
     * 更新YAML信息
     *
     * @param appEnvId        应用环境id
     * @param cuser           创建者
     * @param nameSpace       命名空间
     * @param service         服务
     * @param deployment      部署
     * @param container       容器
     * @param releaseStrategy 发布策略
     * @param releaseBatch    发布批次
     * @param replicas        副本数量
     * @param imageUrl        镜像地址
     */
    public void UpdateYamlData(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String container, String releaseStrategy, Integer replicas
            , Long releaseBatch, String imageUrl, String yamlFilePath) throws Exception {
        KubeYamlData kubeYamlData = this.kubeYamlRepository.findByAppEnvId(appEnvId).orElse(null);
        kubeYamlData.setMtime(LocalDateTime.now());
        kubeYamlData.setMuser(cuser);
        kubeYamlData.setNameSpace(nameSpace);
        kubeYamlData.setService(service);
        kubeYamlData.setDeployment(deployment);
        kubeYamlData.setContainers(container);
        kubeYamlData.setReplicas(replicas);
        kubeYamlData.setReleaseBatch(releaseBatch);
        kubeYamlData.setImageUrl(imageUrl);
        kubeYamlData.setYamlFilePath(yamlFilePath);
        kubeYamlData.setReleaseStrategy(KubeYamlData.ReleaseStrategy.valueOf(releaseStrategy));


        if (kubeYamlData.getYamlFilePath() != "") {
            AppsV1Api appsApi = appEnvService.getAppsApi(appEnvId);
            List<V1Deployment> deploymentList = appsApi.listNamespacedDeployment(nameSpace, false, null, null, null, "apps=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            if (deploymentList.size() != 0) {
                for (int i = 0; i < deploymentList.size(); i++) {
                    V1Deployment v1Deployment = deploymentList.get(i);
                    if (v1Deployment.getMetadata().getName().equals(deployment)) {
                        List<V1Container> containerList = v1Deployment.getSpec().getTemplate().getSpec().getContainers();
                        for (int j = 0; i < containerList.size(); j++) {
                            if (containerList.get(i).getName().equals(container)) {
                                containerList.get(i).setImage(imageUrl);
                            }
                        }
                        v1Deployment.getSpec().getTemplate().getSpec().setContainers(containerList);

                        //appsApi.createNamespacedDeployment(nameSpace, v1Deployment, false, null, null);
                    }
                    kubeYamlData.setDeploymentEditableYaml(Yaml.dump(v1Deployment));
                }

            } else {
                V1Deployment v1Deployment = new V1DeploymentBuilder()
                        .withNewMetadata()
                        .withName(service)
                        .addToLabels("app", service)
                        .endMetadata()
                        .withNewSpec()
                        .withReplicas(replicas)
                        .withNewSelector()
                        .addToMatchLabels("app", service)
                        .endSelector()
                        .withNewTemplate()
                        .withNewMetadata()
                        .addToLabels("app", service)
                        .endMetadata()
                        .withNewSpec()
                        .addNewContainer()
                        .withName(service)
                        .withImage(imageUrl)
                        .endContainer()
                        .endSpec()
                        .endTemplate()
                        .endSpec()
                        .build();
                kubeYamlData.setDeploymentEditableYaml(Yaml.dump(v1Deployment));

                //appsApi.createNamespacedDeployment(nameSpace, v1Deployment, false, null, null);
            }

        }

        this.kubeYamlRepository.saveAndFlush(kubeYamlData);
    
    }

    /**
     * 判断Yaml是否存在
     */
    public Boolean isExistYamlData(Long appEnvId) {
        Long yamlCount = this.kubeYamlRepository.countByAppEnvId(appEnvId);
        return yamlCount.equals(new Long(1));
    }

    public KubeYamlDataBoV1 findYamlDataByEnvId(Long appEnvId) {
        return BeanUtils.convertType(this.kubeYamlRepository.findByAppEnvId(appEnvId).orElse(null), KubeYamlDataBoV1.class);
    }

}
