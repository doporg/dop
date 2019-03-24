import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input, Select, Icon} from '@icedesign/base';
import IceLabel from '@icedesign/label';
import './Styles.scss';

import Pull from './chosenSteps/Pull'
import Maven from './chosenSteps/Maven'
import Node from './chosenSteps/Node'
import DockerImage from './chosenSteps/DockerImage'
import PushDockerImage from './chosenSteps/PushDockerImage'
import Shell from './chosenSteps/Shell'

const {Combobox} = Select;

export default class PipelineInfoStep extends Component {
    constructor(props) {
        super(props);
        this.state = {
            stage: this.props.stage,
            availableSteps: ["拉取代码", "构建maven", "构建node", "构建docker镜像", "推送docker镜像", "自定义脚本"],
            steps: [],
            currentStep: null,
            chosenStep: {
                taskName: ""
            }
        }
    }

    componentWillReceiveProps(nextProps) {
        let self = this;
        if (this.props.currentStage !== nextProps.currentStage) {
            let chosenStep = {
                taskName: ""
            };
            self.setState({chosenStep})
        }
        self.setState({
            stage: nextProps.stage,
        })
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
        });
        this.props.onChange(this.state.stage)
    }

    buildDockerUserName(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].dockerUserName = value;
        this.setState({
            stage
        });
        this.props.onChange(this.state.stage)
    }

    buildRepository(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].repository = value;
        this.setState({
            stage
        });
        this.props.onChange(this.state.stage)
    }

    buildRepositoryVersion(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].repositoryVersion = value;
        this.setState({
            stage
        });
        this.props.onChange(this.state.stage)
    }

    buildDockerPassword(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].dockerPassword = value;
        this.setState({
            stage
        });
        this.props.onChange(this.state.stage)
    }

    shell(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].shell = value;
        this.setState({
            stage
        });
        this.props.onChange(this.state.stage)
    }

    render() {
        return (
            <div>
                {(() => {
                    if (this.state.stage) {
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
                                                value={this.state.stage.name}
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
                                                                        <Pull
                                                                            onChange={this.gitUrl.bind(this)}
                                                                            gitUrl={this.state.chosenStep.gitUrl}
                                                                        />
                                                                    );
                                                                case "构建maven":
                                                                    return (
                                                                        <Maven/>
                                                                    );
                                                                case "构建node":
                                                                    return (
                                                                        <Node/>
                                                                    );
                                                                case "构建docker镜像":
                                                                    return (
                                                                        <DockerImage
                                                                            onUserNameChange={this.buildDockerUserName.bind(this)}
                                                                            onRepositoryChange={this.buildRepository.bind(this)}
                                                                            onRepositoryVersionChange={this.buildRepositoryVersion.bind(this)}
                                                                            dockerUserName={this.state.chosenStep.dockerUserName}
                                                                            repository={this.state.chosenStep.repository}
                                                                            repositoryVersion={this.state.chosenStep.repositoryVersion}
                                                                        />
                                                                    );
                                                                case "推送docker镜像":
                                                                    return (
                                                                        <PushDockerImage
                                                                            onUserNameChange={this.buildDockerUserName.bind(this)}
                                                                            onRepositoryChange={this.buildRepository.bind(this)}
                                                                            onRepositoryVersionChange={this.buildRepositoryVersion.bind(this)}
                                                                            onDockerPasswordChange={this.buildDockerPassword.bind(this)}
                                                                            dockerUserName={this.state.chosenStep.dockerUserName}
                                                                            repository={this.state.chosenStep.repository}
                                                                            repositoryVersion={this.state.chosenStep.repositoryVersion}
                                                                            dockerPassword={this.state.chosenStep.dockerPassword}
                                                                        />
                                                                    );
                                                                case "自定义脚本":
                                                                    return (
                                                                        <Shell
                                                                            onShellChange={this.shell.bind(this)}
                                                                            shell={this.state.chosenStep.shell}
                                                                        />
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
                })()}
            </div>
        )
    }
}
