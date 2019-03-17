import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input, Select, Icon, Feedback} from '@icedesign/base';
import IceLabel from '@icedesign/label';
import './Styles.scss';

import Pull from './chosenSteps/Pull'

const {Combobox} = Select;
const {toast} = Feedback;

export default class PipelineInfoStep extends Component {
    constructor(props) {
        super(props);
        this.state = {
            stage: this.props.stage,
            availableSteps: ["拉取代码", "构建maven", "构建node", "构建docker镜像", "推送docker镜像", "自定义脚本"],
            steps: [],
            chosenStep: {
                taskName: ""
            }
        }
    }

    componentWillReceiveProps(nextProps) {
        let self = this;
        if (nextProps.stage) {
            self.setState({
                stage: nextProps.stage
            })
        }
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevState.stage !== this.state.stage) {
            this.props.onChange(this.state.stage)
        }
    }

    formChange(value) {
        this.props.onChange(value)
    };

    selectStep(value) {
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
                    shell: ""
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
                    shell: ""
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
                    shell: ""
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
                    shell: ""
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
                    shell: ""
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
                    shell: ""
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
                    shell: ""
                };
                break;
        }
        let steps = this.state.stage.steps;
        steps.push(newTask);
        let stage = Object.assign({}, this.state.stage, {steps: steps});
        this.setState({
            stage
        })
    }

    /**
     *  关掉所选task  label
     * */
    closeTask(index) {
        let stage = this.state.stage;
        stage.steps.splice(index, 1);
        this.setState({
            stage
        });
    }

    /**
     *  点击编辑task
     * */
    editTask(chosenStep) {
        console.log(chosenStep)
        this.setState({
            chosenStep
        })
    }

    gitUrl(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].gitUrl = value;
        this.setState({
            stage
        })
        console.log(stage)
    }

    render() {
        return (
            <FormBinderWrapper
                value={this.state.stage}
                onChange={this.formChange.bind(this)}
                ref="form"
            >
                <div className="pipeline-info-step">
                    <h3 className="header">阶段设置</h3>
                    <div>
                        <span className="label">名称: </span>
                        <FormBinder name="name" required message="请输入阶段的名称">
                            <Input
                                placeholder={this.state.stage.name}
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
                                onChange={this.selectStep.bind(this)}
                                dataSource={this.state.availableSteps}
                            />
                            {(() => {
                                if (this.state.stage && this.state.stage.steps) {
                                    return (this.state.stage.steps.map((item, index) => {
                                        let chosenStyle = item.taskName === this.state.chosenStep.taskName ? "chosen task-label" : "task-label";
                                        return (
                                            <div className="chosen-task" key={index}>
                                                <IceLabel className={chosenStyle}>
                                                <span
                                                    onClick={this.editTask.bind(this, item)}
                                                >{item.taskName}</span>
                                                    <Icon type="close" size="xs" className="close"
                                                          onClick={this.closeTask.bind(this, index)}
                                                    />
                                                </IceLabel>
                                            </div>
                                        )
                                    }))
                                }
                            })()}
                        </div>
                        {(() => {
                            if (this.state.chosenStep.taskName) {
                                return (
                                    <div className="chosen-task-detail">
                                        {(() => {
                                            switch (this.state.chosenStep.taskName) {
                                                case "拉取代码":
                                                    return (
                                                        <Pull onChange={this.gitUrl.bind(this)}/>
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
                                                                                                <span
                                                                                                    className="must">*</span>
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

                                        })()}
                                    </div>
                                )
                            }
                        })()}
                    </div>
                </div>
            </FormBinderWrapper>
        )
    }
}
