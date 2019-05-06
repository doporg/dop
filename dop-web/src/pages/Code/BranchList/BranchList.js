import React from 'react';
import Axios from 'axios';
import API from "../../API";

import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';

import './BranchList.css'
import imgBranch from './imgs/branch.png';
import imgCommit from './imgs/commit.png';
import imgDelete from './imgs/delete.png';
import imgDeleteDisabled from './imgs/delete-disabled.png';

const spinner=(
    <Spinner/>
);

class BranchList extends React.Component{

    constructor(props){
        super(props);
        const {projectname,username}=this.props.match.params;
        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            branchList:[],//name,default_,protected_,merged,commit_id,commit_short_id,commit_msg,commit_time
            showData:[],
            nameInput:"",
            defaultBranch:"",
            loadingVisible:true,
            accessInfo:{
                access_level:0,
                visibility:"private",
            },
        };
    }

    componentWillMount(){
        this.loadData();
    }

    loadAccess(){
        let url = API.code+"/projects/"+this.state.projectid+"/access";
        Axios.get(url).then(response=>{
            this.setState({
                accessInfo:response.data
            });
        });
    }

    loadData(){
        let url=API.code+"/projects/"+this.state.projectid+"/repository/branches";
        Axios.get(url).then(response=>{
            let defaultBranch;
            for(let i=0;i<response.data.length;i++){
                if(response.data[i].default_==="true"){
                    defaultBranch=response.data[i].name;
                }
            }
            this.setState({
                branchList:response.data,
                showData:response.data,
                nameInput:"",
                defaultBranch:defaultBranch,
                loadingVisible:false,
            })
        });
        this.loadAccess();
    }

    changeBranchName(e){
        let val=e.target.value;
        if(val!==""){
            let showData=this.state.branchList.filter(item=>{
                return item.name.indexOf(val)!==-1;
            });
            this.setState({
                nameInput:val,
                showData:showData
            })

        }else{
            this.setState({
                nameInput:val,
                showData:this.state.branchList
            })
        }
    }

    settingsLink(){
        let {username,projectname}=this.state;
        this.props.history.push("/code/"+username+"/"+projectname+"/protected_branches");
    }

    deleteMergedBranch(){
        if(window.confirm("确认删除所有合并分支?")) {
            this.setState({
                loadingVisible:true
            });
            let url = API.code + "/projects/" + this.state.projectid + "/repository/merged_branches";
            Axios.delete(url).then(() => {
                setTimeout(()=>{
                    this.loadData();
                },1000);
            })
        }
    }

    deleteBranch(branch){
        if(window.confirm("确认删除分支"+branch+"?")) {
            this.setState({
                loadingVisible:true
            });
            let url = API.code + "/projects/" + this.state.projectid + "/repository/branches?branch=" + branch;
            Axios.delete(url).then(() => {
                this.loadData();
            })
        }
    }

    newBranch(){
        this.props.history.push("/code/"+this.state.projectid+"/branches/new");
    }

    commitLink(sha){
        this.props.history.push("/code/"+this.state.projectid+"/commit/"+sha);
    }

    newMergeRequest(source){
        this.props.history.push("/code/"+this.state.projectid+"/merge_requests/new?source="+source+"&target="+this.state.defaultBranch);
    }
    render(){
        const accessInfo=this.state.accessInfo;
        return (
            <div className="branch-list-container">
                <div className="div-branch-list-top">
                    {
                        (()=>{
                            if(accessInfo.access_level===40){
                                return <span className="text-branch-list-top">在<a onClick={this.settingsLink.bind(this)}>项目设置</a>里可以设置受保护的分支</span>
                            }
                        })()
                    }
                    {
                        (()=>{
                            if(accessInfo.access_level>=30){
                                return [
                                    <button onClick={this.newBranch.bind(this)} className="btn-new-branch">+ 新建分支</button>,
                                    <button onClick={this.deleteMergedBranch.bind(this)} className="btn-delete-merged-branches">删除合并分支</button>
                                ];
                            }
                        })()
                    }

                    <input value={this.state.nameInput} onChange={this.changeBranchName.bind(this)} className="input-branch-list-name" placeholder="输入分支名称来搜索"/>
                </div>
                <Loading visible={this.state.loadingVisible} className="loading-branch-list" tip={spinner}>
                    <div>
                        {
                            this.state.showData.map(item=>{
                                return (
                                    <div className="div-branch-list-item">
                                        <div className="div-branch-item-intro">
                                            <div className="div-branch-item-name">
                                                <img className="img-branch-item-intro" src={imgBranch}/>
                                                <span className="text-branch-item-name">{item.name}</span>
                                                {
                                                    (() => {
                                                        if(item.default_==="true") return <label className="label-branch-item label-branch-item-default">default</label>;

                                                    })()
                                                }
                                                {
                                                    (() => {
                                                        if(item.protected_==="true") return <label className="label-branch-item label-branch-item-protected">protected</label>;
                                                    })()
                                                }
                                                {
                                                    (()=>{
                                                        if(item.merged==="true") return <label className="label-branch-item label-branch-item-merged">merged</label>;
                                                    })()
                                                }

                                            </div>
                                            <div className="div-branch-item-commit">
                                                <img className="img-branch-item-intro" src={imgCommit}/>
                                                <a className="text-branch-item-primary" onClick={this.commitLink.bind(this,item.commit_id)}>{item.commit_short_id}</a>
                                                <a onClick={this.commitLink.bind(this,item.commit_id)}>&nbsp;·&nbsp;{item.commit_msg}&nbsp;·&nbsp;</a>
                                                <label>{item.commit_time}</label>
                                            </div>
                                        </div>
                                        <div className="div-branch-item-operation">
                                            {
                                                (()=>{
                                                    if(accessInfo.access_level>=30) {
                                                        if (item.default_ === "true" || item.protected_ === "true") {
                                                            return (
                                                                <a className="btn-branch-item-delete-disabled">
                                                                    <img className="img-branch-item-delete"
                                                                         src={imgDeleteDisabled}/>
                                                                </a>
                                                            )
                                                        } else {
                                                            return (
                                                                <a onClick={this.deleteBranch.bind(this, item.name)}
                                                                   className="btn-branch-item-delete">
                                                                    <img className="img-branch-item-delete"
                                                                         src={imgDelete}/>
                                                                </a>
                                                            )
                                                        }
                                                    }
                                                })()
                                            }

                                            {
                                                (() => {
                                                    if (item.default_ === "false"&&accessInfo.access_level>=30) {
                                                        return <a onClick={this.newMergeRequest.bind(this,item.name)} className="btn-branch-item-merge">Merge request</a>
                                                    }
                                                })()
                                            }
                                        </div>
                                    </div>
                                )
                            })
                        }

                    </div>
                </Loading>



            </div>
        )
    }
}

export default (props)=><BranchList {...props} key={props.location.pathname} />
