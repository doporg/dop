
import React, {Component} from 'react';
import {Table} from '@icedesign/base';
import {Grid} from '@icedesign/base';
import {Col} from "@alifd/next/lib/grid";
import {Link} from 'react-router-dom';

const {Row} = Grid;

export default class NamespaceList extends Component {

    static displayName = 'NamespaceList';


    constructor(props) {
        super();

        console.log(props.currentData)
        //接受来自分页器的参数即当前页数据
        this.state = {
            currentData: props.currentData
        };
        console.log("constructor");

    }


    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            currentData: nextProps.currentData
        });
    }

    //链接 跳转到对应的命名空间
    idRender = function (id) {
        return <Link to={"/"}
        >{id}</Link>
    }
    render() {
        return (
            <Row wrap gutter="20">
                <Col>
                    <Table dataSource={this.state.currentData}>
                        <Table.Column
                                      title="命名空间ID"
                                      dataIndex="projectId"/>

                        <Table.Column title="命名空间名称"
                                      dataIndex="name"/>

                        <Table.Column title="命名空间是否公开"
                                      dataIndex="metadata.public"/>

                        <Table.Column title="镜像仓库数量"
                                      dataIndex="repoCount"/>
                    </Table>
                </Col>
            </Row>
        );
    }

}

const styles = {
    pagination: {
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    time: {
        fontSize: '12px',
        color: 'rgba(0, 0, 0, 0.5)',
    },
};
