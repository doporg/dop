/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input, Button, Select, Step, Icon, Feedback} from '@icedesign/base';
import IceLabel from '@icedesign/label';
import {Link} from 'react-router-dom';
import jsonp from "jsonp";
import './PipelineInfo.scss';
import Axios from 'axios';
import API from '../../API';

const {Combobox} = Select;
const {toast} = Feedback;

export default class PipelineInfo extends Component {
    constructor(props) {
        super(props);
        this.state = {
            //判断是否为新建
            newPipeline: true,
            // 流水线的基本信息
            pipelineInfo: {
                name: "",
                creator: "test",
                //监听设置
                monitor: "",
                createTime: "",  //时间戳
                stage: []
            },

            // 获取到的项目组成员
            teamMember: [{
                id: 1,
                name: 'test'
            }],

            monitor: ["自动触发", "手动触发"],

            //当前编辑的stage
            currentStage: undefined,

            currentStageOrder: 0,

            //可选的任务
            task: ["拉取代码", "构建maven", "构建node", "构建docker镜像", "推送docker镜像", "自定义脚本"],
            myscript: ["shell"],
            //所选的task
            chosenTask: "",

        };
    }

    componentWillMount() {
        if (this.props.match.params.id) {
            this.getPipelineInfoById(this.props.match.params.id);
            this.setState({
                newPipeline: false
            })
        } else {
            console.log(this.state.currentStage)
        }
    }

    /**
     *  findById
     * */
    getPipelineInfoById(id) {
        let url = API.pipeline + "/pipeline/findById?id=" + id;
        let self = this;
        Axios.get(url).then((response) => {
            self.setState({
                pipelineInfo: response.data,
                currentStage: response.data.stage[0]
            });
        })
    }


    /**
     *  基本信息
     * */
    formChange = value => {
        this.setState({value});
    };
    /**
     *  currentStage form
     * */
    currentFormChange = value => {
        this.setState({value});
    };

    /**
     * 选择监听方式更新数据
     * */
    selectMonitor(value) {
        let pipelineInfo = Object.assign({}, this.state.pipelineInfo, {monitor: value});
        this.setState({
            pipelineInfo
        });
    }

    /**
     * step数字显示
     * */
    stepItemRender(index) {
        return index + 1
    }

    /**
     *  添加step 图标
     * */
    stepItemAdd() {
        return <Icon type="add"/>;
    }

    /**
     * 切换stage, 将新信息放到currentStage
     * */
    changeStage(currentStageOrder) {
        this.setState({
            currentStageOrder,
            chosenTask: ""
        });
        this.setState({
            currentStage: this.state.pipelineInfo.stage[currentStageOrder]
        });
    }

    /**
     *  添加stage
     * */
    addStage() {
        let newStage = {
            name: "请输入名称",
            tasks: []
        };
        this.state.pipelineInfo.stage.push(newStage);
        let stage = this.state.pipelineInfo.stage;
        let pipelineInfo = Object.assign({}, this.state.pipelineInfo, {stage: stage})
        this.setState({
            pipelineInfo
        });
        this.setState({
            currentStage: newStage
        });
        this.setState({
            currentStageOrder: this.state.pipelineInfo.stage.length
        });
    }

    /**
     *  关掉所选task  label
     * */
    closeTask(index) {
        let tempCurrent = this.state.currentStage;
        tempCurrent.tasks.splice(index, 1);
        this.setState({
            currentStage: tempCurrent
        });
    }

    /**
     *  挑选task
     * */
    selectTask(value) {
        let newTask;
        switch (value) {
            case "拉取代码" :
                newTask = {
                    taskName: "拉取代码",
                    gitUrl: "",
                    dockerUserName: "",
                    dockerPassword: "",
                    repository: "",
                    repositoryVersion: "",
                    description: ""
                };
                break;
            case "构建maven":
                newTask = {
                    taskName: "构建maven",
                    gitUrl: "",
                    dockerUserName: "",
                    dockerPassword: "",
                    repository: "",
                    repositoryVersion: "",
                    description: ""
                };
                break;
            case "构建node":
                newTask = {
                    taskName: "构建node",
                    gitUrl: "",
                    dockerUserName: "",
                    dockerPassword: "",
                    repository: "",
                    repositoryVersion: "",
                    description: ""
                };
                break;
            case "构建docker镜像":
                newTask = {
                    taskName: "构建docker镜像",
                    gitUrl: "",
                    dockerUserName: "",
                    dockerPassword: "",
                    repository: "",
                    repositoryVersion: "",
                    description: ""
                };
                break;
            case "推送docker镜像":
                newTask = {
                    taskName: "推送docker镜像",
                    gitUrl: "",
                    dockerUserName: "",
                    dockerPassword: "",
                    repository: "",
                    repositoryVersion: "",
                    description: ""
                };
                break;
            case "自定义脚本":
                newTask = {
                    taskName: "自定义脚本",
                    gitUrl: "",
                    dockerUserName: "",
                    dockerPassword: "",
                    repository: "",
                    repositoryVersion: "",
                    description: ""
                };
                break;
            default:
                newTask = {
                    taskName: "default",
                    gitUrl: "",
                    dockerUserName: "",
                    dockerPassword: "",
                    repository: "",
                    repositoryVersion: "",
                    description: ""
                };
                break;
        }
        let currentStage = this.state.currentStage;
        let findIndex = currentStage.tasks.findIndex((item) => {
            return item.taskName === newTask.taskName
        });
        if (findIndex === -1) {
            currentStage.tasks.push(newTask);
            this.setState({
                currentStage
            })
        }
    }

    /**
     *  点击编辑task
     * */
    editTask(chosenTask) {
        this.setState({
            chosenTask
        })
    }

    /**
     *  构建-填入git地址
     * */
    buildMavenGit(value) {
        let findIndex = this.state.currentStage.tasks.findIndex((item) => {
            return item.taskName === this.state.chosenTask.taskName
        });
        // this.state.currentStage.tasks[findIndex].gitUrl = value
        let currentStage = this.state.currentStage;
        currentStage.tasks[findIndex].gitUrl = value;
        this.setState({
            currentStage
        })

    }

    buildRepository(value) {
        let findIndex = this.state.currentStage.tasks.findIndex((item) => {
            return item.taskName === this.state.chosenTask.taskName
        });
        // this.state.currentStage.tasks[findIndex].repository = value
        let currentStage = this.state.currentStage;
        currentStage.tasks[findIndex].repository = value;
        this.setState({
            currentStage
        })
    }

    buildDockerUsrName(value) {
        let findIndex = this.state.currentStage.tasks.findIndex((item) => {
            return item.taskName === this.state.chosenTask.taskName
        });
        // this.state.currentStage.tasks[findIndex].dockerUserName = value
        let currentStage = this.state.currentStage;
        currentStage.tasks[findIndex].dockerUserName = value;
        this.setState({
            currentStage
        })
    }

    buildRepositoryVersion(value) {
        let findIndex = this.state.currentStage.tasks.findIndex((item) => {
            return item.taskName === this.state.chosenTask.taskName
        });
        // this.state.currentStage.tasks[findIndex].repositoryVersion = value
        let currentStage = this.state.currentStage;
        currentStage.tasks[findIndex].repositoryVersion = value;
        this.setState({
            currentStage
        })
    }

    buildDockerPassword(value) {
        let findIndex = this.state.currentStage.tasks.findIndex((item) => {
            return item.taskName === this.state.chosenTask.taskName
        });
        // this.state.currentStage.tasks[findIndex].dockerPassword = value
        let currentStage = this.state.currentStage;
        currentStage.tasks[findIndex].dockerPassword = value;
        this.setState({
            currentStage
        })
    }
    buildDescription(value){
        let findIndex = this.state.currentStage.tasks.findIndex((item) => {
            return item.taskName === this.state.chosenTask.taskName
        });
        // this.state.currentStage.tasks[findIndex].description = value
        let currentStage = this.state.currentStage;
        currentStage.tasks[findIndex].description = value;
        this.setState({
            currentStage
        })
    }

    /**
     *  removeByIdSQL
     * */
    removeByIdSQL(id) {
        let data = {id: id};
        let url = API.pipeline + '/pipeline/remove';
        Axios.post(
            url,
            data
        ).then((response) => {
            // self.save()
        })
    }

    deletePipeline(pipelineInfo) {
        console.log(pipelineInfo);
        let url = API.pipeline + '/pipeline/jenkins/deleteJob';
        let self = this;
        Axios({
            method: 'post',
            url: url,
            data: pipelineInfo
        }).then((response) => {
            console.log(response);
            if (response.data) {
                self.createPipeline(pipelineInfo);
            }
        })
    }

    createPipeline(pipelineInfo) {
        let urlPipeline = API.pipeline + '/pipeline/jenkins/createJob';
        let self = this;
        Axios({
            method: 'post',
            url: urlPipeline,
            data: pipelineInfo,
        }).then((response) => {
            if (response.data === "CreateJobSuccess") {
                toast.show({
                    type: "success",
                    content: "创建成功",
                    duration: 1000
                });
                self.props.history.push('/pipeline');
            }
        }).catch((error) => {
            toast.show({
                type: "error",
                content: error + ", 创建失败",
                duration: 1000
            });
        })
    }

    /**
     *  提交
     * */
    save() {
        let pipelineInfo = this.state.pipelineInfo;
        let urlSQL = API.pipeline + '/pipeline/save';
        let self = this;
        if (!this.state.newPipeline) {
            this.removeByIdSQL(this.props.match.params.id);
            //预处理一下数据
            delete pipelineInfo.id;
            this.deletePipeline(pipelineInfo);
        } else {
            //new pipeline add createTime
            pipelineInfo = Object.assign({}, this.state.pipelineInfo, {createTime: new Date().getTime().toString()});
            /**
             *  检查数据
             *  stageName不能一样
             *
             * */

        }
        setTimeout(() => {
            this.setState({
                pipelineInfo
            });
            // add SQL
            Axios({
                method: 'post',
                url: urlSQL,
                data: pipelineInfo,
            }).then((response) => {
                console.log(response)
                if (response.data === "success") {
                    self.createPipeline(pipelineInfo);
                }
            }).catch((error) => {
                toast.show({
                    type: "error",
                    content: error + ", 创建失败",
                    duration: 1000
                });
            });

        }, 0)
    };

    /**
     *  返回流水线主页
     * */
    backToMain() {
        this.push('/pipeline')
    }

    render() {
        return (
            <div className="pipeline-info-body">
                <FormBinderWrapper
                    value={this.state.pipelineInfo}
                    onChange={this.formChange}
                    ref="form"
                >
                    <div className="form-body">
                        {(() => {
                            if (this.state.newPipeline) {
                                return (
                                    <div className="form-item">
                                        <span className="form-item-label">流水线名称: </span>
                                        <FormBinder name="name" required message="请输入流水线的名称">
                                            <Input placeholder="请输入流水线的名称"/>
                                        </FormBinder>
                                        <FormError className="form-item-error" name="name"/>
                                    </div>
                                )
                            } else {
                                return (
                                    <div className="form-item">
                                        <span className="form-item-label">流水线名称: </span>
                                        <FormBinder name="name" required message="请输入流水线的名称">
                                            <Input placeholder="请输入流水线的名称" disabled/>
                                        </FormBinder>
                                        <FormError className="form-item-error" name="name"/>
                                    </div>
                                )
                            }
                        })()}
                        <div className="form-item">
                            <span className="form-item-label">监听设置: </span>
                            <FormBinder name="monitor" required message="请选择监听设置">
                                <Combobox
                                    onChange={this.selectMonitor.bind(this)}
                                    dataSource={this.state.monitor}
                                    placeholder="请选择监听设置"
                                >
                                </Combobox>
                            </FormBinder>
                            <FormError className="form-item-error" name="monitor"/>
                        </div>

                        <div className="step-wrapper">
                            <span className="label">配置流水线: </span>
                            <Step className="pipeline-info-step" type="circle" animation={true}
                                  current={this.state.currentStageOrder}
                            >
                                {this.state.pipelineInfo.stage.map((item, index) => {
                                    return (
                                        <Step.Item key={index} title={item.name}
                                                   itemRender={this.stepItemRender}
                                                   className="pipeline-info-stepItem"
                                                   onClick={this.changeStage.bind(this, index)}
                                        />

                                    )
                                })}

                                <Step.Item key={this.state.pipelineInfo.stage.length} title="添加"
                                           itemRender={this.stepItemAdd.bind(this)}
                                           className="pipeline-info-stepItem"
                                           onClick={this.addStage.bind(this, this.state.pipelineInfo.stage.length)}
                                />
                            </Step>
                        </div>

                    </div>
                </FormBinderWrapper>

                {(() => {
                    if (this.state.currentStage !== undefined) {
                        return (
                            <FormBinderWrapper
                                value={this.state.currentStage}
                                onChange={this.currentFormChange}
                                ref="currentForm"
                            >
                                <div className="step-item-detail">
                                    <h3 className="header">阶段设置</h3>
                                    <div>
                                        <span className="label">名称: </span>
                                        <FormBinder name="name" required message="请输入阶段的名称">
                                            <Input
                                                placeholder={this.state.currentStage.name}
                                            />
                                        </FormBinder>
                                        <FormError className="form-item-error" name="name"/>
                                    </div>
                                    <div className="task">
                                        <span className="task-label-set">
                                            任务设置:
                                            <p>*请注意任务顺序</p>
                                        </span>
                                        <div className="choose-task">
                                            <Combobox
                                                filterLocal={false}
                                                placeholder="请选择任务"
                                                onChange={this.selectTask.bind(this)}
                                                dataSource={this.state.task}
                                            />
                                            {
                                                this.state.currentStage.tasks.map((item, index) => {
                                                    let chosenStyle = item.taskName === this.state.chosenTask.taskName ? "chosen task-label" : "task-label";
                                                    return (
                                                        <div className="chosen-task" key={index}>
                                                            <IceLabel className={chosenStyle}>
                                                    <span
                                                        onClick={this.editTask.bind(this, item)}
                                                    >{item.taskName}</span>
                                                                <Icon type="close" size="xs" className="close"
                                                                      onClick={this.closeTask.bind(this, index)}
                                                                ></Icon>
                                                            </IceLabel>
                                                        </div>
                                                    )
                                                })}
                                        </div>
                                        {
                                            (() => {
                                                let className;
                                                if (this.state.chosenTask === "") {
                                                    className = "chosen-task-detail hide"
                                                } else {
                                                    className = "chosen-task-detail"
                                                }
                                                return (
                                                    <div className={className}>
                                                        {
                                                            (() => {
                                                                switch (this.state.chosenTask.taskName) {
                                                                    case "拉取代码":
                                                                        return (
                                                                            <div>
                                                                                <h3 className="chosen-task-detail-title">拉取代码</h3>
                                                                                <div
                                                                                    className="chosen-task-detail-body">
                                                                                    <span className="item">
                                                                                        <span className="must">*</span>
                                                                                        <span>Git地址: </span>
                                                                                    </span>
                                                                                    <Input
                                                                                        onChange={this.buildMavenGit.bind(this)}
                                                                                        className="input"
                                                                                        placeholder={this.state.chosenTask.gitUrl}
                                                                                    />
                                                                                </div>
                                                                            </div>
                                                                        );
                                                                    case "构建maven":
                                                                        return (
                                                                            <div>
                                                                                <h3 className="chosen-task-detail-title">构建maven</h3>
                                                                                <div
                                                                                    className="chosen-task-detail-body">
                                                                                    默认执行 <br/>
                                                                                    'mvn --version' <br/>
                                                                                    'mvn clean package
                                                                                    -Dmaven.test.skip=true' <br/>
                                                                                    'mvn package' <br/>
                                                                                </div>

                                                                            </div>
                                                                        );
                                                                    case "构建node":
                                                                        return (
                                                                            <div>
                                                                                <h3 className="chosen-task-detail-title">构建node</h3>
                                                                                <div
                                                                                    className="chosen-task-detail-body">
                                                                                    默认执行 <br/>
                                                                                    'npm --version' <br/>
                                                                                    'node --version' <br/>
                                                                                    'npm install' <br/>
                                                                                </div>
                                                                            </div>
                                                                        );
                                                                    case "构建docker镜像":
                                                                        return (
                                                                            <div>
                                                                                <h3 className="chosen-task-detail-title">构建docker镜像</h3>
                                                                                <div
                                                                                    className="chosen-task-detail-body">
                                                                                    <span className="item">
                                                                                        <span className="must">*</span>
                                                                                        <span>DockerUserName: </span>
                                                                                    </span>
                                                                                    <Input
                                                                                        onChange={this.buildDockerUsrName.bind(this)}
                                                                                        className="input"
                                                                                        placeholder={this.state.chosenTask.dockerUserName}
                                                                                    />
                                                                                    <span className="item">
                                                                                        <span className="must">*</span>
                                                                                        <span>Repository: </span>
                                                                                    </span>
                                                                                    <Input
                                                                                        onChange={this.buildRepository.bind(this)}
                                                                                        className="input"
                                                                                        placeholder={this.state.chosenTask.repository}
                                                                                    />
                                                                                    <span className="item">
                                                                                        <span className="must">*</span>
                                                                                        <span>Version: </span>
                                                                                    </span>
                                                                                    <Input
                                                                                        onChange={this.buildRepositoryVersion.bind(this)}
                                                                                        className="input"
                                                                                        placeholder={this.state.chosenTask.repositoryVersion}
                                                                                    />
                                                                                </div>


                                                                            </div>
                                                                        );
                                                                    case "推送docker镜像":
                                                                        return (
                                                                            <div>
                                                                                <div>
                                                                                    <h3 className="chosen-task-detail-title">构建docker镜像</h3>
                                                                                    <div
                                                                                        className="chosen-task-detail-body">
                                                                                        <span className="item">
                                                                                            <span
                                                                                                className="must">*</span>
                                                                                            <span>DockerUserName: </span>
                                                                                        </span>
                                                                                        <Input
                                                                                            onChange={this.buildDockerUsrName.bind(this)}
                                                                                            className="input"
                                                                                            placeholder={this.state.chosenTask.dockerUserName}
                                                                                        />
                                                                                        <span className="item">
                                                                                            <span
                                                                                                className="must">*</span>
                                                                                            <span>Repository: </span>
                                                                                        </span>
                                                                                        <Input
                                                                                            onChange={this.buildRepository.bind(this)}
                                                                                            className="input"
                                                                                            placeholder={this.state.chosenTask.repository}
                                                                                        />
                                                                                        <span className="item">
                                                                                            <span
                                                                                                className="must">*</span>
                                                                                            <span>Version: </span>
                                                                                        </span>
                                                                                        <Input
                                                                                            onChange={this.buildRepositoryVersion.bind(this)}
                                                                                            className="input"
                                                                                            placeholder={this.state.chosenTask.repositoryVersion}
                                                                                        />
                                                                                        <span className="item">
                                                                                            <span
                                                                                                className="must">*</span>
                                                                                            <span>DockerPassWord: </span>
                                                                                        </span>
                                                                                        <Input
                                                                                            onChange={this.buildDockerPassword.bind(this)}
                                                                                            className="input"
                                                                                            placeholder={this.state.chosenTask.dockerPassword}
                                                                                            htmlType="password"
                                                                                        />
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        );
                                                                    case "自定义脚本":
                                                                        return (
                                                                            <div>
                                                                                <div>
                                                                                    <h3 className="chosen-task-detail-title">自定义脚本</h3>
                                                                                    <div
                                                                                        className="chosen-task-detail-body">
                                                                                        <span className="item">
                                                                                            <span
                                                                                                className="must">*</span>
                                                                                            <span>脚本类型: </span>
                                                                                        </span>
                                                                                        <Combobox
                                                                                            className="input"
                                                                                            filterLocal={false}
                                                                                            placeholder="请脚本类型"
                                                                                            dataSource={this.state.myscript}
                                                                                        /> <br/>


                                                                                        <div className="textarea">
                                                                                            <div className="title">
                                                                                                <span className="must">*</span>
                                                                                                脚本内容:
                                                                                            </div>
                                                                                            <div className="area-wrap">
                                                                                                <Input
                                                                                                    multiple
                                                                                                    placeholder=""
                                                                                                    className="area"
                                                                                                    onChange={this.buildDescription.bind(this)}
                                                                                                />
                                                                                            </div>
                                                                                        </div>

                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        );
                                                                    default:

                                                                }
                                                            })()
                                                        }
                                                    </div>
                                                )
                                            })()
                                        }

                                    </div>

                                </div>
                            </FormBinderWrapper>)
                    }
                })()}


                <div className="footer">
                    <div className="footer-container">
                        <Button type="primary"
                                className="button"
                                onClick={this.save.bind(this)}
                        >保存</Button>
                        <Link to='/pipeline'>
                            <Button type="normal"
                                    className="button"
                            >取消</Button>
                        </Link>

                    </div>
                </div>
            </div>
        );
    }
}
