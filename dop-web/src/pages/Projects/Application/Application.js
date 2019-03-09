/**
 * 应用列表（没开始用）
 * @author Bowen
 */

import React, {Component} from 'react';
import API from "../../API";
import Axios from "axios";
import TopBar from "../components/ApplicationManagement/TopBar";
import {Input} from "@icedesign/base";
import CreateApplicationDialog from "../components/ApplicationManagement/CreateApplicationDialog";
import Pagination from "../components/ApplicationManagement/ApplicationPagination";
import {Tab} from "@icedesign/base";

const TabPane = Tab.TabPane;

const tabs = [
    {tab: "基本信息", key: "home", content: ""},
    {tab: "环境配置", key: "env", content: "zzzzz"},
    {tab: "变量管理", key: "var"}
];

export default class Application extends Component {
    static displayName = 'Application';

    constructor(props) {
        super(props);
        console.log(props);
        this.state = {
            projectId: props.location.state.projectId,
            searchKey: "",
            createdApplicationNeedRefresh: false,
        }
    }

    refreshFinished() {
        this.setState({
            createdApplicationNeedRefresh: false
        })
    }

//回调函数传给创建项目的窗口，创建项目成功后调用该函数刷新项目列表
    refreshApplicationList() {
        this.setState({
            createdApplicationNeedRefresh: true
        });
        console.log("refreshProjectList");
    }

    onSearch(value) {
        this.setState({
            searchKey: value
        })
    }

    // componentWillReceiveProps() {
    //     let projectId=this.props.location.state.projectId;
    //     console.log(this.props)
    //     console.log("componentWillMount",projectId,this.state.projectId);
    // }

    render() {
        return (
            <div>
                <TopBar
                    extraBefore={
                        <CreateApplicationDialog refreshApplicationList={this.refreshApplicationList.bind(this)}
                                                 projectId={this.state.projectId}/>

                    }
                    extraAfter={
                        <Input
                            size="large"
                            placeholder="请输入应用名称进行搜索"
                            style={{width: '240px'}}
                            // hasClear
                            onChange={this.onSearch.bind(this)}
                        />
                    }

                />
                <Pagination createdApplicationNeedRefresh={this.state.createdApplicationNeedRefresh}
                            refreshFinished={this.refreshFinished.bind(this)} searchKey={this.state.searchKey}
                            projectId={this.state.projectId}/>
            </div>

        );
    }
}
