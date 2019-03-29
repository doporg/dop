import React, {Component} from 'react';
import {Tab} from "@icedesign/base";

import ApplicationBasicInfo from "../components/ApplicationManagement/ApplicationBasicInfo"
import ApplicationVariable from "../components/ApplicationManagement/ApplicationVariable"
import ApplicationEnvironment from "../components/ApplicationManagement/ApplicationEnvironment"
import ApplicationEnvironmentDetail from "../components/ApplicationManagement/ApplicationEnvironmentDetail";

const TabPane = Tab.TabPane;
const styles = {
    container: {
        position: 'fixed',
        top: '62px',
        left: '240px',
        right: '0px',
        display: 'block',
        // alignItems: 'center',
        justifyContent: 'center',
        textAlign: "center",
        // height: '70%',
        padding: '0 20px',
        zIndex: '99',
        background: '#fff',
        boxShadow: 'rgba(0, 0, 0, 0.2) 2px 0px 4px',

    },
    tabPane: {}
};


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
            showEnvDetail: false,
            envId: undefined
        }
    }

    toggleEnvDetail(id) {
        console.log("id", id)
        this.setState({
            showEnvDetail: !this.state.showEnvDetail,
            envId: id == undefined ? "" : id
        })
    }

    envRender() {
        if (this.state.showEnvDetail) {
            return (<div></div>)
        } else {
            return (<ApplicationEnvironment
                projectId={this.state.projectId}
                showEnvDetail={this.toggleEnvDetail.bind(this)}
                appId={this.state.appId}/>)
        }

    }

    envDetailRender() {
        if (this.state.showEnvDetail) {
            return (
                <ApplicationEnvironmentDetail
                    toggleEnvDetail={this.toggleEnvDetail.bind(this)}
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

                <Tab contentStyle={{padding: 20}}
                     style={styles.container}
                     lazyLoad={false}>

                    <TabPane tab={"基本信息"}
                             key={"basic"}
                             style={{textAlign: "center"}}
                    >
                        <ApplicationBasicInfo
                            projectId={this.state.projectId}
                            appId={this.state.appId}/>
                    </TabPane>

                    <TabPane tab={"环境配置"}
                             key={"env"}>
                        {this.envRender()}
                        {this.envDetailRender()}
                    </TabPane>

                    <TabPane tab={"变量管理"}
                             key={"var"}
                             style={{textAlign: "center"}}
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
