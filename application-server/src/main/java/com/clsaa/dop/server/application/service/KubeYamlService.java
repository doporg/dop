package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.KubeYamlRepository;
import com.clsaa.dop.server.application.model.bo.KubeCredentialBoV1;
import com.clsaa.dop.server.application.model.bo.KubeYamlDataBoV1;
import com.clsaa.dop.server.application.model.po.KubeYamlData;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreApi;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "AppYamlService")
public class KubeYamlService {
    @Autowired
    KubeYamlRepository kubeYamlRepository;


    @Autowired
    KubeCredentialService kubeCredentialService;


    public HashMap<String, String> createYamlFileForDeploy(Long appEnvId) {

        KubeYamlDataBoV1 kubeYamlDataBoV1 = this.findYamlDataByEnvId(appEnvId);
        if (kubeYamlDataBoV1.getYamlFilePath().equals("")) {
            return new HashMap<String, String>() {{
                put("yaml", kubeYamlDataBoV1.getDeploymentEditableYaml());
            }};

        } else {
            return new HashMap<String, String>() {{
                put("path", kubeYamlDataBoV1.getYamlFilePath());
            }};
        }

    }


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
                .build();

        if (yamlFilePath.equals("")) {
            AppsV1Api appsApi = getAppsApi(appEnvId);
            List<V1Deployment> deploymentList = appsApi.listNamespacedDeployment(nameSpace, false, null, null, null, "apps=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            if (deploymentList.size() != 0) {
                for (int i = 0; i < deploymentList.size(); i++) {
                    V1Deployment v1Deployment = deploymentList.get(i);
                    if (v1Deployment.getMetadata().getName().equals(deployment)) {
                        List<V1Container> containerList = v1Deployment.getSpec().getTemplate().getSpec().getContainers();
                        for (int j = 0; i < containerList.size(); j++) {
                            if (containerList.get(i).getName().equals(container)) {
                                containerList.get(i).setImage(imageUrl);
                                break;
                            }
                        }
                        v1Deployment.getSpec().getTemplate().getSpec().setContainers(containerList);
                        kubeYamlData.setDeploymentEditableYaml(Yaml.dump(v1Deployment));
                        kubeYamlData.setDeploymentEditableYaml(Yaml.dump(v1Deployment));
                        break;
                        //appsApi.createNamespacedDeployment(nameSpace, v1Deployment, false, null, null);
                    }
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
            kubeYamlData.setYamlFilePath("");

        } else {
            kubeYamlData.setYamlFilePath(yamlFilePath);
            kubeYamlData.setDeploymentEditableYaml("");
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
    public void updateYamlData(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String container, String releaseStrategy, Integer replicas
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

        kubeYamlData.setReleaseStrategy(KubeYamlData.ReleaseStrategy.valueOf(releaseStrategy));


        if (yamlFilePath.equals("")) {
            AppsV1Api appsApi = getAppsApi(appEnvId);
            List<V1Deployment> deploymentList = appsApi.listNamespacedDeployment(nameSpace, false, null, null, null, "apps=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            if (deploymentList.size() != 0) {
                for (int i = 0; i < deploymentList.size(); i++) {
                    V1Deployment v1Deployment = deploymentList.get(i);
                    if (v1Deployment.getMetadata().getName().equals(deployment)) {
                        List<V1Container> containerList = v1Deployment.getSpec().getTemplate().getSpec().getContainers();
                        for (int j = 0; i < containerList.size(); j++) {
                            if (containerList.get(i).getName().equals(container)) {
                                containerList.get(i).setImage(imageUrl);
                                break;
                            }
                        }
                        v1Deployment.getSpec().getTemplate().getSpec().setContainers(containerList);
                        kubeYamlData.setDeploymentEditableYaml(Yaml.dump(v1Deployment));
                        kubeYamlData.setDeploymentEditableYaml(Yaml.dump(v1Deployment));
                        break;
                    }
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
            kubeYamlData.setYamlFilePath("");

        } else {
            kubeYamlData.setYamlFilePath(yamlFilePath);
            kubeYamlData.setDeploymentEditableYaml("");
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


    /**
     * 根据id获取client
     *
     * @param id 应用环境id
     * @renturn ApiClient
     */
    public ApiClient getClient(Long id) {
        KubeCredentialBoV1 kubeCredentialBoV1 = this.kubeCredentialService.findByAppEnvId(id);
        String url = kubeCredentialBoV1.getTargetClusterUrl();
        String token = kubeCredentialBoV1.getTargetClusterToken();
        ApiClient client = Config.fromToken(url,
                token,
                false);
        return client;

    }

    /**
     * 根据id获取api
     *
     * @param id 应用环境id
     * @renturn CoreV1Api
     */
    public CoreV1Api getCoreApi(Long id) {

        return new CoreV1Api(getClient(id));
    }


    /**
     * 根据id获取api
     *
     * @param id 应用环境id
     * @renturn AppsV1Api
     */
    public AppsV1Api getAppsApi(Long id) {
        getClient(id);
        return new AppsV1Api(getClient(id));
    }

    /**
     * 创建服务
     *
     * @param id        应用环境id
     * @param namespace 命名空间
     * @param name      服务
     * @param port      容器
     */
    public void createServiceByNameSpace(Long id, String namespace, String name, Integer port) throws Exception {
        CoreV1Api coreApi = getCoreApi(id);

        AppsV1Api appsV1Api = getAppsApi(id);
        //V1Deployment deployment = new V1DeploymentBuilder()
        //        .withNewMetadata()
        //        .withName("test-deployment2")
        //        .addToLabels("app", name)
        //        .endMetadata()
        //        .withNewSpec()
        //        .withReplicas(2)
        //        .withNewSelector()
        //        .addToMatchLabels("app",name)
        //        .endSelector()
        //        .withNewTemplate()
        //        .withNewMetadata()
        //        .addToLabels("app", name)
        //        .endMetadata()
        //        .withNewSpec()
        //        .addNewContainer()
        //        .withName(name)
        //        .withImage( "registry.dop.clsaa.com/dop/dop-web:3")
        //        .endContainer()
        //        .addNewContainer()
        //        .withName("test-container")
        //        .withImage( "registry.dop.clsaa.com/dop/dop-web:4")
        //        .endContainer()
        //        .endSpec()
        //        .endTemplate()
        //        .endSpec()
        //        .build();
        //
        //V1Deployment deployment2 = new V1DeploymentBuilder()
        //        .withNewMetadata()
        //        .withName("test-deployment")
        //        .addToLabels("app", name)
        //        .endMetadata()
        //        .withNewSpec()
        //        .withReplicas(2)
        //        .withNewSelector()
        //        .addToMatchLabels("app",name)
        //        .endSelector()
        //        .withNewTemplate()
        //        .withNewMetadata()
        //        .addToLabels("app", name)
        //        .endMetadata()
        //        .withNewSpec()
        //        .addNewContainer()
        //        .withName(name)
        //        .withImage("registry.dop.clsaa.com/dop/dop-web:5")
        //        .endContainer()
        //        .endSpec()
        //        .endTemplate()
        //        .endSpec()
        //        .build();
        //
        //V1ReplicationController replicationController =
        //        new V1ReplicationControllerBuilder()
        //                .withNewMetadata()
        //                .withName(name)
        //                .endMetadata()
        //                .withNewSpec()
        //                .withReplicas(replicas.intValue())
        //                .addToSelector("app", name)
        //                .withNewTemplate()
        //                .withNewMetadata()
        //                .withName(name)
        //                .addToLabels("app", name)
        //                .endMetadata()
        //                .withNewSpec()
        //                .addNewContainer()
        //                .withName(name)
        //                .withImage(image)
        //                .addNewPort()
        //                .withContainerPort(port.intValue())
        //                .endPort()
        //                .endContainer()
        //                .endSpec()
        //                .endTemplate()
        //                .endSpec()
        //                .build();


        V1Service service =
                new V1ServiceBuilder()
                        .withNewMetadata()
                        .withName(name)
                        .addToLabels("app", name)
                        .endMetadata()
                        .withNewSpec()
                        .addNewPort()
                        .withProtocol("TCP")
                        .withPort(port)
                        .endPort()
                        .addToSelector("app", name)
                        .endSpec()
                        .build();
        //api.createNamespacedReplicationController(namespace,replicationController,null,null,null);
        //     appsV1Api.createNamespacedDeployment(namespace,deployment,false,null,null);
        //appsV1Api.createNamespacedDeployment(namespace,deployment2,false,null,null);
        coreApi.createNamespacedService(namespace, service, false, null, null);

    }

    /**
     * 获取该应用对应的cluster的所有命名空间
     *
     * @param id 应用环境id
     * @renturn{@link List<String>}
     */
    public List<String> findNameSpaces(Long id) throws Exception {
        CoreV1Api api = getCoreApi(id);

        return api.listNamespace(true, null, null, null, null, null, null, null, false)
                .getItems()
                .stream()
                .map(v1Namespace -> v1Namespace.getMetadata().getName())
                .collect(Collectors.toList());

    }

    /**
     * 根据命名空间获取服务列表
     *
     * @param id        应用环境id
     * @param namespace 命名空间
     * @renturn{@link List<String>}
     */
    public List<String> getServiceByNameSpace(Long id, String namespace) throws Exception {
        CoreV1Api api = getCoreApi(id);

        return api.listNamespacedService(namespace, false, null, null, null, null, Integer.MAX_VALUE, null, null, false)
                .getItems()
                .stream()
                .map(v1Service -> v1Service.getMetadata().getName())
                .collect(Collectors.toList());
    }

    /**
     * 根据命名空间及服务名称获取部署
     *
     * @param id        应用环境id
     * @param namespace 命名空间
     * @param service   服务
     * @renturn{@link List<String>}
     */
    public HashMap<String, Object> getDeploymentByNameSpaceAndService(Long id, String namespace, String service) throws Exception {

        AppsV1Api api = getAppsApi(id);


        V1DeploymentList deploymentList = api.listNamespacedDeployment(namespace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false);

        List<V1Deployment> v1DeploymentList = deploymentList.getItems();

        List<String> nameList = v1DeploymentList.stream().map(v1Deployment -> v1Deployment.getMetadata().getName()).collect(Collectors.toList());
        Map<String, List<String>> containerList = new HashMap<>();
        List<List<String>> lists = deploymentList.getItems().stream().map(
                v1Deployment -> v1Deployment.getSpec().getTemplate().getSpec().getContainers().stream().map(
                        v1Container -> v1Container.getName()).collect(Collectors.toList()
                )).collect(Collectors.toList());

        for (int i = 0; i < nameList.size(); i++) {
            containerList.put(nameList.get(i), v1DeploymentList.get(i).getSpec().getTemplate().getSpec().getContainers().stream().map(
                    v1Container -> v1Container.getName()).collect(Collectors.toList()
            ));
        }


        return new HashMap<String, Object>() {
            {
                put("deployment", nameList);
                put("containers", containerList);
            }

        };
    }

}
