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
            runs: {},
            queue: [],
            time: ""
        }
    }

    componentDidMount() {
        let self = this;
        this.getRuns().then((data) => {
            self.setState({
                runs: data
            })
        })
    }


    getRuns() {
        let url = API.pipeline + '/v1/jenkins/runs?id='+ this.state.pipelineId;
        // let url = 'http://jenkins.dop.clsaa.com/blue/rest/organizations/jenkins/pipelines/simple-node-app/runs/';
        let self = this;
        return new Promise((resolve, reject) => {
            Axios.get(url).then((response) => {
                if (response.status === 200) {
                    if (response.data.length === 0) {
                        toast.show({
                            type: "prompt",
                            content: "该流水线尚未运行",
                            duration: 3000
                        });
                    } else {
                        resolve(response.data[0]);
                        if (response.data[0].state === 'FINISHED') {
                            self.clear();
                        }

                    }
                }
                reject()
            })
        })
    }

    buildPipeline() {
        let url = API.pipeline + '/v1/jenkins/build?id='+ this.state.pipelineId;
        let self = this;
        Axios.post(url).then((response) => {
            if (response.status === 200) {
                let time = setInterval(() => {
                    this.getRuns().then((data) => {
                        self.setState({
                            runs: data
                        })
                    })
                }, 5000);
                self.setState({
                    runs: response.data,
                    time: time
                });

            }
        })

    }
    clear(){
        clearInterval(this.state.time)
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
                        <Button type="normal" className="button" disabled>
                            <Icon type="edit"/>
                            编辑流水线
                        </Button>
                        <Button type="secondary" shape="warning" className="button" disabled>
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
