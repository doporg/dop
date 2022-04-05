package com.clsaa.dop.server.pipeline.service;

import com.clsaa.dop.server.pipeline.config.Jenkinsfile;
import com.clsaa.dop.server.pipeline.config.JobConfig;
import com.clsaa.dop.server.pipeline.dao.PipelineRepository;
import com.clsaa.dop.server.pipeline.dao.ResultOutputRepository;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.clsaa.dop.server.pipeline.util.JenkinsUtils;
import com.clsaa.dop.server.pipeline.util.JenkinsfileUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流水线-jenkins业务实现类
 *
 * @author 张富利
 * @since 2019-03-09
 */

@Service
public class JenkinsService {
    private String jenkinsURI;
    private String user;
    private String pwd;
    private JenkinsServer jenkins;
    private JenkinsUtils jenkinsUtils = new JenkinsUtils();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JenkinsService() {
        this.jenkinsURI = jenkinsUtils.getUri();
        this.user = jenkinsUtils.getUsername();
        this.pwd = jenkinsUtils.getPassword();
        try {
            this.jenkins = new JenkinsServer(new URI(jenkinsURI), user, pwd);
        }catch (Exception e){
            logger.error("[JenkinsService] 初始化失败！Exception",e);
            e.printStackTrace();
        }

    }

    /***
     * 创建流水线
     * param: 流水线的信息, 版本
     * */
    public String createJob(Pipeline pipeline) {
        logger.info("[createJob] Request coming: pipeline");
        String jenkinsfile = JenkinsfileUtil.generate(pipeline.getStages());
        String name = pipeline.getId();

        try {
            if (jenkins.getJob(name) == null) {
                logger.info("[createJob] Get Script={}",jenkinsfile);
                logger.info("[createJob] Get Timing={}",pipeline.getTiming());
                jenkins.createJob(name, new JobConfig(jenkinsfile, pipeline.getTiming()).getXml());
                logger.info("[createJob] Create the new pipeline successful!");
            } else {
                jenkins.deleteJob(name);
                logger.info("[createJob] Get Script={}",jenkinsfile);
                logger.info("[createJob] Get Timing={}",pipeline.getTiming());
                jenkins.createJob(name, new JobConfig(jenkinsfile, pipeline.getTiming()).getXml());
                logger.info("[createJob] Replace the new pipeline successful!");
            }
        } catch (Exception e) {
            logger.error("[createJob] 无法创建流水线！Exception",e);
            System.out.println(e.toString());
            return e.toString();
        }
        return "CreateJobSuccess";
    }

    /**
     * 根据jenkinsfile创建流水线
     */
    public void createByJenkinsfile(Pipeline pipeline) {
        logger.info("[createByJenkinsfile] Request coming: pipeline");
        System.out.println(new JobConfig(pipeline.getJenkinsfile().getGit(), pipeline.getJenkinsfile().getPath(), pipeline.getTiming()).getXml());
        try {
            if (jenkins.getJob(pipeline.getId()) == null) {
                logger.info("[createByJenkinsfile] Get git={}",pipeline.getJenkinsfile().getGit());
                logger.info("[createByJenkinsfile] Get path={}",pipeline.getJenkinsfile().getPath());
                logger.info("[createByJenkinsfile] Get timing={}",pipeline.getTiming());
                jenkins.createJob(pipeline.getId(), new JobConfig(pipeline.getJenkinsfile().getGit(), pipeline.getJenkinsfile().getPath(), pipeline.getTiming()).getXml());
                logger.info("[createByJenkinsfile] Create the new pipeline successful!");
            } else {
                jenkins.deleteJob(pipeline.getId());
                logger.info("[createByJenkinsfile] Get git={}",pipeline.getJenkinsfile().getGit());
                logger.info("[createByJenkinsfile] Get path={}",pipeline.getJenkinsfile().getPath());
                logger.info("[createByJenkinsfile] Get timing={}",pipeline.getTiming());
                jenkins.createJob(pipeline.getId(), new JobConfig(pipeline.getJenkinsfile().getGit(), pipeline.getJenkinsfile().getPath(), pipeline.getTiming()).getXml());
                logger.info("[createByJenkinsfile] Replace the new pipeline successful!");
            }
        } catch (Exception e) {
            logger.error("[createByJenkinsfile] 无法使用Jenkinsfile创建流水线！Exception",e);
            System.out.println(e.toString());
        }
    }

    /**
     * 根据流水线的名字的到运行日志
     */
    public String getBuildOutputText(String name) {
        logger.info("[getBuildOutputText] Request coming: name={}",name);
        try {
            Map<String, Job> jobs = jenkins.getJobs();
            JobWithDetails job = jobs.get(name).details();
            Build builds = job.getLastBuild();
            logger.info("[getBuildOutputText] Get the build info!");
            return builds.details().getConsoleOutputText();
        } catch (Exception e) {
            logger.error("[getBuildOutputText] 没有该流水线！Exception",e);
            return "没有该流水线";
        }
    }

    /**
     * 根据流水线的名字的到运行结果
     */
    public String getBuildResult(String name) {
        logger.info("[getBuildResult] Request coming: name={}",name);
        try {
            Map<String, Job> jobs = jenkins.getJobs();
            JobWithDetails job = jobs.get(name).details();
            Build builds = job.getLastBuild();
            logger.info("[getBuildResult] Get the build info!");
            return builds.details().getResult().toString();
        } catch (Exception e) {
            logger.error("[getBuildResult] 无法获得流水线运行结果！Exception",e);
            return e.toString();
        }
    }

    /***
     * 删除流水线
     * param: 流水线的名称
     * */
    public void deleteJob(String jobName) {
        logger.info("[deleteJob] Request coming: jobName={}",jobName);
        try {
            jenkins.deleteJob(jobName);
            logger.info("[deleteJob] Delete the pipeline successful!");
        } catch (Exception e) {
            logger.error("[JenkinsService] 流水线删除失败！Exception",e);
            System.out.println(e.toString());
        }
    }


}

