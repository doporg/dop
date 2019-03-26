import React from 'react'
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import {Link} from 'react-router-dom';
import FoundationSymbol from 'foundation-symbol';
import './Tree.css'
import API from "../../API";

import imgFile from './imgs/file.png';
import imgFolder from './imgs/folder.png';
import imgSearch from './imgs/search.png';

export default class Tree extends React.Component{


    constructor(props) {

        super(props);

        let {username,projectid,path,ref}=this.props.match.params;

        path=decodeURIComponent(path);

        this.state = {
            username:username,
            projectid: projectid,
            ref: ref,
            refOptions: [],
            path: path,
            treeInfo: [],//name,type,path,commit_id,commit_msg,commit_date,commit_time
        }

    }

    componentWillMount() {

        let {username,projectid,path,ref}=this.state;

        this.loadData(username,projectid,path,ref);
    }


    loadData(username,projectid,path,ref){

        let url=API.code + "/projects/"+projectid+"/repository/tree?path="+path+"&ref="+ref+"&username="+sessionStorage.getItem("user-name");
        let self=this;
        Axios.get(url).then(response => {
            self.setState({
                username:username,
                projectid:projectid,
                path:path,
                ref:ref,
                treeInfo:response.data
            })
        });

        url=API.code+"/projects/"+projectid+"/repository/branchandtag?username="+sessionStorage.getItem("user-name");
        Axios.get(url).then(response=>{
            self.setState({
                refOptions:response.data
            })
        });
    }

    componentWillReceiveProps(nextProps, nextState) {
        // console.log('componentWillReceiveProps');

        let {username,projectid,path,ref}=nextProps.match.params;
        path=decodeURIComponent(path);
        this.loadData(username,projectid,path,ref);

    }




    changeRef(value, data, extra) {

        let {username,projectid}=this.state;
        let path="/";//切换ref将路径设为根目录，以免子目录不存在
        window.location.href = "http://" + window.location.host + "/#/code/"+username+"/"+projectid+"/tree/"+value+"/"+encodeURIComponent(path);

    }

    changePath(path){

        let {username,projectid,ref}=this.state;

        window.location.href = "http://" + window.location.host + "/#/code/"+username+"/"+projectid+"/tree/"+ref+"/"+encodeURIComponent(path);

    }

    readFile(path){

        let {username,projectid,ref}=this.state;

        window.location.href = "http://" + window.location.host + "/#/code/"+username+"/"+projectid+"/blob/"+ref+"/"+encodeURIComponent(path);

    }


    render(){

        return (
            <div className="file-container">
                <div className="div-tree-top">
                    <CascaderSelect style={{marginRight:20,width:200}}  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
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
                                    paths.push(<span className="text-tree-separator">&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;</span>);
                                    paths.push(<a onClick={this.changePath.bind(this,temp_str)}>{strs[i]}</a>);
                                }

                                return paths;
                            }else {
                                return (<a onClick={this.changePath.bind(this,"/")}>根目录</a>);
                            }
                        })()
                    }
                    <button className="btn-search-file">
                        <img src={imgSearch} className="img-tree-search"/>
                        <span className="text-tree-search">查找文件</span>
                    </button>
                </div>


                <div className="div-tree-title">
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

                        return this.state.treeInfo.map(treeNode=>{

                                return (
                                    <div className="div-tree-item">
                                        {
                                            (()=>{
                                                if(treeNode.type==="tree")
                                                    return <img src={imgFolder} className="img-tree-folder"/>;
                                                else
                                                    return <img src={imgFile} className="img-tree-file"/>;
                                            })()
                                        }
                                        <span className="text-tree-name">
                                            <a onClick={
                                                (()=>{
                                                    if(treeNode.type==="tree")
                                                        return this.changePath.bind(this,treeNode.path);
                                                    else
                                                        return this.readFile.bind(this,treeNode.path);
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
