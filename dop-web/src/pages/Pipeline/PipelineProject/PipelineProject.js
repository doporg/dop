/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {Button, Progress, Icon} from '@icedesign/base';
import Axios from 'axios';
import jsonp from 'jsonp';
import API from '../../API';
import './PipelineProject.scss'


export default class PipelineProject extends Component {
    constructor(props) {
        super(props);
        this.state = {
            pipelineInfo: {
                name: "",
                creator: "",
                admin: [{
                    id: '1',
                    name: 'test'
                }],
                //监听设置
                monitor: "",
                createTime: "",  //时间戳
                stage: [
                    //     {
                    //     name: "",
                    //      tasks: [
                    //          //{
                    //     //     taskName: "构建maven",
                    //     //     gitUrl: "",
                    //     //     dockerUserName: "",
                    //     //     repository: "",
                    //     //     description: ""
                    //     // }, {
                    //     //     taskName: "构建docker镜像",
                    //     //     gitUrl: "",
                    //     //     dockerUserName: "",
                    //     //     repository: "",
                    //     //     description: ""
                    //     // }, {
                    //     //     taskName: "推送docker镜像",
                    //     //     gitUrl: "",
                    //     //     dockerUserName: "",
                    //     //     repository: "",
                    //     //     description: ""
                    //     // }
                    //     ]
                    // }
                ]
            },
            currentStageTab: -1,
            currentStageName: "",
            buildResult: [
                {
                    id: "",
                    stageName: "",
                    result: "",
                    outputText: ""
                }
            ],
            output: "",
            jenkinsRuns: {
                durationMillis: "",
                endTimeMillis: "",
                id: "",
                name: "",
                pauseDurationMillis: "",
                queueDurationMillis: "",
                stages: [{
                    _links: {
                        self: {
                            href: ""
                        }
                    },
                    id: "",
                    name: "",
                    execNode: "",
                    status: "",
                    error: {
                        message: "",
                        type: ""
                    },
                    startTimeMillis: "",
                    durationMillis: "",
                    pauseDurationMillis: 0
                }],
                startTimeMillis: "",
                status: "",
                _links:
                    {
                        self: {
                            href: ""
                        }
                    }
            },
            stageDescription: [{
                durationMillis: "",
                error: {
                    message: "",
                    type: ""
                },
                execNode: "",
                id: "",
                name: "",
                pauseDurationMillis: "",
                stageFlowNodes: [{
                    _links: {
                        self: {
                            href: ""
                        },
                        log: {
                            href: ""
                        }
                    },
                    id: "",
                    name: "",
                    execNode: "",
                    status: "",
                    error: {
                        message: "",
                        type: ""
                    },
                    parameterDescription: "",
                    startTimeMillis: "",
                    durationMillis: "",
                    pauseDurationMillis: "",
                    parentNodes: []
                }],

                length: "",
                status: "",
                _links: {
                    self: {
                        href: ""
                    }
                },
                startTimeMillis: "",
            }]
        }
    }

    componentWillMount() {
        // console.log(this.props.match.params.id)
        this.getPipelineInfoById(this.props.match.params.id)
    }

    /**
     * 跳转到编辑页面
     * */
    goToEdit() {
        this.props.history.push("/pipeline/edit/" + this.props.match.params.id)
    }

    /**
     * get PipelineInfo By id
     * */
    getPipelineInfoById(id) {
        let url = API.pipeline + "/pipeline/findById?id=" + id;
        let self = this;
        Axios.get(url).then((response) => {
            console.log(response);
            if (response.status === 200) {
                console.log(111);
                self.setState({
                    pipelineInfo: response.data
                });
            }
        })
    }

    /**
     * 查看stage
     * */
    viewStage(index, name) {
        this.setState({
            currentStageTab: index,
            currentStageName: name
        })
    }

    /**
     * build pipeline
     * */
    buildPipeline() {
        let url = API.pipeline + '/pipeline/jenkins/build';
        let self = this;
        let pipelineInfo = this.state.pipelineInfo;
        delete pipelineInfo.id;
        delete pipelineInfo.admin;
        pipelineInfo.admin = [{
            id: 1,
            name: 'test'
        }];
        pipelineInfo.stage.map((item, index) => {
            delete item.id
        });
        console.log(pipelineInfo);
        Axios({
            method: 'post',
            url: url,
            data: pipelineInfo
        }).then((response) => {
            console.log(response);
            self.getJenkinsRuns();
        })
    }


    getJenkinsRuns() {
        let url = API.jenkins +
            '/job/' +
            this.state.pipelineInfo.name + '_' + this.state.pipelineInfo.createTime + '_' + this.state.pipelineInfo.creator +
            '/wfapi/runs';
        // let url = "http://jenkins.dop.clsaa.com/job/simple-java-maven-app/wfapi/runs"
        let self = this;
        self.setState({
            stageDescription: []
        });
        Axios.get(url).then((response) => {
            if (response.data) {
                self.setState({
                    jenkinsRuns: response.data[0]
                });
                self.state.jenkinsRuns.stages.map((item, index) => {
                    self.getStageDescribe(index);
                });
            }
        })

    }

    getStageDescribe(index) {
        let url = API.jenkins + this.state.jenkinsRuns.stages[index]._links.self.href;
        let self = this;
        let stageDescription = self.state.stageDescription;
        Axios.get(url).then((response) => {
            if (response.data) {
                stageDescription.push(response.data);
                self.setState({
                    stageDescription
                });
            }
        });
    }

    getStageLog(index) {
        let self = this;
        self.setState({
            output: ""
        });
        let text = "";
        console.log(self.state.stageDescription);
        self.state.stageDescription[index].stageFlowNodes.map((item, index) => {
            if (item.name === 'Shell Script') {
                let url = API.jenkins + item._links.log.href;
                console.log(url);
                Axios.get(url).then((response) => {
                    if (response.data) {
                        console.log(response.data.text)
                        response.data.text = response.data.text.replace(/&amp;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&quot;/g, "\"").replace(/&apos;/g, "\'");
                        text += response.data.text;
                        self.setState({
                            output: text
                        })
                    }
                })
            }
        })
    }

    render() {
        return (
            <div className="body">
                <div className="operate">
                    <Button type="primary" className="button" onClick={this.buildPipeline.bind(this)}>
                        <Icon type="play"/>
                        运行流水线
                    </Button>
                    <Button type="normal" className="button" onClick={this.goToEdit.bind(this)}>
                        <Icon type="edit"/>
                        编辑流水线
                    </Button>
                    <Button type="secondary" shape="warning" className="button">
                        <Icon type="ashbin"/>
                        删除流水线
                    </Button>
                </div>
                <div className="step">
                    {/*{this.props.match.params.id}*/}
                    {(() => {
                        if (this.state.pipelineInfo.stage.length === 0) {
                            return (
                                <div className="no-result">
                                    无记录
                                </div>
                            )
                        } else {
                            return (
                                this.state.pipelineInfo.stage.map((item, index) => {
                                    let stageClass = this.state.currentStageTab === index ? "have-result have-result-active" : "have-result";
                                    const successPrefix = <Icon type="select" size="small"
                                                                style={{color: "green"}}/>;
                                    const errorPrefix = <Icon type="close" size="small" style={{color: "red"}}/>;
                                    return (
                                        <div className="have-result-wrap" key={index}
                                             onClick={this.getStageLog.bind(this, index)}>
                                            <div className="line"></div>
                                            <div className={stageClass} key={index}
                                                 onClick={this.viewStage.bind(this, index, item.name)}>
                                                <p className="title">{item.name}</p>
                                                {(() => {
                                                    if (this.state.jenkinsRuns.stages.length > 1) {
                                                        if (this.state.jenkinsRuns.stages[index].status === "FAILED") {
                                                            return (
                                                                <Progress
                                                                    className="progress"
                                                                    percent={100}
                                                                    shape="circle"
                                                                    type="progressive"
                                                                    suffix={errorPrefix}
                                                                />
                                                            )
                                                        } else {
                                                            return (
                                                                <Progress
                                                                    className="progress"
                                                                    percent={100}
                                                                    shape="circle"
                                                                    type="progressive"
                                                                    suffix={successPrefix}
                                                                />
                                                            )
                                                        }
                                                    } else {
                                                        return (
                                                            <Progress
                                                                className="progress"
                                                                percent={0}
                                                                shape="circle"
                                                                type="progressive"
                                                            />
                                                        )
                                                    }
                                                })()}

                                            </div>
                                        </div>
                                    )
                                })
                            )
                        }
                    })()}
                </div>
                {(() => {
                    if (this.state.currentStageTab !== -1) {
                        return (
                            <div className="console">
                                {this.state.output}
                            </div>
                        )
                    }
                })()}

            </div>
        );
    }
}
