/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import {Button, Icon, Loading, Feedback} from '@icedesign/base';
import Axios from 'axios';
import API from '../../API';
import RunResult from './RunResult'
import './PipelineProject.scss'
const {toast} = Feedback;

export default class PipelineProject extends Component {
    constructor(props) {
        super(props);
        this.state = {
            visible: false,
            pipelineId: this.props.match.params.id,
            runs: {}
        }
    }
    componentDidMount() {
        let self = this;
        this.getRuns().then((data)=>{
            self.setState({
                runs: data
            })
        })
    }

    getRuns(){
        // let url = API.jenkinsRest + id + '/runs/';
        let url = 'http://jenkins.dop.clsaa.com/blue/rest/organizations/jenkins/pipelines/simple-node-app/runs/'
        return new Promise((resolve, reject)=>{
            Axios({
                method: 'get',
                url: url,
                headers:{
                    'Authorization': 'Basic emZsOnpmbA=='
                }
            }).then((response)=>{
                if(response.status === 200){
                    if(response.data.length === 0){
                        toast.show({
                            type: "prompt",
                            content: "该流水线尚未运行",
                            duration: 3000
                        });
                    }else{
                        resolve(response.data[0])
                    }
                }
                reject()
            })
        })
    }

    buildPipeline(){
        console.log('click')
        this.setState({
            runs: '111111'
        });
        let url = API.jenkinsRest + this.state.pipelineId + '/runs/';
        let self = this;
        Axios({
            method: 'post',
            url: url,
            headers:{
                'content-type': 'application/json;charset=UTF-8',
                'Authorization': 'Basic emZsOnpmbA=='
            }
        }).then((response)=>{
            console.log(response)
            if(response.status === 200){
                self.setState({
                    runs: response.data
                })
            }
        })

    }
    render() {
        return (
            <div className="body">
                <Loading shape="fusion-reactor" visible={this.state.visible} className="next-loading my-loading">
                    <div className="operate">
                        <Button type="primary" className="button" onClick={this.buildPipeline.bind(this)}>
                            <Icon type="play"/>
                            运行流水线
                        </Button>
                        <Button type="normal" className="button" >
                            <Icon type="edit"/>
                            编辑流水线
                        </Button>
                        <Button type="secondary" shape="warning" className="button">
                            <Icon type="ashbin"/>
                            删除流水线
                        </Button>
                    </div>

                    <RunResult runs={this.state.runs}/>
                </Loading>
            </div>

        );
    }
}
