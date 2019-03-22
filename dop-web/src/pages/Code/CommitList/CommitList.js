import React from 'react'
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import copy from 'copy-to-clipboard';
import {Feedback} from '@icedesign/base';

import imgCopy from './imgs/copy.png';
import imgFolder from './imgs/folder.png';


import "./CommitList.css"

const {toast} = Feedback;

export default class CommitList extends React.Component{

    constructor(props){
        super(props);
        const {projectid,branch}=this.props.match.params;
        this.state={
            projectid: projectid,
            ref:branch,
            refOptions: [],
            commitList:[],
            showList:[],
            msgInput:"",
        }
    }

    componentWillMount() {

        let url=API.code+"/projects/"+this.state.projectid+"/repository/branchandtag?username="+sessionStorage.getItem("user-name");
        let self=this;
        Axios.get(url).then(response=>{
            self.setState({
                refOptions:response.data
            })
        });

        url=API.code+"/projects/"+this.state.projectid+"/repository/commits?ref_name="+this.state.ref+"&username="+sessionStorage.getItem("user-name");
        Axios.get(url).then(response=>{
            // console.log(response.data);
            self.setState({
                commitList:response.data,
                showList:response.data
            })
        });

    }

    handleInputChange(e){
        const val=e.target.value;
        if(val!==""){
            const showList=this.state.commitList.filter(function (item) {
                return item.message.indexOf(val)!==-1;
            })
            this.setState({
                msgInput:val,
                showList:showList
            })
        }else {
            this.setState({
                msgInput:val,
                showList:this.state.commitList
            })
        }
    }

    changeRef(value, data, extra) {

        let url=API.code+"/projects/"+this.state.projectid+"/repository/commits?ref_name="+value+"&username="+sessionStorage.getItem("user-name");
        let self=this;
        Axios.get(url).then(response=>{
            // console.log(response.data);
            self.setState({
                commitList:response.data,
                showList:response.data,
                ref:value,
                msgInput:""
            })
        });

    }

    isSameDate(d1,d2){
        return d1.getFullYear()===d2.getFullYear()&&d1.getMonth()===d2.getMonth()&&d1.getDate()===d2.getDate();
    }


    copySha(sha){
        copy(sha);
        toast.success("已将commit sha复制到剪贴板")
    }


    render(){
        return (
            <div className="commit-list-container">
                <div className="div-commit-list-top">
                    <CascaderSelect className="select-branch"  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    <input value={this.state.msgInput} onChange={this.handleInputChange.bind(this)} className="input-commit-msg-right" placeholder="输入commit message查找commit"/>
                </div>
                {
                    (()=>{
                        let res=[];
                        let commitList=this.state.showList;
                        let count;
                        let d1;
                        let d2;


                        for(let i=0;i<commitList.length;i++){
                            let commit=commitList[i];
                            if(i===0){
                                count=1;
                                d1=new Date(commit.authored_date);
                            }else {
                                d2=new Date(commit.authored_date);
                                if(this.isSameDate(d1,d2)){
                                    count++;
                                }else {
                                    res.push(<div className="div-commit-date">{(d1.getMonth()+1)+"月"+d1.getDate()+ "日," + d1.getFullYear() + " " + count + "次提交"}</div>);
                                    for(let j=i-count;j<i;j++){
                                        res.push(
                                            <div className="div-commit-item">
                                                <div className="div-commit-item-avatar">{commitList[j].author_name.substring(0,1).toUpperCase()}</div>
                                                <div className="div-commit-item-content">
                                                    <div className="div-commit-item-content-up">{commitList[j].message}</div>
                                                    <div className="div-commit-item-content-down">
                                                        {commitList[j].author_name+" 提交在"+commitList[j].authored_time}
                                                    </div>
                                                </div>
                                                <div className="div-commit-operation">
                                                    <span className="text-commit-short-id">{commitList[j].short_id}</span>
                                                    <button className="btn-commit-copy-sha" onClick={this.copySha.bind(this,commitList[j].id)}>
                                                        <img src={imgCopy}/>
                                                    </button>
                                                    <button className="btn-commit-browse-file">
                                                        <img src={imgFolder}/>
                                                    </button>
                                                </div>
                                            </div>
                                        )
                                    }
                                    d1=d2;
                                    count=1;
                                }
                            }
                        }

                        if(commitList.length!==0) {
                            res.push(<div className="div-commit-date">{(d1.getMonth()+1)+"月"+d1.getDate()+ "日," + d1.getFullYear() + " " + count + "次提交"}</div>);
                            for (let i = commitList.length - count; i < commitList.length; i++) {
                                res.push(
                                    <div className="div-commit-item">
                                        <div className="div-commit-item-avatar">{commitList[i].author_name.substring(0,1).toUpperCase()}</div>
                                        <div className="div-commit-item-content">
                                            <div className="div-commit-item-content-up">{commitList[i].message}</div>
                                            <div className="div-commit-item-content-down">
                                                {commitList[i].author_name+" 提交在"+commitList[i].authored_time}
                                            </div>
                                        </div>
                                        <div className="div-commit-operation">
                                            <span className="text-commit-short-id">{commitList[i].short_id}</span>
                                            <button className="btn-commit-copy-sha" onClick={this.copySha.bind(this,commitList[i].id)}>
                                                <img src={imgCopy}/>
                                            </button>
                                            <button className="btn-commit-browse-file">
                                                <img src={imgFolder}/>
                                            </button>
                                        </div>
                                    </div>
                                )
                            }
                        }


                        return res;

                    })()
                }
            </div>
        )
    }


}
