import React from 'react';
import API from "../../API";
import Axios from 'axios';
import {Select} from "@icedesign/base";

import './NewMergeRequest.css';

class NewMergeRequest extends React.Component{

    constructor(props) {
        super(props);
        const {projectname, username} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            branches:[],
            title:"",
            description:"",
            source_branch:"",
            target_branch:"",
        }
    }

    componentWillMount(){
        let url = API.code+"/projects/"+this.state.projectid+"/branches";
        Axios.get(url).then(response=>{
            this.setState({
                branches:response.data
            })
        });
    }

    changeTitle(e){
        this.setState({
            title:e.target.value
        })
    }

    changeDescription(e){
        this.setState({
            description:e.target.value
        })
    }

    selectSource(value){
        this.setState({
            source_branch:value
        })
    }

    selectTarget(value){
        this.setState({
            target_branch:value
        })
    }

    cancelLink(){
        this.props.history.push("/code/"+this.state.projectid+"/merge_requests?state=opened");
    }

    addMergeRequest(){
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests";
        let {source_branch,target_branch,title,description}=this.state;
        Axios({
            method: 'POST',
            url: url,
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
            params:{
                source_branch:source_branch,
                target_branch:target_branch,
                title:title,
                description:description,
            },
        }).then(response=>{
            this.props.history.push("/code/"+this.state.projectid+"/merge_requests?state=opened");
        })
    }

    render(){
        return (
            <div className="new-mr-container">
                <div className="div-new-mr-top">
                    创建合并请求
                </div>
                <div>
                    <div className="div-new-mr-input">
                        <span className="text-new-mr-title">标题</span>
                        <input onChange={this.changeTitle.bind(this)} className="input-new-mr-title"/>
                    </div>
                    <div className="div-new-mr-input">
                        <span className="text-new-mr-description">描述</span>
                        <textarea onChange={this.changeDescription.bind(this)} className="input-new-mr-description"/>
                    </div>
                    <div className="div-new-mr-input">
                        <span className="text-new-mr-branch">目标分支</span>
                        <Select onChange={this.selectTarget.bind(this)} className="select-new-mr-branch" dataSource={this.state.branches}/>
                    </div>
                    <div className="div-new-mr-input">
                        <span className="text-new-mr-branch">源分支</span>
                        <Select onChange={this.selectSource.bind(this)} className="select-new-mr-branch" dataSource={this.state.branches}/>
                    </div>
                </div>
                <div className="div-new-mr-submit">
                    <button className="btn-new-mr-add" onClick={this.addMergeRequest.bind(this)}>创建合并请求</button>
                    <button className="btn-new-mr-cancel" onClick={this.cancelLink.bind(this)}>取消</button>
                </div>
            </div>
        );
    }
}

export default (props)=><NewMergeRequest {...props} key={props.location.pathname} />

