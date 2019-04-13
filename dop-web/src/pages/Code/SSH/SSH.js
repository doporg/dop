import React from 'react';
import Axios from 'axios';
import API from "../../API";


import './SSH.css';


export default class SSH extends React.Component{

    constructor(props){
        super(props);
        this.state={
            keys:[],//id,title,key,created_at
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





    render(){
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
                            <th className="th-ssh-content">内容</th>
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
                                        <td className="td-ssh-title">{item.title}</td>
                                        <td className="td-ssh-content">
                                            <div className="div-ssh-content">
                                                <pre>{item.key}</pre>
                                            </div>
                                        </td>
                                        <td className="td-ssh-time">{item.created_at}</td>
                                        <td className="td-ssh-operation"><button onClick={this.deleteKey.bind(this,item.id,item.title)} className="btn-ssh-delete">删除</button></td>
                                    </tr>
                                );
                            })
                        })()
                    }

                    </tbody>
                </table>
            </div>
        )
    }
}
