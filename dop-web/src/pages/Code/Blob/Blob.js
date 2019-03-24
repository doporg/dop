import React from 'react'
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";

import './Blob.css'

import imgSearch from './imgs/search.png';
import imgFile from './imgs/file.png';

export default class Blob extends React.Component{


    constructor(props){
        super(props);

        let {username,projectid,path,ref} =this.props.match.params;

        path=decodeURIComponent(path);

        this.state={
            username:username,
            projectid:projectid,
            path:path,
            ref:ref,
            refOptions:[],
            lineCount:0,
            blobInfo:{}
        }
    }

    changeCode(e){
        const val=e.target.value;
        let lineCount=val.split("\n").length;
        let blobInfo=this.state.blobInfo;
        blobInfo.file_content=val;
        this.setState({
            lineCount:lineCount,
            blobInfo:blobInfo
        })
    }

    codeHeight(){
        let lineCount=this.state.lineCount;
        return {height:lineCount*20};
    }

    componentWillMount(){
        let {projectid,path,ref}=this.state;

        let url=API.code+"/projects/"+projectid+"/repository/branchandtag?username="+sessionStorage.getItem("user-name");
        let self=this;
        Axios.get(url).then(response=>{
            self.setState({
                refOptions:response.data
            })
        });

        url=API.code+"/projects/"+projectid+"/repository/blob?file_path="+path+"&ref="+ref+"&username="+sessionStorage.getItem("user-name");
        Axios.get(url).then(response=>{
            // console.log(response.data);
            self.setState({
                blobInfo:response.data,
                lineCount:response.data.file_content.split("\n").length
            })
        })

    }


    componentWillReceiveProps(nextProps, nextState) {

        let {username,projectid,path,ref}=nextProps.match.params;

        path=decodeURIComponent(path);

        let url=API.code+"/projects/"+projectid+"/repository/blob?file_path="+path+"&ref="+ref+"&username="+sessionStorage.getItem("user-name");
        let self=this;
        Axios.get(url).then(response=>{
            // console.log(response.data);
            self.setState({
                username:username,
                projectid:projectid,
                path:path,
                ref:ref,
                blobInfo:response.data,
                lineCount:response.data.file_content.split("\n").length
            })
        })
    }



    changeRef(value, data, extra){

        let {username,projectid,path}=this.state;

        window.location.href = "http://" + window.location.host + "/#/code/"+username+"/"+projectid+"/blob/"+value+"/"+encodeURIComponent(path);
    }

    changePath(path){

        let {username,projectid,ref}=this.state;

        window.location.href = "http://" + window.location.host + "/#/code/"+username+"/"+projectid+"/tree/"+ref+"/"+encodeURIComponent(path);

    }

    editFile(){

        let {username,path,projectid,ref}=this.state;

        window.location.href = "http://" + window.location.host + "/#/code/"+username+"/"+projectid+"/edit/"+ref+"/"+encodeURIComponent(path);

    }

    render(){

        return (
            <div className="blob-container">
                <div className="div-blob-top">
                    <CascaderSelect style={{marginRight:20,width:200}}  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    {
                        (()=>{
                            let path=this.state.path;
                            let strs = path.split("/");
                            let paths = [];

                            paths.push(<a onClick={this.changePath.bind(this,"/")}>根目录</a>);

                            let temp_str = "";
                            for (let i = 0; i < strs.length; i++) {
                                if (i !== 0) {
                                    temp_str += "/";
                                }
                                temp_str += strs[i];
                                paths.push(<span className="text-blob-separator">&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;</span>);
                                if(i!==strs.length-1)
                                    paths.push(<a onClick={this.changePath.bind(this,temp_str)}>{strs[i]}</a>);
                                else
                                    paths.push(<a>{strs[i]}</a>)
                            }

                            return paths;
                        })()
                    }
                    <button className="btn-search-file">
                        <img src={imgSearch} className="img-blob-search"/>
                        <span className="text-blob-search">查找文件</span>
                    </button>
                </div>
                <div className="div-blob-title">
                    <img src={imgFile}/>
                    <span className="text-file-name">{this.state.blobInfo.file_name}</span>
                    <span className="text-file-size">&nbsp;&nbsp;{this.state.blobInfo.file_size}</span>
                    <div className="div-blob-operation">
                        <button className="btn-blob-edit" onClick={this.editFile.bind(this)}>编辑</button>
                        <button className="btn-blob-delete">删除</button>
                    </div>
                </div>
                 <div className="div-line-number">

                   {
                        (()=> {
                            let res = [];
                            for (let i = 1; i <= this.state.lineCount; i++) {
                                res.push(<span className="text-line-number">{i}</span>)
                            }
                            return res;
                        })()
                    }

                </div>
                <textarea value={this.state.blobInfo.file_content} style={this.codeHeight()}  className="input-area-code" onChange={this.changeCode.bind(this)} readOnly>
                </textarea>

            </div>
        );
    }

}
