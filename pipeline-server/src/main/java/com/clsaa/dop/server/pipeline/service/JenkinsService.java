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

public class JenkinsService {
    private JenkinsServer jenkins;
    private String jenkinsURI;
    private String jenkinsUser;
    private String jenkinsPWD;
    private Map<String, Job> jobs;

    public JenkinsService(String jenkinsURI, String jenkinsUser, String jenkinsPWD) throws Exception{
        this.jenkinsURI = jenkinsURI;
        this.jenkinsUser = jenkinsUser;
        this.jenkinsPWD = jenkinsPWD;
        this.jenkins = new JenkinsServer(new URI(jenkinsURI), jenkinsUser, jenkinsPWD);
        this.jobs = jenkins.getJobs();
    }

    /***
     * 创建流水线
     * param: 流水线的信息, 版本
     * */
    public String createJob(PipelineBoV1 pipelineBoV1, String version){
        Jenkinsfile jenkinsfile = new Jenkinsfile(pipelineBoV1.getStages());
        String name = pipelineBoV1.getName() + "_"+pipelineBoV1.getCtime()+"_" + pipelineBoV1.getCuser();
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
    public String deleteJob(String jobName){
        try{
            jenkins.deleteJob(jobName);
        }catch (Exception e){
            return e.toString();
        }
        return "DeleteJobSuccess";
    }

    /**
     * 运行流水线
     * param: 流水线的名称
     *
     * */

    public String build(String jobName){
        try{
            if(this.jobs.containsKey(jobName)){
                JobWithDetails job = jobs.get(jobName).details();
                job.build();
            }else{
                return "NotFindJobNamed:" + jobName;
            }
        }catch (Exception e){
            return e.toString();
        }
        return "BuildSuccess";
    }
//    public Map<String, String> getBuildResult(String jobName){
//        Map<String, String> result = new HashMap<String, String>();
//        try{
//            JobWithDetails job = jobs.get(jobName).details();
//            List<Build> builds = job.getAllBuilds();
//            for(int i = 0; i < builds.size(); i++){
//                Build build = builds.get(i);
//                result.put(build.getUrl(), build.details().getResult().toString());
//            }
//            return result;
//        }catch (Exception e){
//            result.put("error", e.toString());
//            return result;
//        }
//    }
//    public String getBuildOutputText(String jobName){
//        Map<String, String> result = new HashMap<String, String>();
//        try{
//            JobWithDetails job = jobs.get(jobName).details();
//            Build builds = job.getLastBuild();
//            return builds.details().getConsoleOutputText();
//        }catch (Exception e){
//            return e.toString();
//        }
//    }
}

