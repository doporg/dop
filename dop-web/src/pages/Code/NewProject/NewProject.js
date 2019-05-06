import API from "../../API";
import Axios from 'axios';
import React from 'react';
import {Radio} from "@icedesign/base";
import {Checkbox} from '@icedesign/base';
import {Feedback} from '@icedesign/base';
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';

import './NewProject.css'

const {Group: RadioGroup} = Radio;
const {toast} = Feedback;
const spinner=(
    <Spinner/>
);

export default class NewProject extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: "",
            description: "",
            visibility: "private",
            initialize_with_readme: "true",
            loadingVisible:false,
        }
    }

    changeVisibility(value) {
        this.setState({
            visibility: value
        })
    }

    createProject() {

        this.setState({
            loadingVisible:true
        });

        let data={};
        data.name=this.state.name;
        data.description=this.state.description;
        data.visibility=this.state.visibility;
        data.initialize_with_readme=this.state.initialize_with_readme;
        data.userId=sessionStorage.getItem("user-id");
        Axios({
            method: "POST",
            url: API.code + "/projects",
            data: data,
            headers: {'Content-type': 'application/json',}
        }).then(response => {
            this.props.history.push("/code/"+sessionStorage.getItem("user-name")+"/"+this.state.name.toLowerCase().trim().replace(/\s/g,"-"));
        });
    }

    setName(e) {
        this.setState({
            name: e.target.value
        })
    }

    setDescription(e) {
        this.setState({
            description: e.target.value
        })
    }

    cancel(){
        this.props.history.push("/code/projects/personal");
    }


    render() {

        return (
            <Loading className="loading-new-project" visible={this.state.loadingVisible} tip={spinner}>
                <div className="new-project-container">
                    <div className="div-new-project-top">
                        新建项目
                    </div>
                    <div className="div-new-project-input">
                        <label className="label-new-project-left">名称</label>
                        <input className="input-new-project-name" onChange={this.setName.bind(this)}/>
                    </div>
                    <div className="div-new-project-input">
                        <label className="label-new-project-left">描述</label>
                        <textarea className="input-new-project-description" onChange={this.setDescription.bind(this)}/>
                    </div>
                    <div className="div-new-project-input">
                        <label className="label-new-project-left">可见等级</label>
                        <div className="div-new-project-visibility">
                            <RadioGroup value={this.state.visibility} onChange={this.changeVisibility.bind(this)}>
                                <Radio id="private" value="private">
                                    PRIVATE&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </Radio>
                                <Radio id="public" value="public">
                                    PUBLIC
                                </Radio>
                            </RadioGroup>
                        </div>
                    </div>
                    <div className="div-new-project-submit">
                        <button onClick={this.createProject.bind(this)} className="btn-new-project-create">创建项目</button>
                        <button onClick={this.cancel.bind(this)} className="btn-new-project-cancel">取消</button>
                    </div>

                </div>
            </Loading>
        );
    }
}
