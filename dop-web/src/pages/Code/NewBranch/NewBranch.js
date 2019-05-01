import React from 'react';
import {Select} from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import {Loading} from "@icedesign/base";
import {Feedback} from '@icedesign/base';
import Spinner from '../components/Spinner';

import './NewBranch.css'

const {toast} = Feedback;
const spinner = (
    <Spinner/>
);

class NewBranch extends React.Component {

    constructor(props) {
        super(props);
        let {username, projectname} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            refOptions: [],
            branch: "",
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
        this.props.history.push("/code/" + this.state.projectid + "/branches");
    }

    selectRef(value) {
        this.setState({
            ref: value
        })
    }

    changeBranchName(e) {
        this.setState({
            branch: e.target.value
        })
    }

    addBranch() {
        this.setState({
            creatingVisible:true
        });
        let url=API.code+"/projects/"+this.state.projectid+"/repository/branches";
        Axios({
            method:"POST",
            headers:{'Content-type':'application/x-www-form-urlencoded',},
            url:url,
            params:{
                branch:this.state.branch,
                ref:this.state.ref
            },
        }).then(()=>{
            toast.success("创建分支成功");
            this.props.history.push("/code/"+this.state.projectid+"/branches");
        })
    }


    render() {
        return (
            <div className="new-branch-container">
                <Loading className="creating-new-branch" visible={this.state.creatingVisible} tip={spinner}>
                    <div className="div-new-branch-top">
                        新分支
                    </div>
                    <div>
                        <div className="div-new-branch-input">
                            <span className="text-new-branch-name">分支名称</span>
                            <input onChange={this.changeBranchName.bind(this)} className="input-new-branch-name"/>
                        </div>
                        <div className="div-new-branch-input">
                            <span className="text-new-branch-source">创建自</span>
                            <Loading className="loading-new-branch" visible={this.state.loadingVisible} tip={spinner}>
                                <Select onChange={this.selectRef.bind(this)} className="select-new-branch" size='large' dataSource={this.state.refOptions}/>
                            </Loading>
                        </div>
                    </div>
                    <div className="div-new-branch-submit">
                        <button onClick={this.addBranch.bind(this)} className="btn-new-branch-add">创建分支</button>
                        <button onClick={this.cancel.bind(this)} className="btn-new-branch-cancel">取消</button>
                    </div>
                </Loading>
            </div>
        )
    }
}

export default (props) => <NewBranch {...props} key={props.location.pathname}/>
