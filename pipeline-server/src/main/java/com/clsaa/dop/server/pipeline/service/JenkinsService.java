package com.clsaa.dop.server.pipeline.service;

import com.clsaa.dop.server.pipeline.config.Jenkinsfile;
import com.clsaa.dop.server.pipeline.config.JobConfig;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流水线-jenkins业务实现类
 *
 * @author 张富利
 * @since 2019-03-09
 */

@Service
public class JenkinsService {
    private static String JenkinsURI = "http://jenkins.dop.clsaa.com";
    private static String User = "zfl";
    private static String PWD = "zfl";
    private JenkinsServer jenkins;


    public JenkinsService() throws Exception{
        this.jenkins = new JenkinsServer(new URI(JenkinsURI), User, PWD);
    }

    /***
     * 创建流水线
     * param: 流水线的信息, 版本
     * */
    public String createJob(PipelineBoV1 pipelineBoV1, String version){
        Jenkinsfile jenkinsfile = new Jenkinsfile(pipelineBoV1.getStages());
        String name = pipelineBoV1.getId();
        try {
            jenkins.createJob(name, new JobConfig(version, jenkinsfile.getScript()).getXml());
        }catch (Exception e){
            return e.toString();
        }
        return "CreateJobSuccess";
    }

    /***
     * 删除流水线
     * param: 流水线的名称
     * */
    public void deleteJob(String jobName){
        try{
            jenkins.deleteJob(jobName);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}

