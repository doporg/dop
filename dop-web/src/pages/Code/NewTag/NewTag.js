import React from 'react';
import {Select} from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import {Loading} from "@icedesign/base";
import {Feedback} from '@icedesign/base';
import Spinner from '../components/Spinner';

import './NewTag.css'

const {toast} = Feedback;
const spinner = (
    <Spinner/>
);

class NewTag extends React.Component{

    constructor(props){
        super(props);
        let {username, projectname} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            refOptions: [],
            tag_name: "",
            message:"",
            ref: "",
            loadingVisible: true,
            creatingVisible:false,
        };
    }

    componentWillMount() {
        let url = API.code + "/projects/" + this.state.projectid + "/repository/branchandtag";
        Axios.get(url).then(response => {
            this.setState({
                refOptions: response.data,
                loadingVisible: false
            })
        })
    }

    cancel() {
        this.props.history.push("/code/" + this.state.projectid + "/tags");
    }

    selectRef(value) {
        this.setState({
            ref: value
        })
    }

    changeTagName(e) {
        this.setState({
            tag_name: e.target.value
        })
    }

    changeMsg(e){
        this.setState({
            message: e.target.value
        })
    }

    addTag(){
        this.setState({
            creatingVisible:true
        });
        let url=API.code+"/projects/"+this.state.projectid+"/repository/tags";
        Axios({
            method:"POST",
            headers:{'Content-type':'application/x-www-form-urlencoded',},
            url:url,
            params:{
                tag_name:this.state.tag_name,
                ref:this.state.ref,
                message:this.state.message,
            },
        }).then(()=>{
            toast.success("创建标签成功");
            this.props.history.push("/code/"+this.state.projectid+"/tags");
        })
    }

    render(){
        return (
            <div className="new-tag-container">
                <Loading className="creating-new-tag" visible={this.state.creatingVisible} tip={spinner}>
                    <div className="div-new-tag-top">
                        新标签
                    </div>
                    <div>
                        <div className="div-new-tag-input">
                            <span className="text-new-tag-name-msg">名称</span>
                            <input onChange={this.changeTagName.bind(this)} className="input-new-tag-name-msg"/>
                        </div>
                        <div className="div-new-tag-input">
                            <span className="text-new-tag-name-msg">信息</span>
                            <input onChange={this.changeMsg.bind(this)} className="input-new-tag-name-msg"/>
                        </div>
                        <div className="div-new-tag-input">
                            <span className="text-new-tag-source">创建自</span>
                            <Loading className="loading-new-tag" visible={this.state.loadingVisible} tip={spinner}>
                                <Select onChange={this.selectRef.bind(this)} className="select-new-tag" size='large' dataSource={this.state.refOptions}/>
                            </Loading>
                        </div>
                    </div>
                    <div className="div-new-tag-submit">
                        <button onClick={this.addTag.bind(this)} className="btn-new-tag-add">创建标签</button>
                        <button onClick={this.cancel.bind(this)} className="btn-new-tag-cancel">取消</button>
                    </div>
                </Loading>
            </div>
        )
    }
}

export default (props) => <NewTag {...props} key={props.location.pathname}/>
