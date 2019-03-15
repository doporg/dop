/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import Axios from 'axios'
import API from '../../API'
import RunStep from '../components/RunStep'

export default class RunResult extends Component {
    constructor(props) {
        super(props);
        this.state = {
            stages: [],
            currentStage: 0
        }
    }
    componentWillReceiveProps (nextProps){
        if(nextProps.runs){
            this.stage(nextProps.runs._links.self.href)
        }
    }
    componentDidUpdate (prevProps, prevState){
        if(prevState.currentStage !== this.state.currentStage){
            console.log(this.state.currentStage)
        }
    }
    //当前stage
    current(data) {
        this.setState({
            currentStage: data
        });
    }
    //获取stage
    stage(href){
        let url = 'http://jenkins.dop.clsaa.com/blue/rest/organizations/jenkins/pipelines/simple-node-app/runs/47/nodes/'
        // let url = API.jenkins + href + "nodes/";
        let self = this;
        let stages = [];
        Axios({
            method: 'get',
            url: url,
            headers:{
                'Authorization': 'Basic emZsOnpmbA=='
            }
        }).then((response)=>{
            console.log(response.data);
            if(response.status === 200){
                response.data.map((item, index)=>{
                    let stage = {
                        title: item.displayName,
                        time: item.durationInMillis,
                        disabled: item.result === 'NOT_BUILT',
                    };
                    stages.push(stage)
                })
            }
            self.setState({
                stages
            })
        })
    }
    render() {
        return (
            <div>
                <RunStep stages={this.state.stages} currentStage={this.state.currentStage} current={this.current.bind(this)}/>
            </div>
        );
    }
}
