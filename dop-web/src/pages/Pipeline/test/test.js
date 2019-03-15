import React, {Component} from 'react';
import RunStep from '../components/RunStep'
import SuccessLog from '../components/SuccessLog'
import FailLog from '../components/FailLog'
import ProcessLog from '../components/ProcessLog'
import {Feedback} from "@icedesign/base";
import API from "../../API";
import Axios from "axios/index";

const {toast} = Feedback;

export default class PipelineTest extends Component {
    constructor() {
        super();
        this.state = {
            currentStep: 1,
            steps: [{
                title: "步骤一",
                disabled: true
            }, {
                title: "步骤一",
            }, {
                title: "步骤一",
                disabled: true
            }]
        };
    }
    componentDidMount(){
        this.jenkinsAuthorization().then((token)=>{
            console.log(token)
        }).catch((data)=>{
            toast.show({
                type: "error",
                content: "授权失败",
                duration: 1000
            });
        })
    }

    jenkinsAuthorization(){
        let url = API.pipeline + '/v1/authorization';
        return new Promise((resolve, reject)=>{
            Axios.get(url).then((response)=>{
                if(response.status === 200){
                    resolve(response.data)
                }else{
                    reject()
                }
            });
        })
    }
    current(data) {
        this.setState({
            currentStep: data
        });
        console.log(this.state.currentStep)
    }

    render() {
        return (
            <div>
                <RunStep steps={this.state.steps} current={this.current.bind(this)}/>
                <SuccessLog/>
                <FailLog/>
                <ProcessLog/>

            </div>
        )
    }

}
