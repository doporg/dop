package com.clsaa.dop.server.pipeline.config;

import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.google.gson.JsonObject;

import java.util.ArrayList;


/**
 * 生成jenkinsfile
 *
 * @author 张富利
 * @since 2019-03-09
 */

public class Jenkinsfile {
    private JsonObject pipeline;
    private String stages;
    private String commond;
    private String stageName;
    public Jenkinsfile(){}
    public Jenkinsfile(ArrayList<Stage> pipelineStage){
        this.stages = "";
        for(int i = 0; i < pipelineStage.size();i++){
            Stage stage = pipelineStage.get(i);

            String name = stage.getName();
            this.stages += "stage(\'" + name + "\'){ \n";
            ArrayList<Step> steps = stage.getSteps();
            if(steps.size() == 0){
                this.stages += "echo \'  没有运行的脚本 \' \n";
            }
            for(int j = 0;j < steps.size(); j++){
                this.stages += "steps{\n";
                Step task = steps.get(j);
                String taskName = task.getTaskName();
                String gitUrl = task.getGitUrl();
                String dockerUserName = task.getDockerUserName();
                String respository = task.getRepository();
                String dockerPassword = task.getDockerPassword();
                String respositoryVersion = task.getRepositoryVersion();
                String shell = task.getShell();
                switch (taskName){
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
                        this.stages += "sh \'docker build -t registry.dop.clsaa.com/" + dockerUserName + "/" + respository + ":" + respositoryVersion + " ./\' \n";
                        break;
                    case ("推送docker镜像"):
                        this.stages += "sh \'docker login -u " + dockerUserName + " -p " + dockerPassword + " registry.dop.clsaa.com\' \n";
                        this.stages += "sh \'docker push registry.dop.clsaa.com/" + dockerUserName + "/" + respository + ":" + respositoryVersion + "\' \n";
                        break;
                    case ("自定义脚本"):
                        this.stages += "sh \'" + shell + "\' \n";
                    case ("部署"):
                        this.stages += "sh \'" + shell + "\' \n";
                        break;
                }
                this.stages += "}\n";
            }
            this.stages += "}\n";
        }
    }
    public String getScript(){
        return ("pipeline {\n" +
                "    agent any\n" +
                "    stages {\n" +
                        this.stages +
                "    }\n" +
                "}");
    }

}
