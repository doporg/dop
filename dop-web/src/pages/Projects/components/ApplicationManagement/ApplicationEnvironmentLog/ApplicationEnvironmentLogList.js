import React, {Component} from 'react';
import {Balloon, Grid, Icon, Table} from '@icedesign/base';
import {Col} from "@alifd/next/lib/grid";
import {Link} from "react-router-dom";
import "./ApplicationEnvironmentLogList.scss"

const {Row} = Grid;
/**
 * 展示部署历史的页面
 * @author Bowen
 **/

export default class ApplicationEnvironmentLogList extends Component {

    static displayName = 'ApplicationEnvironmentLogList';


    constructor(props) {
        super(props);

        //接受来自分页器的参数即当前页数据
        this.state = {
            appEnvId: props.appEnvId,
            currentData: props.currentData
        };

    }

    //接受来自分页器的参数即当前页数据
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            currentData: nextProps.currentData
        });
    }

    //渲染表格中的ID项，添加超链接至该项目ID下的应用ID列表
    codeIdRender(id) {
        let _this = this;
        return <Link to={""}
        >{id}</Link>
    }

    //渲染表格中的ID项，添加超链接至该项目ID下的应用ID列表
    imageIdRender(id) {
        let _this = this;
        return <Link to={""}
        >{id}</Link>
    }


    render() {
        const defaultTrigger = (
            <div>查看环境信息</div>
        );
        const renderOpr = (value, index, record) => {
            console.log("record:", record, value)

            return <div>{record.ctime}
                <Icon onClick={this.popupConfirm.bind(this, record.id)} type="ashbin" className="delete-icon"/>

            </div>
        };
        const envRender = (envData) => {
            return (<Balloon trigger={defaultTrigger} closable={false}>
                {envData}
            </Balloon>)
        };

        return (
            <Row wrap gutter="20">
                <Col>
                    <Table dataSource={this.state.currentData}>
                        <Table.Column title="运行ID"
                                      dataIndex="runningId"/>

                        <Table.Column
                            cell={this.codeIdRender.bind(this)}
                            title="代码ID"
                            dataIndex="codeId"/>

                        <Table.Column cell={this.imageIdRender.bind(this)}
                                      title="版本号"
                                      dataIndex="buildTag"/>

                        <Table.Column
                            cell={envRender.bind(this)}
                            title="环境信息"
                            dataIndex="envInfo"/>

                        <Table.Column title="部署时间"
                                      dataIndex="ctime"/>

                        <Table.Column title="部署人"
                                      dataIndex="userId"/>

                        <Table.Column title="运行状态"
                                      dataIndex="status"/>
                    </Table>
                </Col>
            </Row>
        );
    }

}

