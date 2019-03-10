package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppEnvRepository;
import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppEnvironment;
import io.kubernetes.client.ApiClient;

import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;

import io.kubernetes.client.proto.V1;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/*
 * 暂未开始使用
 */
@Service(value = "KubernetesService")
public class KubernetesService {
    @Autowired
    AppEnvRepository appEnvRepository;


}


