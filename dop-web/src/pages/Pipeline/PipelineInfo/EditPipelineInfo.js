/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input, Button, Select, Loading} from '@icedesign/base';
import {Link} from 'react-router-dom';
import PipelineInfoStage from '../components/PipelineInfoStage'
import Jenkinsfile from '../components/Jenkinsfile'
import './PipelineInfo.scss';
import Axios from 'axios';
import API from '../../API';
import {Feedback} from "@icedesign/base/index";

const {Combobox} = Select;
const {toast} = Feedback;

export default class EditPipelineInfo extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // 流水线的基本信息
            pipelineId: this.props.match.params.id,
            visible: false,
            pipeline: {
                name: "",
                cuser: window.sessionStorage.getItem('user-id'),
                //监听设置
                monitor: "",
                config:"",
                stages: [],
                jenkinsfile:{}
            },
            monitor: ["自动触发", "手动触发"],
            jenkinsFile: ["自带Jenkinsfile", "无Jenkinsfile"],
            haveJenkinsFile: null,
            jenkinsFileInfo: {
                git: "",
                path: ""
            }

        };
    }

    componentWillMount() {
        this.getPipeline()
    }

    getPipeline() {
        let url = API.pipeline + '/v1/pipeline/' + this.state.pipelineId;
        let self = this;
        self.setState({
            visible: true
        });
        Axios.get(url).then((response) => {
            console.log(response)
            if (response.status === 200) {
                if(response.data.stages === null){
                    response.data.stages = []
                }
                if(response.data.jenkinsfile === null){
                    response.data.jenkinsfile = {
                        git: "",
                        path: ""
                    }
                }
                self.setState({
                    pipeline: response.data,
                    visible: false
                });
            }else{
                toast.show({
                    type: "error",
                    content: "获取信息失败",
                    duration: 1000
                });
            }
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
        let pipeline = Object.assign({}, this.state.pipeline, {monitor: value});
        this.setState({
            pipeline
        });
    }

    selectJenkinsFile(value) {
        this.setState({
            haveJenkinsFile: value
        })
    }

    jenkinsFileData(jenkinsFileInfo) {
        this.setState({
            jenkinsFileInfo
        });
    }

    setStages(value, currentStage) {
        let pipeline = Object.assign({}, this.state.pipeline, {stages: value});
        this.setState({
            pipeline: pipeline,
            currentStage: currentStage
        })
    }

    saveJenkinsfile() {
        let self = this;
        let url = API.pipeline + '/v1/pipeline/update';
        Axios({
            method: 'put',
            url: url,
            data: self.state.pipeline
        }).then((response) => {
            if (response.status === 200) {
                self.deleteJenkins.bind(self).then(()=>{
                    self.createJenkinsfile.bind(self)
                }).then(()=>{
                    toast.show({
                        type: "success",
                        content: "保存成功",
                        duration: 1000
                    });
                    self.props.history.push('/pipeline')
                })
            }
        }).catch(()=>{
            toast.show({
                type: "error",
                content: "请检查您的路径",
                duration: 1000
            });
        })
    }

    save() {
        let self = this;
        let url = API.pipeline + '/v1/pipeline/update';
        Axios({
            method: 'put',
            url: url,
            data: self.state.pipeline
        }).then((response) => {
            if (response.status === 200) {
                self.deleteJenkins().then(()=>{
                    self.createJenkins()
                }).then(()=>{
                    toast.show({
                        type: "success",
                        content: "保存成功",
                        duration: 1000
                    });
                    self.props.history.push('/pipeline')
                })
            }
        })
    }
    createJenkins(){
        let url = API.pipeline + '/v1/jenkins';
        let data = this.state.pipeline;
        return new Promise((resolve)=>{
            Axios.post(url, data).then((response) => {
                if (response.status === 200) {
                    resolve()
                }
            });
        })
    }
    createJenkinsfile(){
        let url = API.pipeline + '/v1/jenkins/jenkinsfile';
        let data = this.state.pipeline;
        return new Promise((resolve)=>{
            Axios.post(url, data).then((response) => {
                if (response.status === 200) {
                    resolve()
                }
            });
        })
    }

    deleteJenkins(){
        let url = API.pipeline + '/v1/jenkins/' + this.state.pipelineId;
        return new Promise((resolve, reject)=>{
            Axios.delete(url).then((response) => {
                if (response.status === 200) {
                    resolve()
                }
            });
        })
    }


    render() {
        return (
            <div className="pipeline-info-body">

                <Loading shape="fusion-reactor" visible={this.state.visible} className="next-loading my-loading">
                    <FormBinderWrapper
                        value={this.state.pipeline}
                        onChange={this.formChange}
                        ref="form"
                    >
                        <div className="form-body">
                            <div className="form-item">
                                <span className="form-item-label">流水线名称: </span>
                                <FormBinder name="name" required message="请输入流水线的名称">
                                    <Input placeholder="请输入流水线的名称" className="combobox"/>
                                </FormBinder>
                                <FormError className="form-item-error" name="name"/>
                            </div>
                            <div className="form-item">
                                <span className="form-item-label">监听设置: </span>
                                <FormBinder name="monitor" required message="请选择监听设置">
                                    <Combobox
                                        onChange={this.selectMonitor.bind(this)}
                                        dataSource={this.state.monitor}
                                        placeholder={this.state.pipeline.monitor}
                                        className="combobox"
                                    >
                                    </Combobox>
                                </FormBinder>
                                <FormError className="form-item-error" name="monitor"/>
                            </div>

                            <div className="form-item">
                                <span className="form-item-label">配置流水线: </span>
                                <FormBinder name="config" required message="配置设置">
                                    <Combobox
                                        onChange={this.selectJenkinsFile.bind(this)}
                                        dataSource={this.state.jenkinsFile}
                                        placeholder={this.state.pipeline.config}
                                        className="combobox"
                                    >
                                    </Combobox>
                                </FormBinder>
                                <FormError className="form-item-error" name="config"/>
                            </div>

                            {(() => {
                                if (this.state.pipeline.config === '自带Jenkinsfile') {
                                    return (
                                        <Jenkinsfile
                                            jenkinsfile={this.state.pipeline.jenkinsfile}
                                            onChange={this.jenkinsFileData.bind(this)}
                                        />
                                    )
                                } else {
                                    return (
                                        <PipelineInfoStage
                                            stages={this.state.pipeline.stages}
                                            currentStage={this.state.currentStage}
                                            onChange={this.setStages.bind(this)}
                                        />
                                    )
                                }
                            })()}

                        </div>
                    </FormBinderWrapper>
                </Loading>


                <div className="footer">
                    <div className="footer-container">
                        <Button type="primary"
                                className="button"
                                onClick={
                                    this.state.haveJenkinsFile === "自带Jenkinsfile" ?
                                        this.saveJenkinsfile.bind(this) : this.save.bind(this)
                                }
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
