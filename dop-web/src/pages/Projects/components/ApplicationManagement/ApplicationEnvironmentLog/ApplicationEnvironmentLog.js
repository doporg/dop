import React, {Component} from 'react';
import TopBar from "./topbar";
import {Breadcrumb, Button} from "@icedesign/base";
import Pagination from "./ApplicationEnvironmentLogPagination";

/**
 * 应用列表
 * @author Bowen
 *
 */
export default class ApplicationEnvironmentLog extends Component {
    static displayName = 'ApplicationEnvironmentLog';

    constructor(props) {
        super(props);

        //从项目列表点击项目ID 跳转到该页面，从URL中通过正则表达式解析出项目ID

        this.state = {
            projectId: props.projectId,
            appId: props.appId,
            appEnvId: props.appEnvId,
            switchPage: props.switchPage
        }
    }


    render() {
        return (
            <div>
                {/*创建函数的对话框和搜索框*/}
                <TopBar
                    extraBefore={<Breadcrumb>
                        <Breadcrumb.Item link="#/project">所有项目</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/projectDetail?projectId=" + this.state.projectId}>{"项目：" + this.state.projectId}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{"应用：" + this.state.appId}</Breadcrumb.Item>
                        <Breadcrumb.Item>{"应用环境：" + this.state.appEnvId + "日志"}</Breadcrumb.Item>
                    </Breadcrumb>}
                />
                {/*应用列表及分页器*/}
                <Pagination appEnvId={this.state.appEnvId}/>
                <Button onClick={this.state.switchPage.bind(this, "envList")} type="primary">返回环境列表</Button>
            </div>

        );
    }
}
