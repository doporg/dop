import Pagination from "../../../Application/Application";
import {Tab} from "@icedesign/base";
import React, {Component} from 'react';

export default class Applicationdetail extends Component {

    render() {
        return (
            <Tab lazyLoad={false}>
                <TabPane tab={"基本信息"} key={"basic"}>
                    基本信息页面
                </TabPane>
                <TabPane tab={"环境配置"} key={"env"}>
                    环境配置页面
                </TabPane>
                <TabPane tab={"变量管理"} key={"var"}>
                    变量管理页面
                </TabPane>
            </Tab>
        )
    }
}