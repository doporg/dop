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
                cuser: "1",
                //监听设置
                monitor: "",
                stages: []
            },
            monitor: ["自动触发", "手动触发"],

        };
    }

    componentWillMount() {
        this.getPipeline()
    }

    getPipeline() {
        let url = API.pipeline + '/v1/pipeline/byId?id=' + this.state.pipelineId;
        let self = this;
        self.setState({
            visible: true
        });
        Axios.get(url).then((response) => {
            console.log(response)
            if (response.status === 200) {
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

    stages(value) {
        let pipeline = Object.assign({}, this.state.pipeline, {stages: value});
        this.setState({pipeline})
    }

    save() {
        let self = this;
        let url = API.pipeline + '/v1/pipeline/update';
        console.log(self.state.pipeline)
        Axios({
            method: 'put',
            url: url,
            data: self.state.pipeline
        }).then((response) => {
            if (response.status === 200) {
                toast.show({
                    type: "success",
                    content: "保存成功",
                    duration: 1000
                });
                self.props.history.push('/pipeline')
            }
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
                                    <Input placeholder="请输入流水线的名称"/>
                                </FormBinder>
                                <FormError className="form-item-error" name="name"/>
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
                                <PipelineInfoStage stages={this.state.pipeline.stages}
                                                   currentStage={0}
                                                   onChange={this.stages.bind(this)}/>
                            </div>

                        </div>
                    </FormBinderWrapper>
                </Loading>


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
