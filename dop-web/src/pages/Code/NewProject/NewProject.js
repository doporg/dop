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

    changeReadMe() {
        this.setState({
            initialize_with_readme: this.state.initialize_with_readme === "false" ? "true" : "false"
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
            toast.success("创建项目成功");
            this.props.history.push("/code/"+sessionStorage.getItem("user-name")+"/"+this.state.name.toLowerCase());
        }).catch(error => {
            toast.error("创建失败");
        })
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


    render() {

        return (
            <Loading className="loading-new-project" visible={this.state.loadingVisible} tip={spinner}>
                <div className="new-container">
                    <div className="div_title">
                        新建项目
                    </div>
                    <div className="div_input">
                        <label className="label_left">名称</label>
                        <input className="input_name" onChange={this.setName.bind(this)}/>
                    </div>
                    <div className="div_input">
                        <label className="label_left">描述</label>
                        <textarea className="input_description" onChange={this.setDescription.bind(this)}/>
                    </div>
                    <div className="div_input">
                        <label className="label_left">可见等级</label>
                        <div className="div_visibility">
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
                    <div className="div_input">
                        <label className="label_left">其他</label>
                        <div className="div_visibility">
                            <Checkbox defaultChecked onClick={this.changeReadMe.bind(this)}>自动创建README.md</Checkbox>
                        </div>
                    </div>
                    <button onClick={this.createProject.bind(this)} className="btn_create">创建项目</button>
                </div>
            </Loading>
        );
    }
}
