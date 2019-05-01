package com.clsaa.dop.server.pipeline.config;

/**
 * 生成创建jenkins是多需要的xml
 *
 * @author 张富利
 * @since 2019-03-09
 */

public class JobConfig {
    private String script;
    private String xml;
    private String timing;

    public JobConfig() {
        this.script = "";
        this.xml = "<flow-definition plugin=\"workflow-job@2.31\">\n" +
                "  <actions>\n" +
                "    <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin=\"pipeline-model-definition@1.3.4.1\"/>\n" +
                "    <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction plugin=\"pipeline-model-definition@1.3.4.1\">\n" +
                "      <jobProperties/>\n" +
                "      <triggers/>\n" +
                "      <parameters/>\n" +
                "      <options/>\n" +
                "    </org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction>\n" +
                "  </actions>\n" +
                "  <description></description>\n" +
                "  <keepDependencies>false</keepDependencies>\n" +
                "  <properties/>\n" +
                "  <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.61.1\">\n" +
                "    <script>" +

                "    </script>\n" +
                "    <sandbox>true</sandbox>\n" +
                "  </definition>\n" +
                "  <triggers/>\n" +
                "  <disabled>false</disabled>\n" +
                "</flow-definition>";
    }

    public JobConfig(String script, String timing) {
        this.script = script;
        this.timing = timing;
        this.xml = "<flow-definition plugin=\"workflow-job@2.31\">\n" +
                "  <actions>\n" +
                "    <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin=\"pipeline-model-definition@1.3.4.1\"/>\n" +
                "    <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction plugin=\"pipeline-model-definition@1.3.4.1\">\n" +
                "      <jobProperties/>\n" +
                "      <triggers/>\n" +
                "      <parameters/>\n" +
                "      <options/>\n" +
                "    </org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction>\n" +
                "  </actions>\n" +
                "  <description></description>\n" +
                "  <keepDependencies>false</keepDependencies>\n" +
                "  <properties>\n" +
                "\t\t<com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin=\"gitlab-plugin@1.5.12\">\n" +
                "\t\t\t<gitLabConnection/>\n" +
                "\t\t</com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>\n" +
                "\t\t<org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty plugin=\"gitlab-logo@1.0.3\">\n" +
                "\t\t\t<repositoryName/>\n" +
                "\t\t</org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty>\n" +
                "\t\t<com.sonyericsson.rebuild.RebuildSettings plugin=\"rebuild@1.29\">\n" +
                "\t\t\t<autoRebuild>false</autoRebuild>\n" +
                "\t\t\t<rebuildDisabled>false</rebuildDisabled>\n" +
                "\t\t</com.sonyericsson.rebuild.RebuildSettings>\n" +
                "\t\t<org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "\t\t\t<triggers>\n" +
                "\t\t\t\t<hudson.triggers.TimerTrigger>\n" +
                "\t\t\t\t\t<spec>"+ this.timing +"</spec>\n" +
                "\t\t\t\t</hudson.triggers.TimerTrigger>\n" +
                "\t\t\t</triggers>\n" +
                "\t\t</org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "\t</properties>\n" +
                "  <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.61.1\">\n" +
                "    <script>\n" +
                this.script +
                "</script>\n" +
                "    <sandbox>true</sandbox>\n" +
                "  </definition>\n" +
                "  <triggers/>\n" +
                "  <disabled>false</disabled>\n" +
                "</flow-definition>";
    }

    public JobConfig(String git, String path, String timing) {
        this.xml = "<flow-definition plugin=\"workflow-job@2.31\">\n" +
                "<actions/>\n" +
                "<description/>\n" +
                "<keepDependencies>false</keepDependencies>\n" +
                "<properties>\n" +
                "<com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin=\"gitlab-plugin@1.5.12\">\n" +
                "<gitLabConnection/>\n" +
                "</com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>\n" +
                "<org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty plugin=\"gitlab-logo@1.0.3\">\n" +
                "<repositoryName/>\n" +
                "</org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty>\n" +
                "<com.sonyericsson.rebuild.RebuildSettings plugin=\"rebuild@1.29\">\n" +
                "<autoRebuild>false</autoRebuild>\n" +
                "<rebuildDisabled>false</rebuildDisabled>\n" +
                "</com.sonyericsson.rebuild.RebuildSettings>\n" +
                "<org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "<triggers>\n" +
                "<hudson.triggers.TimerTrigger>\n" +
                "<spec>"+ timing +"</spec>\n" +
                "</hudson.triggers.TimerTrigger>\n" +
                "</triggers>\n" +
                "</org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "</properties>\n" +
                "<definition class=\"org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition\" plugin=\"workflow-cps@2.61\">\n" +
                "<scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.9.1\">\n" +
                "<configVersion>2</configVersion>\n" +
                "<userRemoteConfigs>\n" +
                "<hudson.plugins.git.UserRemoteConfig>\n" +
                "<url>\n" +
                git +"\n" +
                "</url>\n" +
                "</hudson.plugins.git.UserRemoteConfig>\n" +
                "</userRemoteConfigs>\n" +
                "<branches>\n" +
                "<hudson.plugins.git.BranchSpec>\n" +
                "<name>*/master</name>\n" +
                "</hudson.plugins.git.BranchSpec>\n" +
                "</branches>\n" +
                "<doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n" +
                "<submoduleCfg class=\"list\"/>\n" +
                "<extensions/>\n" +
                "</scm>\n" +
                "<scriptPath>"+path+"</scriptPath>\n" +
                "<lightweight>true</lightweight>\n" +
                "</definition>\n" +
                "<triggers/>\n" +
                "<disabled>false</disabled>\n" +
                "</flow-definition>";
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
