import React, {Component} from 'react';
import {Input, Select} from '@icedesign/base';
import API from '../../../API'
import Axios from 'axios'
import '../Styles.scss'

const {Combobox} = Select;

export default class Pull extends Component{
    constructor(props){
        super(props);
        this.state = {
            gitUrl: ""
        }
    }
    componentWillMount(){
        if(this.props.appId){
            let url = API.application + "/app/" + this.props.appId + "/urlInfo";
            let self = this;
            Axios.get(url).then((response)=>{
                if(response.status === 200){
                    self.setState({
                        gitUrl: response.data.warehouseUrl
                    });
                    self.props.onChange(response.data.warehouseUrl);
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
                        value={this.state.gitUrl}
                        disabled
                    />
                </div>
            </div>
        )
    }
}
