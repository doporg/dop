import React from 'react';
import { Select } from "@icedesign/base";
import {Radio} from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import {Feedback} from '@icedesign/base';
import { Dialog } from '@icedesign/base';


import './EditProject.css'

const {Group: RadioGroup} = Radio;
const {toast} = Feedback;


class EditProject extends React.Component{

    constructor(props){
        super(props);
        const {username,projectname}=this.props.match.params;
        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            editInfo:{},//id,name,description,default_branch,visibility
            branches:[],

        };
    }

    componentWillMount(){

        let url=API.code+"/projects/"+this.state.projectid+"/editinfo?userId="+sessionStorage.getItem("user-id");
        let self=this;
        Axios.get(url).then(response=>{
            self.setState({
                editInfo:response.data
            })
        });

        url=API.code+"/projects/"+this.state.projectid+"/branches?userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
            self.setState({
                branches:response.data
            })
        });

    }

    updateProject(){

        let editInfo=this.state.editInfo;

        Axios({
            method: "PUT",
            url: API.code+ "/projects/"+this.state.projectid,
            params: {
                userId:sessionStorage.getItem("user-id"),
                name:editInfo.name,
                description:editInfo.description,
                default_branch:editInfo.default_branch,
                visibility:editInfo.visibility
            },
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
        }).then(response => {

            let {username,projectname}=this.state;

            this.props.history.push("/code/"+username+"/"+projectname);

        })

    }


    deleteProject(){

        if(window.confirm("确认删除项目?")) {
            let projectid = this.state.projectid;
            let url = API.code + "/projects/" + projectid + "?userId=" + sessionStorage.getItem("user-id");
            Axios.delete(url).then(response => {
                this.props.history.push("/code/projects/personal");
            })
        }

    }


    changeName(e){
        let editInfo=this.state.editInfo;
        editInfo.name=e.target.value;
        this.setState({
            editInfo:editInfo
        })
    }

    changeDescription(e){
        let editInfo=this.state.editInfo;
        editInfo.description=e.target.value;
        this.setState({
            editInfo:editInfo
        })
    }

    changeDefaultBranch(value, data, extra){
        let editInfo=this.state.editInfo;
        editInfo.default_branch=value;
        this.setState({
            editInfo:editInfo
        })
    }

    changeVisibility(value){
        let editInfo=this.state.editInfo;
        editInfo.visibility=value;
        this.setState({
            editInfo:editInfo
        })
    }


    render(){

        return (
            <div>
                <div className="edit-project-update-container">
                    <div className="edit-project-update-title">
                        项目设置
                    </div>
                    <div className="edit-project-input-div">
                        <label className="edit-project-label-left">名称</label>
                        <input value={this.state.editInfo.name} className="edit-project-input-name" onChange={this.changeName.bind(this)}/>
                    </div>
                    <div className="edit-project-input-div">
                        <label className="edit-project-label-left">描述</label>
                        <textarea value={this.state.editInfo.description} className="edit-project-input-description" onChange={this.changeDescription.bind(this)}/>
                    </div>
                    <div className="edit-project-input-div">
                        <label className="edit-project-label-branch">默认分支</label>
                        <Select value={this.state.editInfo.default_branch} dataSource={this.state.branches} className="edit-project-select-branch" size="large" onChange={this.changeDefaultBranch.bind(this)} />
                    </div>
                    <div className="edit-project-choose-div">
                        <label className="edit-project-label-left">可见等级</label>
                        <div className="edit-project-visibility-div">
                            <RadioGroup value={this.state.editInfo.visibility} onChange={this.changeVisibility.bind(this)}>
                                <Radio id="private" value="private">
                                    PRIVATE&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </Radio>
                                <Radio id="public" value="public">
                                    PUBLIC
                                </Radio>
                            </RadioGroup>
                        </div>
                    </div>
                    <div className="edit-project-submit-div">
                        <button onClick={this.updateProject.bind(this)} className="edit-project-update-btn">保存修改</button>
                    </div>
                </div>

                <div className="edit-project-delete-container">
                    <div className="edit-project-delete-title">删除项目</div>
                    <div className="edit-project-delete-description">
                        <p>删除项目将删除其版本仓库及所有相关资源</p>
                        <p><b>删除项目无法恢复！</b></p>
                    </div>
                    <div className="edit-project-submit-div">
                        <button onClick={this.deleteProject.bind(this)} className="edit-project-delete-btn">删除项目</button>
                    </div>
                </div>
            </div>
        )
    }
}

export default (props)=><EditProject {...props} key={props.location.pathname} />
