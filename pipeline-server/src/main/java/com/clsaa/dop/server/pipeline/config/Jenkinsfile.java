package com.clsaa.dop.server.pipeline.config;


import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.yaml.snakeyaml.Yaml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


/**
 * 生成jenkinsfile
 *
 * @author 张富利
 * @since 2019-03-09
 */

public class Jenkinsfile {

    private String stages;
    private Long appEnvId;
    private String git;

    public Jenkinsfile() {
    }

    public Jenkinsfile(Long appEnvId, ArrayList<Stage> pipelineStage) {
        this.appEnvId = appEnvId;
        this.stages = "";

        for (int i = 0; i < pipelineStage.size(); i++) {
            Stage stage = pipelineStage.get(i);

            String name = stage.getName();
            this.stages += "stage(\'" + name + "\'){ \n";
            ArrayList<Step> steps = stage.getSteps();
            if (steps.size() == 0) {
                this.stages += "echo \'  没有运行的脚本 \' \n";
            }
            for (int j = 0; j < steps.size(); j++) {
                this.stages += "steps{\n";
                Step task = steps.get(j);
                String taskName = task.getTaskName();
                String gitUrl = task.getGitUrl();
                this.git = task.getGitUrl();
                String dockerUserName = task.getDockerUserName();
                String respository = task.getRepository();
                String dockerPassword = task.getDockerPassword();
                String respositoryVersion = task.getRepositoryVersion();
                String shell = task.getShell();
                String deploy = task.getDeploy();
                String ip = task.getIp();
                String token = task.getToken();
                String dockerRepoHost = "registry.dop.clsaa.com";
                String dockerRepoPath = "/default";
                String imageName = dockerRepoHost + dockerRepoPath;
                try {
                    if (respository.startsWith("http")) {
                        dockerRepoHost = new URL(respository).getHost();
                        dockerRepoPath = new URL(respository).getPath();
                        imageName = dockerRepoHost + dockerRepoPath;
                    } else {
                        dockerRepoHost = respository.split("/")[0];
                        imageName = dockerRepoHost;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    BizAssert.justFailed(new BizCode(BizCodes.INVALID_PARAM.getCode()
                            , e.getMessage()));
                }
                switch (taskName) {
                    case ("拉取代码"):
                        this.stages += "deleteDir() \n";
                        this.stages += "git \"" + gitUrl + "\" \n";
                        break;
                    case ("构建maven"):
                        this.stages += "sh \'mvn --version \' \n";
                        this.stages += "sh \"mvn -U -am clean package \" \n";
                        break;
                    case ("构建node"):
                        this.stages += "sh \'npm --version \' \n";
                        this.stages += "sh \'node --version \' \n";
                        this.stages += "sh \'npm install \' \n";
                        break;
                    case ("构建docker镜像"):
                        this.stages += "sh \'docker build -t " + imageName + ":" + respositoryVersion + " ./\' \n";
                        break;
                    case ("推送docker镜像"):

                        this.stages += "sh \'docker login -u \"" + dockerUserName + "\" -p \"" + dockerPassword + "\" " + dockerRepoHost + "\' \n";
                        this.stages += "sh \'docker push " + imageName + ":" + respositoryVersion + "\' \n";
                        break;
                    case ("自定义脚本"):
                        this.stages += "sh \'" + shell + "\' \n";
                    case ("部署"):
                        String[] deploys = deploy.split("---\n");
                        for (int z = 0; z < deploys.length; z++) {
                            Yaml yaml = new Yaml();
                            if (deploys[z] == "") {
                                break;
                            }
                            Map map = yaml.load(deploys[z]);
                            Object apiVersion = map.get("apiVersion");
                            Object kind = map.get("kind");
                            Map metadata = (Map) map.get("metadata");
                            Object namespace = metadata.get("namespace");
                            if (kind.toString().equals("Deployment")) {
                                // apiVersion: apps/v1beta1
                                this.stages += "sh \'\'\'\n" +
                                        "curl -X POST -H \'Content-Type:application/yaml\' " +
                                        "-k -H \'Bearer " + token + "\' " +
                                        ip +
                                        "/apis/" + apiVersion +
                                        "/namespaces/" + namespace.toString() +
                                        "/" + kind.toString().toLowerCase() + "s " +
                                        "-d \'\n" +
                                        deploys[z].trim() + "\n" +
                                        "\'\n" +
                                        " \'\'\'" + "\n";
                            } else {
                                this.stages += "sh \'\'\'\n" +
                                        "curl -X POST -H \'Content-Type:application/yaml\' " +
                                        "-k -H \'Bearer " + token + "\' " +
                                        ip +
                                        "/api/" + apiVersion +
                                        "/namespaces/" + namespace.toString() +
                                        "/" + kind.toString().toLowerCase() + "s " +
                                        "-d \'\n" +
                                        deploys[z].trim() + "\n" +
                                        "\'\n" +
                                        " \'\'\'" + "\n";
                            }
                        }
                        break;
                }
                this.stages += "}\n";
            }
            this.stages += "}\n";
        }
    }

    public String getScript() {
        return ("pipeline {\n" +
                "    agent any\n" +
                "    stages {\n" +
                this.stages + "\n" +
                "    }\n" +
                "}\n");
    }


}
