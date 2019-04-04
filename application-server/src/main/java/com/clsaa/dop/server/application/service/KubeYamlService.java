package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.KubeYamlRepository;
import com.clsaa.dop.server.application.model.bo.KubeCredentialBoV1;
import com.clsaa.dop.server.application.model.bo.KubeYamlDataBoV1;
import com.clsaa.dop.server.application.model.po.KubeYamlData;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1beta1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "KubeYamlService")
public class KubeYamlService {
    @Autowired
    KubeYamlRepository kubeYamlRepository;

    @Autowired
    AppEnvService appEnvService;
    @Autowired
    KubeCredentialService kubeCredentialService;
    @Autowired
    BuildTagRunningIdMappingService buildTagRunningIdMappingService;
    @Autowired
    AppUrlInfoService appUrlInfoService;

    public String createYamlFileForDeploy(Long cuser, Long appEnvId, String runningId) {

        KubeYamlDataBoV1 kubeYamlDataBoV1 = this.findYamlDataByEnvId(appEnvId);
        if (kubeYamlDataBoV1.getYamlFilePath().equals("")) {
            String yaml = (kubeYamlDataBoV1.getDeploymentEditableYaml());
            String buildTag = buildTagRunningIdMappingService.findBuildTagByRunningIdAndAppEnvId(cuser, runningId, appEnvId);
            Long appId = this.appEnvService.findAppIdById(appEnvId);
            String imageUrl = appUrlInfoService.findAppUrlInfoByAppId(appId).getImageUrl();
            yaml = yaml.replace("<image_url>", imageUrl + ":" + buildTag);
            return yaml;

        } else {
            String path = kubeYamlDataBoV1.getYamlFilePath();


            String[] splitPath = path.split("blob/");
            String finalPath = "https://raw.githubusercontent.com/" + splitPath[0].split("github.com/")[1] + splitPath[1];

            RestTemplate restTemplate = new RestTemplate();
            String yaml = restTemplate.getForObject(finalPath, String.class);
            String buildTag = buildTagRunningIdMappingService.findBuildTagByRunningIdAndAppEnvId(cuser, runningId, appEnvId);

            yaml = yaml.replace("<image_url>", buildTag);
            return yaml;
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
     * @param container       容器
     * @param releaseStrategy 发布策略
     * @param releaseBatch    发布批次
     * @param replicas        副本数量
     */
    public void CreateYamlData(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String container, String releaseStrategy, Integer replicas
            , Long releaseBatch, String yamlFilePath) throws Exception {

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
                .build();

        if (yamlFilePath.equals("")) {
            CoreV1Api coreV1Api = getCoreV1Api(appEnvId);
            List<V1Service> serviceList = coreV1Api.listNamespacedService(nameSpace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            IntOrString targetPort = serviceList.get(0).getSpec().getPorts().get(0).getTargetPort();
            AppsV1beta1Api appsV1beta1Api = getAppsV1beta1Api(appEnvId);
            List<AppsV1beta1Deployment> deploymentList = appsV1beta1Api.listNamespacedDeployment(nameSpace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            if (deploymentList.size() != 0) {
                for (int i = 0; i < deploymentList.size(); i++) {
                    AppsV1beta1Deployment AppsV1beta1Deployment = deploymentList.get(i);
                    if (AppsV1beta1Deployment.getMetadata().getName().equals(deployment) || deployment.equals("")) {
                        List<V1Container> containerList = AppsV1beta1Deployment.getSpec().getTemplate().getSpec().getContainers();
                        for (int j = 0; i < containerList.size(); j++) {
                            if (containerList.get(i).getName().equals(container) || container.equals((""))) {
                                containerList.get(i).setImage("<image_url>");
                                break;
                            }
                        }
                        AppsV1beta1Deployment.getSpec().getTemplate().getSpec().setContainers(containerList);
                        String apiVersion = AppsV1beta1Deployment.getApiVersion();
                        if (apiVersion == null) {
                            apiVersion = "apps/v1beta1";
                        }
                        String kind = AppsV1beta1Deployment.getKind();
                        if (kind == null) {
                            kind = "Deployment";
                        }
                        V1ObjectMeta meta = AppsV1beta1Deployment.getMetadata();
                        V1ObjectMeta newMeta = new V1ObjectMetaBuilder()
                                .withLabels(meta.getLabels())
                                .withName(meta.getName())
                                .withNamespace(meta.getNamespace())
                                .build();
                        AppsV1beta1DeploymentSpec spec = AppsV1beta1Deployment.getSpec();

                        AppsV1beta1Deployment appsV1beta1Deployment = new AppsV1beta1DeploymentBuilder()
                                .withApiVersion(apiVersion)
                                .withKind(kind)
                                .withMetadata(newMeta)
                                .withSpec(spec)
                                .build();
                        kubeYamlData.setDeploymentEditableYaml(Yaml.dump(appsV1beta1Deployment));
                        break;
                        //appsV1beta1Api.createNamespacedDeployment(nameSpace, AppsV1beta1Deployment, false, null, null);
                    }
                }

            } else {
                AppsV1beta1Deployment AppsV1beta1Deployment = new AppsV1beta1DeploymentBuilder()
                        .withApiVersion("apps/v1beta1")
                        .withKind("Deployment")
                        .withNewMetadata()
                        .withName(service)
                        .withNamespace(nameSpace)
                        .addToLabels("app", service)
                        .endMetadata()
                        .withNewSpec()
                        .withReplicas(replicas)
                        .withNewSelector()
                        .addToMatchLabels("app", service)
                        .endSelector()
                        .withNewStrategy()
                        .withNewRollingUpdate()
                        .withMaxSurge(new IntOrString(1))
                        .withMaxUnavailable(new IntOrString(1))
                        .endRollingUpdate()
                        .endStrategy()
                        .withNewTemplate()
                        .withNewMetadata()
                        .addToLabels("app", service)
                        .endMetadata()
                        .withNewSpec()
                        .addNewContainer()
                        .withName(service)
                        .withImage("<image_url>")
                        .addNewPort()
                        .withContainerPort(targetPort.getIntValue())
                        .withProtocol("TCP")
                        .endPort()
                        .withImagePullPolicy("Always")
                        .addNewVolumeMount()
                        .withMountPath(" /etc/localtime")
                        .withName("host-time")
                        .withMountPath("/etc/timezone")
                        .withName("host-timezone")
                        .endVolumeMount()
                        .endContainer()
                        .withDnsPolicy("ClusterFirst")
                        .addNewVolume()
                        .withNewHostPath()
                        .withType("")
                        .withPath("/etc/localtime")
                        .endHostPath()
                        .withName("host-time")
                        .endVolume()
                        .addNewVolume()
                        .withNewHostPath()
                        .withType("")
                        .withPath("/etc/timezone")
                        .endHostPath()
                        .withName("host-timezone")
                        .endVolume()
                        .endSpec()
                        .endTemplate()
                        .endSpec()
                        .build();

                kubeYamlData.setDeploymentEditableYaml(Yaml.dump(AppsV1beta1Deployment));

                //appsV1beta1Api.createNamespacedDeployment(nameSpace, AppsV1beta1Deployment, false, null, null);
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
     */
    public void updateYamlData(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String container, String releaseStrategy, Integer replicas
            , Long releaseBatch, String yamlFilePath) throws Exception {
        KubeYamlData kubeYamlData = this.kubeYamlRepository.findByAppEnvId(appEnvId).orElse(null);
        kubeYamlData.setMtime(LocalDateTime.now());
        kubeYamlData.setMuser(cuser);
        kubeYamlData.setNameSpace(nameSpace);
        kubeYamlData.setService(service);
        kubeYamlData.setDeployment(deployment);
        kubeYamlData.setContainers(container);
        kubeYamlData.setReplicas(replicas);
        kubeYamlData.setReleaseBatch(releaseBatch);
        //kubeYamlData.setImageUrl(imageUrl);

        kubeYamlData.setReleaseStrategy(KubeYamlData.ReleaseStrategy.valueOf(releaseStrategy));



        if (yamlFilePath.equals("")) {
            CoreV1Api coreV1Api = getCoreV1Api(appEnvId);
            List<V1Service> serviceList = coreV1Api.listNamespacedService(nameSpace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            IntOrString targetPort = serviceList.get(0).getSpec().getPorts().get(0).getTargetPort();
            AppsV1beta1Api appsV1beta1Api = getAppsV1beta1Api(appEnvId);
            List<AppsV1beta1Deployment> deploymentList = appsV1beta1Api.listNamespacedDeployment(nameSpace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            if (deploymentList.size() != 0) {
                for (int i = 0; i < deploymentList.size(); i++) {
                    AppsV1beta1Deployment AppsV1beta1Deployment = deploymentList.get(i);
                    if (AppsV1beta1Deployment.getMetadata().getName().equals(deployment) || deployment.equals("")) {
                        List<V1Container> containerList = AppsV1beta1Deployment.getSpec().getTemplate().getSpec().getContainers();
                        for (int j = 0; i < containerList.size(); j++) {
                            if (containerList.get(i).getName().equals(container) || container.equals((""))) {
                                containerList.get(i).setImage("<image_url>");
                                break;
                            }
                        }
                        AppsV1beta1Deployment.getSpec().getTemplate().getSpec().setContainers(containerList);
                        String apiVersion = AppsV1beta1Deployment.getApiVersion();
                        if (apiVersion == null) {
                            apiVersion = "apps/v1beta1";
                        }
                        String kind = AppsV1beta1Deployment.getKind();
                        if (kind == null) {
                            kind = "Deployment";
                        }
                        V1ObjectMeta meta = AppsV1beta1Deployment.getMetadata();
                        V1ObjectMeta newMeta = new V1ObjectMetaBuilder()
                                .withLabels(meta.getLabels())
                                .withName(meta.getName())
                                .withNamespace(meta.getNamespace())
                                .build();
                        AppsV1beta1DeploymentSpec spec = AppsV1beta1Deployment.getSpec();

                        AppsV1beta1Deployment appsV1beta1Deployment = new AppsV1beta1DeploymentBuilder()
                                .withApiVersion(apiVersion)
                                .withKind(kind)
                                .withMetadata(newMeta)
                                .withSpec(spec)
                                .build();
                        kubeYamlData.setDeploymentEditableYaml(Yaml.dump(appsV1beta1Deployment));
                        //kubeYamlData.setDeploymentEditableYaml(Yaml.dump(AppsV1beta1Deployment));
                        break;
                    }
                }

            } else {
                AppsV1beta1Deployment AppsV1beta1Deployment = new AppsV1beta1DeploymentBuilder()
                        .withApiVersion("apps/v1beta1")
                        .withKind("Deployment")
                        .withNewMetadata()
                        .withName(service)
                        .withNamespace(nameSpace)
                        .addToLabels("app", service)
                        .endMetadata()
                        .withNewSpec()
                        .withReplicas(replicas)
                        .withNewSelector()
                        .addToMatchLabels("app", service)
                        .endSelector()
                        .withNewStrategy()
                        .withNewRollingUpdate()
                        .withMaxSurge(new IntOrString(1))
                        .withMaxUnavailable(new IntOrString(1))
                        .endRollingUpdate()
                        .endStrategy()
                        .withNewTemplate()
                        .withNewMetadata()
                        .addToLabels("app", service)
                        .endMetadata()
                        .withNewSpec()
                        .addNewContainer()
                        .withName(service)
                        .withImage("<image_url>")
                        .addNewPort()
                        .withContainerPort(targetPort.getIntValue())
                        .withProtocol("TCP")
                        .endPort()
                        .withImagePullPolicy("Always")
                        .addNewVolumeMount()
                        .withMountPath(" /etc/localtime")
                        .withName("host-time")
                        .withMountPath("/etc/timezone")
                        .withName("host-timezone")
                        .endVolumeMount()
                        .endContainer()
                        .withDnsPolicy("ClusterFirst")
                        .addNewVolume()
                        .withNewHostPath()
                        .withType("")
                        .withPath("/etc/localtime")
                        .endHostPath()
                        .withName("host-time")
                        .endVolume()
                        .addNewVolume()
                        .withNewHostPath()
                        .withType("")
                        .withPath("/etc/timezone")
                        .endHostPath()
                        .withName("host-timezone")
                        .endVolume()
                        .endSpec()
                        .endTemplate()
                        .endSpec()
                        .build();


                kubeYamlData.setDeploymentEditableYaml(Yaml.dump(AppsV1beta1Deployment));

                //appsV1beta1Api.createNamespacedDeployment(nameSpace, AppsV1beta1Deployment, false, null, null);
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

    public void updateDeploymentYaml(Long muser, Long appEnvId, String deploymentYaml) {
        KubeYamlData kubeYamlData = this.kubeYamlRepository.findByAppEnvId(appEnvId).orElse(null);
        kubeYamlData.setMtime(LocalDateTime.now());
        kubeYamlData.setMuser(muser);
        kubeYamlData.setDeploymentEditableYaml(deploymentYaml);
        this.kubeYamlRepository.saveAndFlush(kubeYamlData);
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
     * @renturn NetworkingV1Api
     */
    public ExtensionsV1beta1Api getExtensionsV1beta1Api(Long id) {

        return new ExtensionsV1beta1Api(getClient(id));
    }


    /**
     * 根据id获取api
     *
     * @param id 应用环境id
     * @renturn CoreV1Api
     */
    public CoreV1Api getCoreV1Api(Long id) {

        return new CoreV1Api(getClient(id));
    }


    /**
     * 根据id获取api
     *
     * @param id 应用环境id
     * @renturn AppsV1beta1Api
     */
    public AppsV1beta1Api getAppsV1beta1Api(Long id) {
        getClient(id);
        return new AppsV1beta1Api(getClient(id));
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
        CoreV1Api coreV1Api = getCoreV1Api(id);

        AppsV1beta1Api AppsV1beta1Api = getAppsV1beta1Api(id);
        //AppsV1beta1Deployment deployment = new AppsV1beta1DeploymentBuilder()
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
        //AppsV1beta1Deployment deployment2 = new AppsV1beta1DeploymentBuilder()
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
                        .withApiVersion("v1")
                        .withKind("Service")
                        .withNewMetadata()
                        .withName(name)
                        .withNamespace(namespace)
                        .addToLabels("app", name)
                        .endMetadata()
                        .withNewSpec()
                        .addNewPort()
                        .withPort(port)
                        .withTargetPort(new IntOrString(port))
                        .withProtocol("TCP")
                        .endPort()
                        .addToSelector("app", name)
                        .endSpec()
                        .build();
        //api.createNamespacedReplicationController(namespace,replicationController,null,null,null);
        //     AppsV1beta1Api.createNamespacedDeployment(namespace,deployment,false,null,null);
        //AppsV1beta1Api.createNamespacedDeployment(namespace,deployment2,false,null,null);
        coreV1Api.createNamespacedService(namespace, service, false, null, null);

        V1beta1Ingress ingress =
                new V1beta1IngressBuilder()
                        .withApiVersion("apps/v1beta1")
                        .withKind("Ingress")
                        .withNewMetadata()
                        .withName(name)
                        .withNamespace(namespace)
                        .endMetadata()
                        .withNewSpec()
                        .addNewRule()
                        .withHost("www.dpp.clsaa.com")
                        .withNewHttp()
                        .addNewPath()
                        .withNewBackend()
                        .withServiceName(name)
                        .withServicePort(new IntOrString(port))
                        .endBackend()
                        .endPath()
                        .endHttp()
                        .endRule()
                        .endSpec()
                        .build();

        ExtensionsV1beta1Api extensionsV1beta1Api = getExtensionsV1beta1Api(id);
        extensionsV1beta1Api.createNamespacedIngress(namespace, ingress, false, null, null);
        //V1NetworkPolicy v1NetworkPolicy=new V1NetworkPolicy(ingress);
        //networkingV1Api.createNamespacedNetworkPolicy(namespace,ingress);


    }

    /**
     * 获取该应用对应的cluster的所有命名空间
     *
     * @param id 应用环境id
     * @renturn{@link List<String>}
     */
    public List<String> findNameSpaces(Long id) throws Exception {
        CoreV1Api api = getCoreV1Api(id);

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
        CoreV1Api api = getCoreV1Api(id);

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

        AppsV1beta1Api api = getAppsV1beta1Api(id);


        AppsV1beta1DeploymentList deploymentList = api.listNamespacedDeployment(namespace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false);

        List<AppsV1beta1Deployment> AppsV1beta1DeploymentList = deploymentList.getItems();

        List<String> nameList = AppsV1beta1DeploymentList.stream().map(AppsV1beta1Deployment -> AppsV1beta1Deployment.getMetadata().getName()).collect(Collectors.toList());
        Map<String, List<String>> containerList = new HashMap<>();
        List<List<String>> lists = deploymentList.getItems().stream().map(
                AppsV1beta1Deployment -> AppsV1beta1Deployment.getSpec().getTemplate().getSpec().getContainers().stream().map(
                        v1Container -> v1Container.getName()).collect(Collectors.toList()
                )).collect(Collectors.toList());

        for (int i = 0; i < nameList.size(); i++) {
            containerList.put(nameList.get(i), AppsV1beta1DeploymentList.get(i).getSpec().getTemplate().getSpec().getContainers().stream().map(
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
