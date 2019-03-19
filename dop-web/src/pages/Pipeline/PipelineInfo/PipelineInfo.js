/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input, Button, Select} from '@icedesign/base';
import {Link} from 'react-router-dom';
import PipelineInfoStage from '../components/PipelineInfoStage'
import './PipelineInfo.scss';
import Axios from 'axios';
import API from '../../API';

const {Combobox} = Select;

export default class PipelineInfo extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // 流水线的基本信息
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
        let url = API.pipeline + '/v1/pipeline';
        Axios({
            method: 'post',
            url: url,
            data: self.state.pipeline
            // data: data
        }).then((response)=>{
            console.log(response)
        })
    }


    render() {
        return (
            <div className="pipeline-info-body">
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
                            <PipelineInfoStage stages={this.state.pipeline.stages} currentStage={0}
                                               onChange={this.stages.bind(this)}/>
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
