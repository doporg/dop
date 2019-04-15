import React, {Component} from 'react';
import {Grid, Table} from '@icedesign/base';
import {Col} from "@alifd/next/lib/grid";
import {Link} from 'react-router-dom';
import "./ProjectList.scss"

const {Row} = Grid;


/**
 * 展示项目列表
 * @author Bowen
 **/

export default class ProjectList extends Component {

    static displayName = 'ProjectList';


    constructor(props) {
        super(props);

        console.log(props.currentData)
        //接受来自分页器的参数即当前页数据
        this.state = {
            currentData: props.currentData
        };
        console.log("constructor");

    }


    componentWillReceiveProps(nextProps, nextContext) {
        // let url = API.application + '/projects';
        // for (let i = 0; i < nextProps.currentData.length; i++) {
        //     let tmpTime = nextProps.currentData[i].ctime;
        //     if (tmpTime[0].length < 4) break;
        //     nextProps.currentData[i].ctime = tmpTime[0] + "/" + tmpTime[1] + "/" + tmpTime[2] + " " + tmpTime[3] + ":" + tmpTime[4];
        // }
        this.setState({
            currentData: nextProps.currentData
        });
    }

    //链接 点击后跳转到对应projectId所包含的所有应用列表的页面
    idRender = function (id) {
        return <Link to={"/application?projectId=" + id}
        >{id}</Link>
    }
    render() {
        return (
            <Row wrap gutter="20">
                <Col>
                    <Table dataSource={this.state.currentData}>
                        <Table.Column cell={this.idRender}
                                      title="项目ID"
                                      dataIndex="id"/>

                        <Table.Column title="项目名称"
                                      dataIndex="title"/>

                        <Table.Column title="创建人"
                                      dataIndex="cuserName"/>

                        <Table.Column title="创建时间"
                                      dataIndex="ctime"/>
                    </Table>
                </Col>
            </Row>
        );
    }

}

