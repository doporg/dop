import React, {Component} from 'react';
import {Input, Select} from '@icedesign/base';
import API from '../../../API'
import Axios from 'axios'
import '../Styles.scss'

const {Combobox} = Select;

export default class Pull extends Component{
    constructor(props){
        super(props);
    }
    componentWillMount(){
        if(this.props.projectId){
            let url = API.gateway + "/application-server/applicationDetail/" + this.props.projectId;
            let self = this;
            Axios.get(url).then((response)=>{
                if(response.status === 200){
                    self.props.onChange(response.data.warehouse);
                }
            })
        }
    }

    /**
     *  构建-填入git地址
     * */
    buildMavenGit(value) {
        this.props.onChange(value)
    }
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">拉取代码</h3>
                <div
                    className="chosen-task-detail-body">
                    <span className="item">
                        <span className="must">*</span>
                        <span>Git地址: </span>
                    </span>
                    <Input
                        onChange={this.buildMavenGit.bind(this)}
                        className="input"
                        value={this.props.gitUrl}
                    />
                </div>
            </div>
        )
    }
}
