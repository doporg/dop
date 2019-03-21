import React from 'react'
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import {Link} from 'react-router-dom';
import { Breadcrumb } from "@icedesign/base";

import './Tree.css'
import API from "../../API";

import imgFile from './imgs/file.png';
import imgFolder from './imgs/folder.png';

export default class Tree extends React.Component{


    constructor(props) {

        super(props);

        const {projectid,branch}=this.props.match.params;


        this.state = {
            projectid: projectid,
            ref: branch,
            refOptions: [],
            path: "/",
            treeInfo: [],//name,type,path,commit_id,commit_msg,commit_date,commit_time
        }

    }

    componentWillMount() {
        let url=API.code + "/projects/"+this.state.projectid+"/repository/tree?path="+this.state.path+"&ref="+this.state.ref+"&username="+sessionStorage.getItem("user-name");
        let self=this;
        Axios.get(url).then(response => {
            self.setState({
                treeInfo:response.data
            })
        });

        url=API.code+"/projects/"+this.state.projectid+"/repository/branchandtag?username="+sessionStorage.getItem("user-name");
        Axios.get(url).then(response=>{
            self.setState({
                refOptions:response.data
            })
        });
    }



    changeRef(value, data, extra) {

        let url=API.code + "/projects/"+this.state.projectid+"/repository/tree?path="+this.state.path+"&ref="+value+"&username="+sessionStorage.getItem("user-name");
        let self=this;
        Axios.get(url).then(response => {
            self.setState({
                ref:value,
                path:"/",
                treeInfo:response.data
            })
        });
    }

    changePath(path){
        let url=API.code + "/projects/"+this.state.projectid+"/repository/tree?path="+path+"&ref="+this.state.ref+"&username="+sessionStorage.getItem("user-name");
        let self=this;
        Axios.get(url).then(response => {
            self.setState({
                path:path,
                treeInfo:response.data
            })
        });
    }



    render(){

        const {projectid,branch,username}=this.props.match.params;

        return (
            <div className="file-container">
                <div className="div-tree-top">
                    <CascaderSelect style={{marginRight:20}}  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    {
                        (()=>{
                            let path=this.state.path;
                            if(path!=="/") {
                                let strs = path.split("/");
                                let paths = [];

                                paths.push(<a onClick={this.changePath.bind(this,"/")}>根目录</a>);

                                let temp_str = "";
                                for (let i = 0; i < strs.length; i++) {
                                    if (i !== 0) {
                                        temp_str += "/";
                                    }
                                    temp_str += strs[i];
                                    paths.push(<a>&nbsp;&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;&nbsp;</a>);
                                    paths.push(<a onClick={this.changePath.bind(this,temp_str)}>{strs[i]}</a>);
                                }

                                // console.log(strs);
                                // console.log(paths);

                                return paths;
                            }else {
                                return (<a onClick={this.changePath.bind(this,"/")}>根目录</a>);
                            }
                        })()
                    }
                </div>
                <div className="div-tree-item">
                    <span style={{marginRight:18}} className="text-tree-name-title">名称</span>
                    <span className="text-tree-commit-title">最后提交</span>
                    <span className="text-tree-update-title">最后更新</span>
                </div>

                {
                    (()=>{
                        //若不是根目录可以返回上一级目录
                        if(this.state.path!=="/"){

                            let path;
                            //一级目录,退回根目录
                            if(this.state.path.indexOf("/")===-1){
                                path="/";
                            }else {//多级目录，截取掉最后一级的目录
                                path=this.state.path.substring(0,this.state.path.lastIndexOf("/"));
                            }



                            return (
                                <div className="div-tree-item">
                                    <span className="text-tree-name">
                                        <a onClick={this.changePath.bind(this,path)}>..</a>
                                    </span>
                                    <span className="text-tree-commit"/>
                                    <span className="text-tree-update"/>
                                </div>
                            )
                        }
                    })()
                }
                {
                    (()=> {
                        let count;
                        if(this.state.path==="/") count=0;
                        else count = 1;

                        return this.state.treeInfo.map(treeNode=>{
                            // if(count++%2===0){
                                return (
                                    <div className={
                                        (()=>{
                                        if(count++%2===0)
                                            return "div-tree-item";
                                        else
                                            return "div-tree-item-dark";
                                        })()
                                    }>
                                        <img src={
                                            (()=>{
                                                if(treeNode.type==="tree")
                                                    return imgFolder;
                                                else
                                                    return imgFile;
                                            })()
                                        }/>
                                        <span className="text-tree-name">
                                            <a onClick={
                                                (()=>{
                                                    if(treeNode.type==="tree")
                                                        return this.changePath.bind(this,treeNode.path);
                                                })()
                                            }>{treeNode.name}</a>
                                        </span>
                                        <span className="text-tree-commit">{treeNode.commit_msg}</span>
                                        <span className="text-tree-update">{treeNode.commit_time}</span>
                                    </div>
                                )
                        })
                    })()
                }
            </div>
        );
    }
}
