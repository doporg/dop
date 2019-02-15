/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {Input, Button, Select, Step, Icon} from '@icedesign/base';
import Axios from 'axios';
import API from '../../API';
import {Link} from 'react-router-dom';
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
            }
        };
    }

    componentWillMount() {
        // console.log(this.props.match.params.id)
        this.getPipelineInfoById(this.props.match.params.id)
    }

    /**
     * 跳转到编辑页面
     * */
    goToEdit(){
        this.props.history.push("/pipeline/edit/" + this.props.match.params.id)
    }

    /**
     * get PipelineInfo By id
     * */
    getPipelineInfoById(id){
        let url = API.pipeline + "/pipeline/findById?id=" + id;
        let self = this;
        Axios.get(url).then((response)=>{
            console.log(response);
            if(response.status === 200){
                console.log(111);
                self.setState({
                    pipelineInfo: response.data
                });
            }
        })
    }

    render() {
        return (
            <div className="body">
                <div className="operate">
                    <Button type="primary" className="button">
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
                    {this.props.match.params.id}
                    {(()=>{
                        console.log(this.state.pipelineInfo)
                        if(this.state.pipelineInfo.stage === []){
                            console.log(11111111111);
                            return(
                                <div>无记录</div>
                            )
                        }else{
                            return(
                                this.state.pipelineInfo.stage.map((item, index)=>{
                                    return(
                                        <div key={index}>
                                            item.name
                                        </div>
                                    )
                                })
                            )
                        }
                    })()}
                </div>
            </div>
        );
    }
}
