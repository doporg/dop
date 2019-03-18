/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input, Button, Select, Feedback} from '@icedesign/base';
import {Link} from 'react-router-dom';
import PipelineInfoStage from '../components/PipelineInfoStage'
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
            pipeline: {
                name: "",
                cuser: "test",
                //监听设置
                monitor: "",
                stages: []
            },
            monitor: ["自动触发", "手动触发"],

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
     * 选择监听方式更新数据
     * */
    selectMonitor(value) {
        let pipelineInfo = Object.assign({}, this.state.pipelineInfo, {monitor: value});
        this.setState({
            pipelineInfo
        });
    }

    stages(value){
        console.log(value)
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
        let urlSQL = API.pipeline + '/v1/pipeline';
        if (!this.state.newPipeline) {
            this.removeByIdSQL(this.props.match.params.id);
            //预处理一下数据
            delete pipelineInfo.id;
            this.deletePipeline(pipelineInfo);
        }
        setTimeout(() => {

            // add SQL
            Axios({
                method: 'post',
                url: urlSQL,
                data: pipelineInfo,
            }).then((response) => {
                console.log(response);
                // if (response.data === "success") {
                //     self.createPipeline(pipelineInfo);
                // }
            }).catch((error) => {
                toast.show({
                    type: "error",
                    content: error + ", 创建失败",
                    duration: 1000
                });
            });

        }, 0)
    };


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
                            <PipelineInfoStage stages={this.state.pipeline.stages} currentStage={0} onChange={this.stages}/>
                        </div>

                    </div>
                </FormBinderWrapper>




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
