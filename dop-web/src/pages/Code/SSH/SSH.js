import React from 'react';
import Axios from 'axios';
import API from "../../API";
import { Dialog } from '@icedesign/base';


import './SSH.css';


export default class SSH extends React.Component{

    constructor(props){
        super(props);
        this.state={
            keys:[],//id,title,key,created_at
            title:"",
            key:"",
            visible:false,
        }
    }


    componentWillMount(){
        this.loadData();
    }

    loadData(){
        let url=API.code+"/user/keys?userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
            this.setState({
                keys:response.data,
                title:"",
                key:"",
                visible:false,
            })
        })
    }

    deleteKey(id,title){
        if(window.confirm("确认删除SSHKey:"+title)) {
            let url = API.code + "/user/keys/" + id + "?userId=" + sessionStorage.getItem("user-id");
            Axios.delete(url).then(response => {
                this.loadData();
            })
        }
    }

    newKey(){
        this.props.history.push("/code/ssh/new");
    }


    readmeLink(){
        this.props.history.push("/code/ssh/README");
    }

    onOpen(title,key){
        this.setState({
            title:title,
            key:key,
            visible:true,
        })
    }

    onClose(){
        this.setState({
            visible:false
        })
    }


    render(){
        const footer = (
            <div></div>
        );
        return (
            <div className="ssh-container">
                <div className="div-ssh-top">
                    <span className="text-ssh-intro">在增加 SSH 密钥之前需要先</span>
                    <a onClick={this.readmeLink.bind(this)} className="text-ssh-help">生成密钥。</a>
                    <button onClick={this.newKey.bind(this)} className="btn-ssh-add">+ 增加 SSH 密钥</button>
                </div>
                <table className="table-ssh-list">
                    <thead>
                        <tr className="tr-ssh-head">
                            <th className="th-ssh-title">标题</th>
                            <th className="th-ssh-time">创建时间</th>
                            <th className="th-ssh-operation"> </th>
                        </tr>
                    </thead>
                    <tbody>
                    {
                        (()=>{
                            return this.state.keys.map((item)=>{
                                return (
                                    <tr className="tr-ssh-body">
                                        <td className="td-ssh-title"><a onClick={this.onOpen.bind(this,item.title,item.key)}>{item.title}</a></td>
                                        <td className="td-ssh-time">{item.created_at}</td>
                                        <td className="td-ssh-operation"><button onClick={this.deleteKey.bind(this,item.id,item.title)} className="btn-ssh-delete">删除</button></td>
                                    </tr>
                                );
                            })
                        })()
                    }

                    </tbody>
                </table>
                <Dialog
                    visible={this.state.visible}
                    onClose={this.onClose.bind(this)}
                    title={this.state.title}
                    footer={footer}
                >
                    <div className="ssh-dialog">
                        {this.state.key}
                    </div>
                </Dialog>
            </div>
        )
    }
}
