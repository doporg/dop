import React from 'react'
import Axios from 'axios';
import API from "../../API";
import {parseDiff, Diff, Hunk,Decoration} from 'react-diff-view';
import copy from 'copy-to-clipboard';
import {Feedback} from '@icedesign/base';
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';

import './Commit.css';
import './ReactDiffView.css'

import imgFile from './imgs/file.png';
import imgCopy from './imgs/copy.png';

const {toast} = Feedback;

const spinner=(
    <Spinner/>
);

//diff,new_path,old_path,a_mode,b_mode,new_file,renamed_file,deleted_file
const DiffFile = ({diffItem}) => {
    const diffText=diffItem.diff;
    const files = parseDiff(diffText);

    const renderFile = ({oldRevision, newRevision, type, hunks}) => {

        let res=[];
        if(diffItem.new_file){
            res.push(
                <div className="div-diff-file-title">
                    <img className="img-diff-file-title" src={imgFile}/>
                    <span className="text-diff-file-name">{diffItem.new_path}</span>
                    <span className="text-diff-file-change">&nbsp;&nbsp;new&nbsp;{diffItem.a_mode}&nbsp;->&nbsp;{diffItem.b_mode}</span>
                </div>
            )
        }else if(diffItem.deleted_file){
            res.push(
                <div className="div-diff-file-title">
                    <img className="img-diff-file-title" src={imgFile}/>
                    <span className="text-diff-file-name">{diffItem.new_path}</span>
                    <span className="text-diff-file-change">&nbsp;&nbsp;deleted&nbsp;{diffItem.a_mode}&nbsp;->&nbsp;{diffItem.b_mode}</span>
                </div>
            )
        }else if(diffItem.renamed_file){
            res.push(
                <div className="div-diff-file-title">
                    <img className="img-diff-file-title" src={imgFile}/>
                    <span className="text-diff-file-name">{diffItem.old_path}&nbsp;->&nbsp;{diffItem.new_path}</span>
                    <span className="text-diff-file-change">&nbsp;&nbsp;renamed</span>
                </div>
            )
        }else{
            res.push(
                <div className="div-diff-file-title">
                    <img className="img-diff-file-title" src={imgFile}/>
                    <span className="text-diff-file-name">{diffItem.new_path}</span>
                    <span className="text-diff-file-change">&nbsp;&nbsp;modified</span>
                </div>
            )
        }

        res.push(
            <Diff key={oldRevision + '-' + newRevision} viewType="unified" diffType={type} hunks={hunks}>
                {
                    hunks => hunks.map(hunk => [
                        <Decoration key={'decoration-' + hunk.content}>
                            {hunk.content}
                        </Decoration>,
                        <Hunk key={hunk.content} hunk={hunk}/>
                    ])
                }
            </Diff>
        );

        return res;

    };

    return (
        <div className="div-diff-file">
            {files.map(renderFile)}
        </div>
    );
};


class Commit extends React.Component{

    constructor(props){
        super(props);
        let {projectname,username,sha}=this.props.match.params;
        sha=decodeURIComponent(sha);
        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            sha:sha,
            loadingVisible:true,
            diffInfo:[],//diff,new_path,old_path,a_mode,b_mode,new_file,renamed_file,deleted_file
            commitInfo:{
                message:"",
                author_name:"",
                authored_date:"",
                authored_time:"",
                short_id:"",
                id:"",
                additions:0,
                deletions:0,
            },//message,author_name,authored_date,authored_time,short_id,id,additions,deletions
        }
    }

    componentWillMount(){
        let url=API.code+"/projects/"+this.state.projectid+"/repository/commit/diff?sha="+this.state.sha;
        Axios.get(url).then(response=>{
            this.setState({
                diffInfo:response.data,
                loadingVisible:false,
            })
        });

        url=API.code+"/projects/"+this.state.projectid+"/repository/commit?sha="+this.state.sha;
        Axios.get(url).then(response=>{
            this.setState({
                commitInfo:response.data
            })
        })
    }

    copySha(){
        copy(this.state.commitInfo.id);
        toast.success("已将commit sha复制到剪贴板")
    }

    browseFile(){

        let {username,projectname}=this.state;
        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+this.state.commitInfo.id+"/"+encodeURIComponent("/"));

    }

    render(){
        let commitInfo=this.state.commitInfo;
        return (
            <div className="commit-container">
                <div className="div-diff-author">
                    <b>Commit&nbsp;{commitInfo.short_id}</b><a onClick={this.copySha.bind(this)}><img className="img-diff-copy-sha" src={imgCopy}/></a>
                    <span className="text-diff-time">{commitInfo.authored_time}</span>提交由<div className="div-diff-avatar">{commitInfo.author_name.charAt(0).toUpperCase()}</div><b>{commitInfo.author_name}</b>
                    <a onClick={this.browseFile.bind(this)} className="btn-diff-browse-file">浏览文件</a>
                </div>
                <div className="div-diff-msg">{commitInfo.message}</div>
                <div className="div-diff-stat">正在显示<span className="text-diff-blue">{this.state.diffInfo.length}个修改文件</span>包含<span className="text-diff-green">{commitInfo.additions}行增加</span>和<span className="text-diff-red">{commitInfo.deletions}行删除</span></div>
                {
                    this.state.diffInfo.map(item=>{
                        return <DiffFile diffItem={item}/>;
                    })
                }
                <Loading visible={this.state.loadingVisible} className="loading-diff" tip={spinner}/>
             </div>
        )
    }
}

export default (props)=><Commit {...props} key={props.location.pathname} />

