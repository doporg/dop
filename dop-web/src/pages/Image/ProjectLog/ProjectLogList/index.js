
import React, {Component} from 'react';
import {Table} from '@icedesign/base';
import {Grid} from '@icedesign/base';
import {Col} from "@alifd/next/lib/grid";

const {Row} = Grid;

//用于展示
export default class NamespaceLogList extends Component {

    static displayName = 'NamespaceLogList';


    constructor(props) {
        super(props);
        this.state = {
            currentData: [],
        };

    }


    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            currentData: nextProps.currentData
        });
    }

    render() {
        return (
            <Row wrap gutter="20">
                <Col>
                    <Table dataSource={this.state.currentData}>
                        <Table.Column title="用户名"
                                      dataIndex="projectId"/>

                        <Table.Column title="镜像名称"
                                      dataIndex="name"/>

                        <Table.Column title="标签"
                                      dataIndex="metadata.public"/>

                        <Table.Column title="操作"
                                      dataIndex="repoCount"/>
                                      
                        <Table.Column title="时间戳"
                                      dataIndex="time"/>
                    </Table>
                </Col>
            </Row>
        );
    }


};
