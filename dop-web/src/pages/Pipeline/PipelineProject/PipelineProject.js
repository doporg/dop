/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {Button, Icon} from '@icedesign/base';
import {Loading, Form, Input} from '@alifd/next';
import Axios from 'axios';
import API from '../../API';
import Iframe from '../components/Iframe'
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
            iframeSrc: "",
            visible: true,
        }
    }

    componentWillMount() {
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
                self.setState({
                    pipelineInfo: response.data
                });
                self.getRun(response.data.name + "_" + response.data.createTime + "_" + response.data.creator)
            }
        })
    }

    /**
     * build pipeline
     * */
    buildPipeline() {
        let url = API.pipeline + '/pipeline/jenkins/build';
        let self = this;
        self.setState({visible: true})
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
        Axios({
            method: 'post',
            url: url,
            data: pipelineInfo
        }).then((response) => {
            console.log(response);
            if (response.data === "BuildSuccess") {
                self.getRun(pipelineInfo.name + "_" + pipelineInfo.createTime + "_" + pipelineInfo.creator);
            }
        })
    }

    getRun(name) {
        let url = API.jenkins + '/blue/rest/organizations/jenkins/pipelines/' + name + '/runs';
        let self = this;
        Axios.get(url).then((response) => {
            console.log(response);
            if (response.data) {
                let iframeSrc = API.jenkins +
                    "/blue/organizations/jenkins/" +
                    name + "/detail/" + name + "/" +
                    response.data.length + "/pipeline";
                self.setState({
                    iframeSrc: iframeSrc
                });
                console.log(iframeSrc)
            }
        })
    }

    onLoad(visible) {
        let self = this;
        setTimeout(() => {
            self.setState({
                visible,
            })
        }, 6000)
        console.log(111)

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
                <div className="iframe">
                    <Iframe src={this.state.iframeSrc} onLoad={visible => this.onLoad(visible)}/>
                </div>


            </div>
        );
    }
}
