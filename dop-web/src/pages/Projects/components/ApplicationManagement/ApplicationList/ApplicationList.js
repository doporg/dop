import React, {Component} from 'react';
import {Dialog, Feedback, Grid, Icon, Table} from '@icedesign/base';
import API from "../../../../API.js"
import {Col} from "@alifd/next/lib/grid";
import Axios from "axios";
import {Link} from "react-router-dom";
import "./ApplicationList.scss"

const {Row} = Grid;
const Toast = Feedback.toast;
/**
 * 展示应用的列表
 * @author Bowen
 **/

export default class ApplicationList extends Component {

    static displayName = 'ApplicationList';


    constructor(props) {
        super(props);

        console.log("applicationList", props)
        //接受来自分页器的参数即当前页数据
        this.state = {
            projectId: props.projectId,
            isVisible: false,
            currentData: props.currentData,
            deletedCallRefresh: props.deletedCallRefresh,
            searchKey: props.searchKey == undefined ? "" : props.searchKey
        };
        console.log("constructor");

    }

    //接受来自分页器的参数即当前页数据
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            currentData: nextProps.currentData
        });
    }

    popupConfirm = (id) => {
        console.log(id)
        Dialog.confirm({
            content: "你确定要删除该应用吗？",
            title: "确认删除",
            onOk: this.onDelete.bind(this, id)
        });
    };

    //删除按钮响应函数
    onDelete = (id) => {
        let url = API.application + '/app/' + id;
        let _this = this;
        console.log("id", id)
        Axios.delete(url)
            .then(function (response) {
                Toast.success("删除成功")
                _this.state.deletedCallRefresh();
                }
            )
            .catch(function (error) {
                console.log(error);
            });


    }

    //渲染表格中的ID项，添加超链接至该项目ID下的应用ID列表
    idRender(id) {
        let _this = this
        return <Link to={"/applicationDetail?appId=" + id + "&projectId=" + _this.state.projectId}
        >{id}</Link>
    }

    render() {
        const renderOpr = (value, index, record) => {
            let id = record.id;
            console.log("record:", record, value)

            return <div>{record.ctime}
                <Icon onClick={this.popupConfirm.bind(this, record.id)} type="ashbin" className="delete-icon"/>

            </div>
        }

        return (
            <Row wrap gutter="20">
                <Col>
                    <Table dataSource={this.state.currentData}>
                        <Table.Column cell={this.idRender.bind(this)}
                                      title="ID"
                                      dataIndex="id"/>

                        <Table.Column title="应用名称"
                                      dataIndex="title"/>

                        <Table.Column title="拥有者"
                                      dataIndex="ouser"/>

                        <Table.Column cell={renderOpr}
                                      title="创建时间"
                                      dataIndex="ctime"/>
                    </Table>
                </Col>
            </Row>
        );
    }

}

