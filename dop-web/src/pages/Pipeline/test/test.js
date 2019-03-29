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
        // let url = API.gateway + '/application-server/app/env/477/yamlFile';
        // // let url = "https://raw.githubusercontent.com/clsaa/dop/master/dop-web/k8s.yaml"
        // Axios.get(url).then((response)=>{
        //     console.log(response.data.yaml)
        // })
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
