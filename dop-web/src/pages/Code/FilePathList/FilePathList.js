import React from 'react';
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";

import './FilePathList.css'

import imgFile from './imgs/file.png';

export default class FilePathList extends React.Component{

    constructor(props){
        super(props);

        let {username,projectid,ref}=this.props.match.params;

        this.state={
            username:username,
            projectid:projectid,
            ref:ref,
            refOptions:[],
            pathList:[],
            showData:[],
            path:"",
        }

    }

    componentWillMount() {

        let{projectid,ref}=this.state;

        let url=API.code+"/projects/"+projectid+"/repository/branchandtag?userId="+sessionStorage.getItem("user-id");
        let self=this;
        Axios.get(url).then(response=>{
            let refOptions=response.data;

            if(this.isCommit(ref,refOptions)){
                refOptions.push(
                    {
                        value:"commit",
                        label:"commit",
                        children:[
                            {
                                value:ref,
                                label:ref
                            }
                        ]
                    }
                )
            }

            self.setState({
                refOptions:refOptions
            })
        });

        url=API.code+"/projects/"+projectid+"/repository/filepathlist?ref="+ref+"&userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
            self.setState({
                pathList:response.data,
                showData:response.data
            })
        });

    }

    isCommit(ref,refOptions){

        let branches=refOptions[0].children;
        let tags=refOptions[1].children;

        for(let i=0;i<branches.length;i++){
            if(branches[i].value===ref)
                return false;
        }

        for(let i=0;i<tags.length;i++){
            if(tags[i].value===ref)
                return false;
        }

        return true;

    }

    changeRef(value, data, extra){

        let{projectid}=this.state;

        let url=API.code+"/projects/"+projectid+"/repository/filepathlist?ref="+value+"&userId="+sessionStorage.getItem("user-id");
        let self=this;
        Axios.get(url).then(response=>{
            self.setState({
                ref:value,
                path:"",
                pathList:response.data,
                showData:response.data
            })
        });
    }

    changePath(e){

        let path=e.target.value;
        let showData=this.state.pathList.filter((item)=>{
            return item.indexOf(path)!==-1;
        });
        this.setState({
            path:path,
            showData:showData
        });

    }

    blobLink(path){

        let {username,projectid,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectid+"/blob/"+ref+"/"+encodeURIComponent(path))

        // return "http://" + window.location.host + "/#/code/"+username+"/"+projectid+"/blob/"+ref+"/"+encodeURIComponent(path);
    }

    treeRootLink(){

        let {username,projectid,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectid+"/tree/"+ref+"/"+encodeURIComponent("/"));

        // return "http://" + window.location.host + "/#/code/"+username+"/"+projectid+"/tree/"+ref+"/"+encodeURIComponent("/");

    }

    render(){
        return (
            <div className="file-list-container">
                <div className="div-file-list-top">
                    <CascaderSelect className="select-ref-file-list"  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    <a onClick={this.treeRootLink.bind(this)}>根目录</a>
                    <span className="text-file-list-separator">/</span>
                    <input className="input-file-list-search" placeholder="输入路径搜索文件" value={this.state.path} onChange={this.changePath.bind(this)}/>
                </div>
                <div>
                    {
                        this.state.showData.map(item=> {
                                return (
                                    <div className="div-file-list-item">
                                        <img src={imgFile}/>
                                        <a onClick={this.blobLink.bind(this,item)}>{item}</a>
                                    </div>
                                )
                         })
                    }
                </div>
            </div>
        )
    }
}
