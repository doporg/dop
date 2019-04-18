import React, {Component} from 'react';
import RepoList from '../RepoList'
import {Breadcrumb,Tab} from "@icedesign/base";
import NamespaceLogList from "../../NamespaceLog/NamespaceLogList";
import API from "../../../API";
import Axios from "axios/index";


export default class Repos extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentData:[],
            totalCount:0,
            id: props.location.pathname.match("[0-9]+")[0],
            projectName:"",
            userRole:0,
        };
    }

    init(){
        let url = API.image+"/v1/projects/"+this.state.id;
        let _this = this;
        Axios.get(url, {})
            .then(function (response) {
                console.log(response.data);
                _this.setState({
                    projectName:response.data.name,
                    userRole:response.data.currentUserRole
                });
            })

    }
    componentWillMount() {
        this.init();
    }

    render() {
        return (
                <div>
                    <Breadcrumb style={{marginBottom: "10px"}}>
                        <Breadcrumb.Item style={{color: "#5485F7"}} link="#/image/projects">命名空间</Breadcrumb.Item>
                    </Breadcrumb>
                    <div className={"repoName"}>
                        {this.state.projectName}
                    </div>
                    <Tab>
                        <Tab.TabPane key={"image"} tab={"镜像仓库"}>
                            <RepoList projectId={this.state.id} />
                        </Tab.TabPane>
                        <Tab.TabPane key={"member"} tab={"成员"}>

                        </Tab.TabPane>
                        <Tab.TabPane key={"labels"} tab={"标签"}>

                        </Tab.TabPane>
                        <Tab.TabPane key={"logs"} tab={"日志"}>
                            <NamespaceLogList projectId={this.state.id} />
                        </Tab.TabPane>
                    </Tab>
                </div>
        )
    }

}






