import React, {Component} from 'react';
import {Tab} from "@icedesign/base";
import ProjectBasicInfo from "../ProjectBasicInfo/ProjectBasicInfo"
import Application from "../../../Application/Application"

const TabPane = Tab.TabPane;


/**
 * 项目详情信息页面
 * @author Bowen
 */
export default class ProjectDetail extends Component {
    static displayName = 'ProjectDetail';

    constructor(props) {
        super(props);
        this.state = {
            //在应用列表点击应用ID跳转到该页面，通过正则表达式解析应用id
            projectId: props.location.search.match("projectId=[0-9]+")[0].split("=")[1]
        }
    }


    render() {
        return (
            <Tab contentStyle={{padding: 20}}
                 className="Tab"
                 lazyLoad={false}>


                <TabPane tab={"项目信息"}
                         key={"info"}
                         className="TabPane"
                >

                    <ProjectBasicInfo
                        projectId={this.state.projectId}/>
                </TabPane>


                <TabPane tab={"应用"}
                         key={"app"}
                         className="TabPane"
                >
                    <Application
                        projectId={this.state.projectId}/>

                </TabPane>

            </Tab>

        )

    }
}
