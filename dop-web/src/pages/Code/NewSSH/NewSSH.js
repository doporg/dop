import React from 'react'
import Axios from 'axios';
import API from "../../API";
import {Feedback} from '@icedesign/base';

import './NewSSH.css'

const {toast} = Feedback;

export default class NewSSH extends React.Component{

    constructor(props){
        super(props);
        this.state={
            key:"",
            title:"",
        };
    }

    changeKey(e){
        let val=e.target.value;
        let strs=val.split(" ");
        if(strs.length===3){
            this.setState({
                key:val,
                title:strs[2]
            })
        }else {
            this.setState({
                key:val
            })
        }

    }

    changeTitle(e){
        let val=e.target.value;
        this.setState({
            title:val
        })
    }

    addKey(){
        let url=API.code+"/user/keys";
        Axios({
            method:"POST",
            headers:{'Content-type':'application/json',},
            url:url,
            data:{
                key:this.state.key,
                title:this.state.title,
                userId:sessionStorage.getItem("user-id")
            },
        }).then(response=>{
            toast.success("创建ssh成功");
            this.props.history.push("/code/ssh");
        })
    }

    cancel(){
        this.props.history.push("/code/ssh");
    }

    render(){
        return (
            <div className="new-ssh-container">
                <div className="div-new-ssh-top">
                    增加 SSH 密钥
                </div>
                <div>
                    <div className="div-new-ssh-input">
                        <span className="text-new-ssh-key">公钥</span>
                        <textarea onChange={this.changeKey.bind(this)} value={this.state.key} className="input-new-ssh-key"/>
                    </div>
                    <div className="div-new-ssh-input">
                        <span className="text-new-ssh-title">标题</span>
                        <input onChange={this.changeTitle.bind(this)} value={this.state.title} className="input-new-ssh-title"/>
                    </div>
                </div>
                <div className="div-new-ssh-submit">
                    <button onClick={this.addKey.bind(this)} className="btn-new-ssh-add">增加密钥</button>
                    <button onClick={this.cancel.bind(this)} className="btn-new-ssh-cancel">取消</button>
                </div>
            </div>
        )
    }

}
