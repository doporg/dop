/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input, Button, Select, Step, Icon} from '@icedesign/base';
import IceLabel from '@icedesign/label';
import {Link} from 'react-router-dom';
import jsonp from "jsonp";
import './PipelineInfo.scss';
import Axios from 'axios';
import API from '../../API';

const {Combobox} = Select;

const fatchAdmin = (admin) => {
    return admin.map((item, index) => {
        console.log(index);
        return {
            id: `${index}`,
            name: `test${index}`
        };
    });
}

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
            task: ["构建maven", "构建docker镜像", "推送docker镜像"],
            //所选的task
            chosenTask: "",

        };
    }

    componentWillMount() {
        if (this.props.match.params.id) {
            console.log(this.props.match.params.id)
            this.getPipelineInfoById(this.props.match.params.id)
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
            fatchAdmin(response.data.admin);
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
     * 选择管理员更新到视图
     * */
    onSelectAdminUpdate(value) {
        if (this.searchTimeout) {
            clearTimeout(this.searchTimeout);
        }
        this.searchTimeout = setTimeout(() => {
            jsonp(
                `https://suggest.taobao.com/sug?code=utf-8&q=${value}`,
                (err, data) => {
                    const teamMember = data.result.map(item => {
                        return {
                            label: item[0],
                            value: item[1]
                        };
                    });
                    this.setState({
                        teamMember
                    });
                }
            );
        }, 100);
    }

    /**
     * 选择管理员更新数据
     * */
    selectAdmin(value) {
        let pipelineInfo = Object.assign({}, this.state.pipelineInfo, {admin: value});
        this.setState({
            pipelineInfo
        });
    }

    /**
     * 自定义管理员
     * */
    selectValueRender = v => {
        return `${v.name}`;
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
            case "构建maven":
                newTask = {
                    taskName: "构建maven",
                    gitUrl: "",
                    dockerUserName: "",
                    repository: "",
                    description: ""
                };
                break;
            case "构建docker镜像":
                newTask = {
                    taskName: "构建docker镜像",
                    gitUrl: "",
                    dockerUserName: "",
                    repository: "",
                    description: ""
                };
                break;
            case "推送docker镜像":
                newTask = {
                    taskName: "推送docker镜像",
                    gitUrl: "",
                    dockerUserName: "",
                    repository: "",
                    description: ""
                };
                break;
        }
        let currentStage = this.state.currentStage;
        let findIndex = currentStage.tasks.findIndex((item) => {
            return item.taskName == newTask.taskName
        });
        if (findIndex == -1) {
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
        this.setState({
            test: value
        });
        let findIndex = this.state.currentStage.tasks.findIndex((item) => {
            return item.taskName === this.state.chosenTask.taskName
        });
        this.state.currentStage.tasks[findIndex].gitUrl = value
    }

    /**
     *  removeById
     * */
    removeById(id) {
        let data = {id: id};
        let url = API.pipeline + '/pipeline/remove';
        let self = this;
        Axios.post(
            url,
            data
        ).then((response) => {
            // self.save()
        })
    }

    /**
     *  提交
     * */
    save() {
        let pipelineInfo = Object.assign({}, this.state.pipelineInfo, {createTime: new Date().getTime().toString()});
        let url = API.pipeline + '/pipeline/save';
        if (!this.state.newPipeline) {
            this.removeById(this.props.match.params.id);

            //预处理一下数据
            delete pipelineInfo.id;
            delete pipelineInfo.admin;
            pipelineInfo.admin = [{
                id: 1,
                name: 'test'
            }];
            pipelineInfo.stage.map((item,index)=>{
                delete item.id
            })
        }

        /**
         *  检查数据
         *  stageName不能一样
         *
         * */

        setTimeout(() => {
            this.setState({
                pipelineInfo
            });
            Axios({
                method: 'post',
                url: url,
                data: pipelineInfo,
            }).then((response) => {
                console.log(response)
            }).catch((error) => {
                console.log(error)
            })
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
            <div className="body">
                <FormBinderWrapper
                    value={this.state.pipelineInfo}
                    onChange={this.formChange}
                    ref="form"
                >
                    <div className="form-body">
                        <div className="form-item">
                            <span className="form-item-label">流水线名称: </span>
                            <FormBinder name="name" required message="请输入流水线的名称">
                                <Input placeholder="请输入流水线的名称"/>
                            </FormBinder>
                            <FormError className="form-item-error" name="name"/>
                        </div>
                        <div className="form-item">
                            <span className="form-item-label">管理员: </span>
                            {/*有bug需改进*/}
                            <Combobox
                                onInputUpdate={this.onSelectAdminUpdate.bind(this)}
                                filterLocal={false}
                                value={this.state.pipelineInfo.admin}
                                fillProps="name"
                                multiple
                                placeholder="请输入项目组成员"
                                onChange={this.selectAdmin.bind(this)}
                                dataSource={this.state.teamMember}
                            />
                            <FormError className="form-itemError" name="admin"/>
                        </div>
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
                            <Step className="step" type="circle" animation={true}
                                  current={this.state.currentStageOrder}
                            >
                                {this.state.pipelineInfo.stage.map((item, index) => {
                                    return (
                                        <Step.Item key={index} title={item.name}
                                                   itemRender={this.stepItemRender}
                                                   className="stepItem"
                                                   onClick={this.changeStage.bind(this, index)}
                                        />

                                    )
                                })}

                                <Step.Item key={this.state.pipelineInfo.stage.length} title="添加"
                                           itemRender={this.stepItemAdd.bind(this)}
                                           className="stepItem"
                                           onClick={this.addStage.bind(this, this.state.pipelineInfo.stage.length)}
                                />
                            </Step>
                        </div>

                    </div>
                </FormBinderWrapper>

                {(() => {
                    if (this.state.currentStage !== undefined ) {
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
                                                if (this.state.chosenTask == "") {
                                                    className = "chosen-task-detail hide"
                                                } else {
                                                    className = "chosen-task-detail"
                                                }
                                                return (
                                                    <div className={className}>
                                                        {
                                                            (() => {
                                                                switch (this.state.chosenTask.taskName) {
                                                                    case "构建maven":
                                                                        return (
                                                                            <div>
                                                                                <h3 className="chosen-task-detail-title">构建maven</h3>
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
                                                                        break;
                                                                    case "构建docker镜像":
                                                                        return (
                                                                            <div>
                                                                                构建docker镜像
                                                                            </div>
                                                                        );
                                                                        break;
                                                                    case "推送docker镜像":
                                                                        return (
                                                                            <div>
                                                                                推送docker镜像
                                                                            </div>
                                                                        );
                                                                        break;

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
