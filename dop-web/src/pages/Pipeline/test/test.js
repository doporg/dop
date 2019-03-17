import React, {Component} from 'react';
import PipelineInfoStage from '../components/PipelineInfoStage'


export default class PipelineTest extends Component {
    constructor() {
        super();
        this.state = {
            stages: [{
                name: "11",
                steps: []
            },{
                name: "22",
                steps: []
            }]
        };
    }
    stages(value){
        console.log(value)
    }
    render() {
        return (
            <div>
                <PipelineInfoStage stages={this.state.stages} currentStage={0} onChange={this.stages}/>
            </div>
        )
    }

}
