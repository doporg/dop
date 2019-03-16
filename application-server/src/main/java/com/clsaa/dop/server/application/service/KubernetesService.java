package com.clsaa.dop.server.application.service;

import io.kubernetes.client.ApiClient;

import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;

import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.stereotype.Service;


/*
 * 暂未开始使用
 */
@Service(value = "KubernetesService")
public class KubernetesService {
    public V1NamespaceList findNameSpaces(String url, String token) throws Exception {

        ApiClient client = Config.fromToken(url,
                token,
                false);
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        return api.listNamespace(true, null, null, null, null, null, null, null, false);

    }


}
