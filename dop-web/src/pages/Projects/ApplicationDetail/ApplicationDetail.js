import React, {Component} from 'react';
import {Tab} from "@icedesign/base";

import ApplicationBasicInfo from "../components/ApplicationManagement/ApplicationBasicInfo/ApplicationBasicInfo"
import ApplicationVariable from "../components/ApplicationManagement/ApplicationVariable/ApplicationVariable"
import ApplicationEnvironment from "../components/ApplicationManagement/ApplicationEnvironment/ApplicationEnvironment"
import ApplicationEnvironmentDetail
    from "../components/ApplicationManagement/ApplicationEnvironmentDetail/ApplicationEnvironmentDeatil";
import "./ApplicationDetail.scss"
import ApplicationEnvironmentLog
    from "../components/ApplicationManagement/ApplicationEnvironmentLog/ApplicationEnvironmentLog";

const TabPane = Tab.TabPane;



/**
 * 应用详情页面列表
 * @author Bowen
 */
export default class ApplicationDetail extends Component {
    static displayName = 'ApplicationDetail';

    constructor(props) {
        super(props);
        this.state = {
            //在应用列表点击应用ID跳转到该页面，通过正则表达式解析应用ID
            projectId: props.location.search.match("projectId=[0-9]+")[0].split("=")[1],
            appId: props.location.search.match("appId=[0-9]+")[0].split("=")[1],
            showPage: "envList",
            envId: undefined
        }
    }

    switchPage(page, id) {
        console.log("id", id)
        this.setState({
            showPage: page,
            envId: id === undefined ? "" : id
        })
    }

    envRender() {
        if (this.state.showPage === "envList") {
            return (<div></div>)
        } else {
            return (<ApplicationEnvironment
                projectId={this.state.projectId}
                switchPage={this.switchPage.bind(this)}
                appId={this.state.appId}/>)
        }

    }

    envLogRender() {
        if (this.state.showPage === "envLog") {
            return (<div></div>)
        } else {
            return (<ApplicationEnvironmentLog
                projectId={this.state.projectId}
                switchPage={this.switchPage.bind(this)}
                appEnvId={this.state.envId}
                appId={this.state.appId}/>)
        }

    }

    envDetailRender() {
        if (this.state.showPage === "envDetail") {
            return (
                <ApplicationEnvironmentDetail
                    switchPage={this.switchPage.bind(this)}
                    projectId={this.state.projectId}
                    appEnvId={this.state.envId}
                    appId={this.state.appId}/>
            )
        } else {
            return (<div></div>)
        }
    }


    render() {
        return (
            <div>
                {/*<TopBar*/}
                {/*extraBefore={  <Breadcrumb>*/}
                {/*<Breadcrumb.Item link="#/project">所有项目</Breadcrumb.Item>*/}
                {/*<Breadcrumb.Item*/}
                {/*link={"#/application?projectId=" + this.state.projectId}>{"项目：" + this.state.projectId}</Breadcrumb.Item>*/}
                {/*<Breadcrumb.Item*/}
                {/*link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{"应用：" + this.state.appId}</Breadcrumb.Item>*/}
                {/*</Breadcrumb>}*/}
                {/*// extraAfter={ <CreateApplicationVariableDialog*/}
                {/*//*/}
                {/*//     refreshApplicationVariableList={this.refreshApplicationVariableList.bind(this)}*/}
                {/*//     appId={this.state.appId}/>}*/}
                {/*/>*/}

                <Tab contentStyle={{padding: 20}}
                     className="Tab"
                     lazyLoad={false}>


                    <TabPane tab={"基本信息"}
                             key={"basic"}
                             className="TabPane"
                    >

                        <ApplicationBasicInfo
                            projectId={this.state.projectId}
                            appId={this.state.appId}/>
                    </TabPane>

                    <TabPane tab={"环境配置"}
                             key={"env"}
                             className="TabPane">

                        {this.envRender()}
                        {this.envDetailRender()}
                    </TabPane>

                    <TabPane tab={"变量管理"}
                             key={"var"}
                             className="TabPane"
                    >
                        <ApplicationVariable
                            projectId={this.state.projectId}
                            appId={this.state.appId}/>

                    </TabPane>

                </Tab>


            </div>
        )

    }
}
