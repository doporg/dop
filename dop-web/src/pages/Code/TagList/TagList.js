import React from 'react';
import Axios from 'axios';
import API from "../../API";
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';

import './TagList.css'

import imgTag from './imgs/tag.png';
import imgCommit from './imgs/commit.png';
import imgDelete from './imgs/delete.png';
import imgDownload from './imgs/download.png';

const spinner=(
    <Spinner/>
);

class TagList extends React.Component{

    constructor(props){
        super(props);
        const {projectname,username}=this.props.match.params;
        this.state= {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            tagList:[],
            showData:[],
            nameInput:"",
            loadingVisible:true,
        };
    }


    componentWillMount(){
        this.loadData();
    }

    loadData(){
        let url=API.code+"/projects/"+this.state.projectid+"/repository/tags";
        Axios.get(url).then(response=>{
            this.setState({
                tagList:response.data,
                showData:response.data,
                nameInput:"",
                loadingVisible:false,
            })
        })
    }

    deleteTag(tag_name){
        if(window.confirm("确认删除标签"+tag_name+"?")) {
            this.setState({
                loadingVisible:true
            });
            let url = API.code + "/projects/" + this.state.projectid + "/repository/tags?tag_name=" + tag_name;
            Axios.delete(url).then(() => {
                this.loadData();
            })
        }
    }

    changetTagName(e){
        let val=e.target.value;
        if(val!==""){
            let showData=this.state.tagList.filter(item=>{
                return item.name.indexOf(val)!==-1;
            });
            this.setState({
                nameInput:val,
                showData:showData
            })

        }else{
            this.setState({
                nameInput:val,
                showData:this.state.tagList
            })
        }
    }

    newTag(){
        this.props.history.push("/code/"+this.state.projectid+"/tags/new");
    }

    commitLink(sha){
        this.props.history.push("/code/"+this.state.projectid+"/commit/"+sha);
    }

    downloadZipLink(tag_name){
        window.open("http://gitlab.dop.clsaa.com/api/v4/projects/"+encodeURIComponent(this.state.projectid)+"/repository/archive.zip?sha="+encodeURIComponent(tag_name));
    }

    render(){
        return (
            <div className="tag-list-container">
                <div className="div-tag-list-top">
                    <span className="text-tag-list-top">Tag可以将历史中某个特定的提交标记为重要的</span>
                    <button onClick={this.newTag.bind(this)} className="btn-new-tag">+ 新建标签</button>
                    <input value={this.state.nameInput} onChange={this.changetTagName.bind(this)} className="input-tag-list-name" placeholder="输入标签名称来搜索"/>
                </div>
                <Loading visible={this.state.loadingVisible} className="loading-tag-list" tip={spinner}>
                    <div>
                        {
                            this.state.showData.map(item=>{
                                return (
                                    <div className="div-tag-list-item">
                                        <div className="div-tag-item-intro">
                                            <div className="div-tag-item-name-msg">
                                                <img className="img-tag-item-intro" src={imgTag}/>
                                                <span className="text-tag-item-name">{item.name}</span>
                                                <span className="text-tag-item-msg">{item.message}</span>
                                            </div>
                                            <div className="div-tag-item-commit">
                                                <img className="img-tag-item-intro" src={imgCommit}/>
                                                <a onClick={this.commitLink.bind(this,item.commit_id)} className="text-tag-item-primary">{item.commit_short_id}</a>
                                                <a onClick={this.commitLink.bind(this,item.commit_id)}>&nbsp;·&nbsp;{item.commit_msg}&nbsp;·&nbsp;</a>
                                                <label>{item.commit_time}</label>
                                            </div>
                                        </div>
                                        <div className="div-tag-item-operation">
                                            <a onClick={this.deleteTag.bind(this,item.name)} className="btn-tag-item-delete">
                                                <img className="img-tag-item-delete" src={imgDelete}/>
                                            </a>
                                            <a onClick={this.downloadZipLink.bind(this,item.name)} className="btn-tag-item-download">
                                                <img className="img-tag-item-download" src={imgDownload}/>
                                            </a>
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

export default (props)=><TagList {...props} key={props.location.pathname} />
