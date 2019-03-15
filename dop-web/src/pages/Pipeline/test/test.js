import React, {Component} from 'react';
import RunStep from '../components/RunStep'
import SuccessLog from '../components/SuccessLog'
import FailLog from '../components/FailLog'
import ProcessLog from '../components/ProcessLog'

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
