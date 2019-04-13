import React from 'react'
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import copy from 'copy-to-clipboard';
import {Feedback} from '@icedesign/base';
import { Balloon } from "@icedesign/base";
import ReactTooltip from 'react-tooltip'
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';

import imgCopy from './imgs/copy.png';
import imgFolder from './imgs/folder.png';


import "./CommitList.css"

const {toast} = Feedback;

const spinner=(
    <Spinner/>
);

class CommitList extends React.Component{

    constructor(props){
        super(props);
        const {projectname,ref,username}=this.props.match.params;
        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            ref:ref,
            refOptions: [],
            commitList:[],
            showList:[],
            msgInput:"",
            loadingVisible:true,
        }
    }

    componentWillMount() {

        let {projectid,ref} = this.state;
        this.loadData(projectid,ref);

    }

    loadData(projectid,ref){

        let url=API.code+"/projects/"+projectid+"/repository/branchandtag?userId="+sessionStorage.getItem("user-id");
        let self=this;
        Axios.get(url).then(response=>{
            self.setState({
                refOptions:response.data,
                loadingVisible:false,
            })
        });

        url=API.code+"/projects/"+projectid+"/repository/commits?path=/&ref_name="+ref+"&userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
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
            });
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

        let {username,projectname}=this.state;
        this.props.history.push("/code/"+username+"/"+projectname+"/commitlist/"+value);

    }

    isSameDate(d1,d2){
        return d1.getFullYear()===d2.getFullYear()&&d1.getMonth()===d2.getMonth()&&d1.getDate()===d2.getDate();
    }


    copySha(sha){
        copy(sha);
        toast.success("已将commit sha复制到剪贴板")
    }

    browseFile(commit_id){

        let {username,projectname}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+commit_id+"/"+encodeURIComponent("/"));

    }

    rootLink(){
        let {username,projectname,ref}=this.state;
        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+ref+"/"+encodeURIComponent("/"));
    }


    render(){
        return (
            <div className="commit-list-container">
                <div className="div-commit-list-top">
                    <Loading visible={this.state.loadingVisible} className="loading-ref-commit-list" tip={spinner}>
                        <CascaderSelect className="select-ref-commit-list"  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    </Loading>
                    <a className="link-commit-root" onClick={this.rootLink.bind(this)}>根目录</a>
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
                                                    <button data-tip data-for='copy' className="btn-commit-copy-sha" onClick={this.copySha.bind(this,commitList[j].id)}>
                                                        <img src={imgCopy} className="img-commit-copy-sha"/>
                                                    </button>
                                                    <button data-tip data-for='browse' className="btn-commit-browse-file" onClick={this.browseFile.bind(this,commitList[j].id)}>
                                                        <img src={imgFolder} className="img-commit-browse-file"/>
                                                    </button>
                                                    <ReactTooltip id='copy' place="bottom" type='dark' effect='solid'>
                                                        <span>复制提交sha到剪贴板</span>
                                                    </ReactTooltip>
                                                    <ReactTooltip id='browse' place="bottom" type='dark' effect='solid'>
                                                        <span>查看文件</span>
                                                    </ReactTooltip>
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
                                            <button data-tip data-for='copy' className="btn-commit-copy-sha" onClick={this.copySha.bind(this,commitList[i].id)}>
                                                <img src={imgCopy} className="img-commit-copy-sha"/>
                                            </button>
                                            <button data-tip data-for='browse' className="btn-commit-browse-file" onClick={this.browseFile.bind(this,commitList[i].id)}>
                                                <img src={imgFolder} className="img-commit-browse-file"/>
                                            </button>
                                            <ReactTooltip id='copy' place="bottom" type='dark' effect='solid'>
                                                <span>复制提交sha到剪贴板</span>
                                            </ReactTooltip>
                                            <ReactTooltip id='browse' place="bottom" type='dark' effect='solid'>
                                                <span>查看文件</span>
                                            </ReactTooltip>
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

export default (props)=><CommitList {...props} key={props.location.pathname} />
