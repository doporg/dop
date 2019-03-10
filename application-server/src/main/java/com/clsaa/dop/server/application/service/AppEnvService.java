package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppEnvRepository;
import com.clsaa.dop.server.application.dao.AppVarRepository;
import com.clsaa.dop.server.application.model.bo.AppEnvBoV1;
import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppEnvironment;
import com.clsaa.dop.server.application.model.vo.AppEnvDetailV1;
import com.clsaa.dop.server.application.util.BeanUtils;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import org.apache.commons.configuration.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "AppEnvService")
public class AppEnvService {
    @Autowired
    AppYamlService appYamlService;

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
     * 根据id获取client
     *
     * @param id        应用环境id
     * @renturn ApiClient
     */
    public ApiClient getClient(Long id) {
        AppEnvironment appEnv = this.appEnvRepository.findById(id).orElse(null);
        String url = appEnv.getTargetClusterUrl();
        String token = appEnv.getTargetClusterToken();
        ApiClient client = Config.fromToken(url,
                token,
                false);
        return client;

    }

    /**
     * 根据id获取api
     *
     * @param id        应用环境id
     * @renturn CoreV1Api
     */
    public CoreV1Api getCoreApi(Long id) {

        return new CoreV1Api(getClient(id));
    }


    /**
     * 根据id获取api
     *
     * @param id        应用环境id
     * @renturn AppsV1Api
     */
    public AppsV1Api getAppsApi(Long id) {
        getClient(id);
        return new AppsV1Api(getClient(id));
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
     * 更新url和token信息
     *
     * @param id        应用环境id
     * @param url url
     * @param token  token
     * */
    public void updateUrlAndToken(Long id, String url, String token) {
        AppEnvironment appEnvironment = this.appEnvRepository.findById(id).orElse(null);
        appEnvironment.setTargetClusterUrl(url);
        appEnvironment.setTargetClusterToken(token);
        this.appEnvRepository.saveAndFlush(appEnvironment);
    }


    /**
     * 根据命名空间及服务名称获取部署
     *
     * @param id        应用环境id
     * @param namespace 命名空间
     * @param service      服务
     * @renturn{@link List<String>}
     */
    public List<String> getDeploymentByNameSpaceAndService(Long id, String namespace, String service) throws Exception {

        AppsV1Api api = getAppsApi(id);

        List<String> deploymentList = api.listNamespacedDeployment(namespace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false)
                .getItems()
                .stream()
                .map(v1Service -> v1Service.getMetadata().getName())
                .collect(Collectors.toList());
        if (deploymentList.size() <= 1) {
            return null;
        } else {
            return deploymentList;
        }
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

//AppsV1Api appsV1Api = getAppsApi(id);
//        V1Deployment deployment = new V1DeploymentBuilder()
//                .withNewMetadata()
//                .withName(name)
//                .addToLabels("app", name)
//                .endMetadata()
//                .withNewSpec()
//                .withReplicas(replicas.intValue())
//                .withNewSelector()
//                .addToMatchLabels("app",name)
//                .endSelector()
//                .withNewTemplate()
//                .withNewMetadata()
//                .addToLabels("app", name)
//                .endMetadata()
//                .withNewSpec()
//                .addNewContainer()
//                .withName(name)
//                .withImage(image)
//                .endContainer()
//                .endSpec()
//                .endTemplate()
//                .endSpec()
//                .build();
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
                        .endSpec()
                        .build();
        //api.createNamespacedReplicationController(namespace,replicationController,null,null,null);
        //     appsV1Api.createNamespacedDeployment(namespace,deployment,false,null,null);
        coreApi.createNamespacedService(namespace, service, false, null, null);

    }
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
     * @param releaseBatch 发布批次
     * @param replicas    副本数量
     */
    public void updateOrCreateYamlInfoByAppEnvId(Long appEnvId, Long cuser, String nameSpace, String service, String deployment, String containers, String releaseStrategy, Integer replicas
            , Long releaseBatch, String image_url) {
        this.appYamlService.updataOrCreateYamlData(appEnvId, cuser, nameSpace, service, deployment, containers, releaseStrategy, replicas
                , releaseBatch, image_url);

    }

}
