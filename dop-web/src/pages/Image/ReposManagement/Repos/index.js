import React, {Component} from 'react';
import RepoList from '../RepoList'
import {Breadcrumb,Tab} from "@icedesign/base";


export default class Repos extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentData:[],
            totalCount:0,
            id: props.location.pathname.match("[0-9]+")[0]
        };

    }


    render() {
        return (
                <div>
                    <Breadcrumb style={{marginBottom: "10px"}}>
                        <Breadcrumb.Item style={{color: "#5485F7"}} link="#/image/projects">命名空间</Breadcrumb.Item>
                    </Breadcrumb>
                    <div className={"repoName"}>
                        Dop
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

                        </Tab.TabPane>
                    </Tab>
                </div>
        )
    }

}






