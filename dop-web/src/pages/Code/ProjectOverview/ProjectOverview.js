import React from 'react';
import FoundationSymbol from 'foundation-symbol';
import API from "../../API";
import Axios from 'axios';
import copy from 'copy-to-clipboard';
import {Feedback} from '@icedesign/base';


import './ProjectOverview.css'

import imgPublic from './imgs/public.png'
import imgPrivate from './imgs/private.png'
import imgStar from './imgs/star.png'
import imgBranch from './imgs/branch.png'
import imgDownload from './imgs/download.png'

const {toast} = Feedback;

const server="http://localhost:13900";

export default class ProjectOverview extends React.Component{

    constructor(props){

        super(props);
        const {username,projectid} = this.props.match.params;

        this.state={
            username:username,
            projectid:projectid,
            url:"",
            projectInfo:{
                commit_count: 0,
                description: "",
                file_size: "",
                forks_count: 0,
                http_url_to_repo: "",
                id: projectid,
                name: "",
                ssh_url_to_repo: "",
                star_count: 0,
                tag_count: 0,
                branch_count:0,
                visibility: "public"
            }

        }
        ;

    }

    componentDidMount(){

        let url =  server+ "/projects/"+this.state.projectid;
        let self = this;
        Axios.get(url).then((response) => {
            self.setState({url:response.data.http_url_to_repo,projectInfo:response.data});
        });
    }

    star(){
        const curUser = window.sessionStorage.getItem("user-name");
        console.log(curUser);
        let url =  server+ "/projects/"+this.state.projectid+"/star/"+curUser;
        let self = this;
        Axios.post(url).then((response) => {
            url=server+ "/projects/"+this.state.projectid;
            Axios.get(url).then((response) => {
                self.setState({projectInfo:response.data});
            });
        });

    }


    changeSSH(){
        this.setState({url:this.state.projectInfo.ssh_url_to_repo});
    }

    changeHttp(){
        this.setState({url:this.state.projectInfo.http_url_to_repo});
    }

    copyUrl = () => {
        copy(this.state.url);
        toast.success("已复制到剪贴板");
        // message.success('复制成功，如果失败，请在输入框内手动复制.');
    };


    render(){

        const projectInfo=this.state.projectInfo;

        return (

            <div className="container">
                {
                    (() =>{
                        if(projectInfo.visibility==="public"){
                            return (
                                <button className="btn btn_visibility">
                                    <img src={imgPublic}/>PUBLIC
                                </button>
                            )
                        }else {
                            return (
                                <button className="btn btn_visibility">
                                    <img src={imgPrivate}/>PRIVATE
                                </button>
                            )
                        }
                    })()
                }
                <button className="btn btn_edit">
                    <FoundationSymbol size="small" type="edit2" >
                    </FoundationSymbol>
                </button>
                <div className="project_avatar">
                    {projectInfo.name.substring(0,1).toUpperCase()}
                </div>

                <div>
                    <p className="text_title">{projectInfo.name}</p>
                    <p className="text_description">{projectInfo.description}</p>
                </div>

                <div className="div1">
                    <button className="btn btn_star" onClick={this.star.bind(this)}>
                        <img src={imgStar}/>{projectInfo.star_count}
                    </button>
                    <button className="btn btn_branch">
                        <img src={imgBranch}/>{projectInfo.forks_count}
                    </button>

                    <button className="btn btn_ssh" onClick={this.changeSSH.bind(this)}>
                        SSH
                    </button>
                    <button className="btn btn_http" onClick={this.changeHttp.bind(this)}>
                        HTTP
                    </button>
                    <input className="input_url" type="text" value={this.state.url}/>

                    <button className="btn btn_copy" onClick={this.copyUrl.bind(this)}>
                        <FoundationSymbol size="small" type="copy" >
                        </FoundationSymbol>
                    </button>
                    <button className="btn btn_download">
                        <img src={imgDownload}/>
                    </button>
                </div>

                <div className="div2">
                    <span>{projectInfo.commit_count}次提交</span>
                    <span>{projectInfo.branch_count}个分支</span>
                    <span>{projectInfo.tag_count}个标签</span>
                    <span>{projectInfo.file_size}</span>
                </div>

            </div>
        );
    }
}

