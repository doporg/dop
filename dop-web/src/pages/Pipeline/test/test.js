import React, {Component} from 'react';
import PipelineInfoStage from '../components/PipelineInfoStage'


export default class PipelineTest extends Component {
    constructor() {
        super();
        this.state = {
            stages: []
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
