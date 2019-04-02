import React, {Component} from 'react';
import Pull from '../components/chosenSteps/Pull'
import DockerImage from '../components/chosenSteps/DockerImage'
import Axios from 'axios'
import API from '../../API'


export default class PipelineTest extends Component {
    constructor() {
        super();
        this.state = {
            stages: []
        };
    }
    componentWillMount(){
        let url1 = API.gateway + '/application-server/app/env/524/yamlFile?runningId="5ca0cbd6bb5f6c1350ca8bb7"';
        Axios.get(url1).then((response)=>{
            console.log(response)
        })
        // let url = "https://raw.githubusercontent.com/clsaa/dop/master/dop-web/k8s.yaml"
    }
    render() {
        return (
            <div>
                <Pull
                    // projectId={435}
                />
                <DockerImage
                    appId={477}
                />
            </div>
        )
    }

}
