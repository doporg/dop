import React from 'react';
import { CascaderSelect } from "@icedesign/base";
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
            deleteVisible:false

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

            toast.success("修改项目信息成功");

            let {username,projectname}=this.state;

            this.props.history.push("/code/"+username+"/"+projectname);

        })

    }


    deleteProject(){

        let projectid=this.state.projectid;
        let url=API.code+"/projects/"+projectid+"?userId="+sessionStorage.getItem("user-id");
        Axios.delete(url).then(response=>{
            this.props.history.push("/code/projects");
        })

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

    openDelete(){
        this.setState({
            deleteVisible:true
        });
    }

    closeDelete(){
        this.setState({
            deleteVisible:false
        });
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
                        <CascaderSelect value={this.state.editInfo.default_branch} dataSource={this.state.branches} className="edit-project-select-branch" size="large" onChange={this.changeDefaultBranch.bind(this)} />
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
                        <button onClick={this.openDelete.bind(this)} className="edit-project-delete-btn">删除项目</button>
                    </div>
                </div>

                <Dialog
                    visible={this.state.deleteVisible}
                    onOk={this.deleteProject.bind(this)}
                    onCancel={this.closeDelete.bind(this)}
                    onClose={this.closeDelete.bind(this)}
                    title={"删除"+this.state.editInfo.name}
                    footerAlign="left"
                >
                    <div className="edit-project-delete-dialog">
                        <p className="edit-project-delete-font"><b>删除项目后无法恢复！确定要继续吗?</b></p>
                    </div>
                </Dialog>
            </div>
        )
    }
}

export default (props)=><EditProject {...props} key={props.location.pathname} />
