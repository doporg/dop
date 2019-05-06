import React from 'react'
import Axios from 'axios';
import API from "../../API";

import './MergeRequest.css'

import imgOk from './imgs/ok.png';
import imgWarning from './imgs/warning.png';

class MergeRequest extends React.Component{

    constructor(props) {
        super(props);
        let {projectname, username, iid} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            iid:iid,
            mrInfo:{
                state:"",
                created_by:"",
                created_at:"",
                merged_by:"",
                merged_at:"",
                closed_by:"",
                closed_at:"",
                target_branch:"",
                source_branch:"",
                title:"",
                description:"",

            },
            accessInfo:{
                access_level:0,
                visibility:"private",
            },
            target_branch:{
                protected_:true,
                developers_can_merge:false
            },
        }
    }

    loadAccess(){
        let url = API.code+"/projects/"+this.state.projectid+"/access";
        Axios.get(url).then(response=>{
            this.setState({
                accessInfo:response.data
            });
        });
    }

    componentWillMount(){
        this.loadData();
    }

    loadData(){
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests/"+this.state.iid;
        Axios.get(url).then(response=>{
            this.setState({
                mrInfo:response.data
            },this.loadCanMerge.bind(this));
        });
        this.loadAccess();
    }

    loadCanMerge(){
        let url= API.code+"/projects/"+this.state.projectid+"/repository/branch?branch="+this.state.mrInfo.target_branch;
        Axios.get(url).then(response=>{
            this.setState({
                target_branch:response.data
            })
        });
    }

    updateMR(state_event){
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests/"+this.state.iid;
        Axios({
            method: 'PUT',
            url: url,
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
            params:{
                state_event:state_event
            },
        }).then(response=>{
            this.loadData();
        })
    }

    acceptMR(){
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests/"+this.state.iid+"/merge";
        Axios({
            method: 'PUT',
            url: url,
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
            params:{},
        }).then(response=>{
            this.loadData();
        })
    }

    render(){
        const {mrInfo,accessInfo,target_branch}=this.state;
        return (
            <div className="mr-container">
                <div className="div-mr-top">
                    <div className="div-mr-author">
                        {
                            (()=>{
                                if(mrInfo.state==="opened"){
                                    return <label className="label-mr-opened">Opened</label>
                                }else if(mrInfo.state==="merged"){
                                    return <label className="label-mr-merged">Merged</label>
                                }else if(mrInfo.state==="closed"){
                                    return <label className="label-mr-closed">Closed</label>
                                }
                            })()
                        }
                        <span className="text-mr-author-time">{mrInfo.created_at}</span>创建由
                        <div className="div-mr-author-avatar">{mrInfo.created_by.charAt(0)}</div><b>{mrInfo.created_by}</b>
                    </div>
                    {
                        (()=>{
                            if(mrInfo.state==="opened"&&accessInfo.access_level>=30){
                                return <button onClick={this.updateMR.bind(this,"close")} className="btn-mr-close">关闭合并请求</button>;
                            }
                        })()
                    }
                </div>
                <div className="div-mr-title">
                    {mrInfo.title}
                </div>
                <div className="div-mr-description">
                    {mrInfo.description}
                </div>

                <div className="div-mr-msg">请求合并&nbsp;&nbsp;<b>{mrInfo.source_branch}</b>&nbsp;&nbsp;到&nbsp;&nbsp;<b>{mrInfo.target_branch}</b></div>

                {
                    (()=>{
                        if(mrInfo.state==="opened"){
                            if(mrInfo.merge_status==="can_be_merged"){
                                if(target_branch.protected_==="false"||accessInfo.access_level===40||accessInfo.access_level===30&&target_branch.developers_can_merge) {
                                    return (
                                        <div className="div-mr-operation">
                                            <img className="img-mr-tip" src={imgOk}/>
                                            <button onClick={this.acceptMR.bind(this)} className="btn-mr-merge">Merge</button>
                                            此合并请求现在可以进行合并
                                        </div>
                                    )
                                }else {
                                    return (
                                        <div className="div-mr-operation">
                                            <img className="img-mr-tip" src={imgWarning}/>
                                            你没有权限合并该请求
                                        </div>
                                    )
                                }
                            }else{
                                if(mrInfo.changes_count===null){
                                    return (
                                        <div className="div-mr-operation">
                                            <img className="img-mr-tip" src={imgWarning}/>
                                            此合并请求不包含任何的改动
                                        </div>
                                    )
                                }else {
                                    return (
                                        <div className="div-mr-operation">
                                            <img className="img-mr-tip" src={imgWarning}/>
                                            此合并请求存在冲突
                                        </div>
                                    )
                                }
                            }
                        }
                    })()
                }

                {
                    (()=>{
                        if(mrInfo.state==="merged"){
                            return (
                                <div className="div-mr-operation">
                                    <img className="img-mr-tip" src={imgOk}/>
                                    <span className="text-mr-author-time">{mrInfo.merged_at}</span>合并由
                                    <div className="div-mr-author-avatar">{mrInfo.merged_by.charAt(0)}</div><b>{mrInfo.merged_by}</b>
                                </div>
                            )
                        }
                    })()
                }

                {
                    (()=>{
                        if(mrInfo.state==="closed"){
                            return (
                                <div className="div-mr-operation">
                                    <img className="img-mr-tip" src={imgWarning}/>
                                    {
                                        (()=>{
                                            if(accessInfo.access_level>=30){
                                                return <button onClick={this.updateMR.bind(this,"reopen")} className="btn-mr-reopen">Reopen</button>;
                                            }
                                        })()
                                    }
                                    <span className="text-mr-author-time">{mrInfo.closed_at}</span>关闭由
                                    <div className="div-mr-author-avatar">{mrInfo.closed_by.charAt(0)}</div><b>{mrInfo.closed_by}</b>
                                </div>
                            )
                        }
                    })()
                }

            </div>
        )
    }
}

export default (props)=><MergeRequest {...props} key={props.location.pathname} />
