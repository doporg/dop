package cn.com.devopsplus.dop.server.pipeline.service;

import cn.com.devopsplus.dop.server.pipeline.util.JenkinsUtils;
import cn.com.devopsplus.dop.server.pipeline.util.JenkinsfileUtil;
import cn.com.devopsplus.dop.server.pipeline.config.JobConfig;
import cn.com.devopsplus.dop.server.pipeline.model.po.Pipeline;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
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

    @Autowired
    private JenkinsServer jenkins;

    private JenkinsUtils jenkinsUtils = new JenkinsUtils();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JenkinsService() {}

    @Bean
    public static JenkinsServer getJenkinsServer() throws URISyntaxException {
        return new JenkinsServer(new URI(JenkinsUtils.uri), JenkinsUtils.username, JenkinsUtils.password);
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

