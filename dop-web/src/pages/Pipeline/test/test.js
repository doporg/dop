import React, {Component} from 'react';
import Pull from '../components/chosenSteps/Pull'


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
                <Pull
                    // projectId={435}
                />
            </div>
        )
    }

}
