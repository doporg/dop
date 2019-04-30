import React from 'react';
import API from "../../API";
import Axios from 'axios';

import './MergeRequestList.css';


class MergeRequestList extends React.Component{


    constructor(props) {
        super(props);
        const {projectname, username,state} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            state:state,
            mrListInfo:[],
        }
    }

    componentWillMount(){
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests?state="+this.state.state;
        Axios.get(url).then(response=>{
            this.setState({
                mrListInfo:response.data
            });
        })
    }

    selectItemClass(state){
        if(state===this.state.state)
            return "mr-list-menu-item-active";
        else
            return "mr-list-menu-item";
    }

    changeState(state){
        this.props.history.push("/code/"+this.state.projectid+"/merge_requests/"+state);
    }

    newMergeRequestLink(){
        this.props.history.push("/code/"+this.state.projectid+"/merge_requests/new");
    }

    render(){
        return (
            <div className="mr-list-container">
                <div className="div-mr-list-top">
                    <ul>
                        <li>
                            <a className={this.selectItemClass("opened")} onClick={this.changeState.bind(this,"opened")}>
                                <span>处理中</span>
                            </a>
                        </li>
                        <li>
                            <a className={this.selectItemClass("merged")} onClick={this.changeState.bind(this,"merged")}>
                                <span>已合并</span>
                            </a>
                        </li>
                        <li>
                            <a className={this.selectItemClass("closed")} onClick={this.changeState.bind(this,"closed")}>
                                <span>已关闭</span>
                            </a>
                        </li>
                    </ul>
                    <button onClick={this.newMergeRequestLink.bind(this)} className="btn-new-mr">+新建合并请求</button>
                </div>
                <div>
                    {
                        this.state.mrListInfo.map(item=>{
                            return (
                                <div className="mr-list-item">
                                    <div className="mr-list-item-center">
                                        <div className="mr-list-item-title">
                                            {item.title}
                                        </div>
                                        <div className="mr-list-item-time">
                                            <span className="text-list-time-left">!{item.iid}&nbsp;·&nbsp;在{item.created_at}创建由&nbsp;<b>{item.created_by}</b></span>
                                            <span className="text-list-time-right">在{item.updated_at}更新</span>
                                        </div>
                                    </div>
                                </div>
                            )
                        })
                    }

                </div>
            </div>
        )
    }
}

export default (props)=><MergeRequestList {...props} key={props.location.pathname} />
