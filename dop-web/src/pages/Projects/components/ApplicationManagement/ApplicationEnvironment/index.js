import React, {Component} from 'react';
import {Card, Button, Dialog, Feedback, Icon} from '@icedesign/base';
import {Link} from "react-router-dom";
import Axios from "axios";
import API from "../../../../API";
import {Col, Row} from "@alifd/next/lib/grid";
import CreateApplicationEnvironmentDialog from "../CreateApplicationEnvrionment";
import CreateApplicationVariableDialog from "../ApplicationVariable";


const Toast = Feedback.toast;

export default class ApplicationEnvironment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            appId: props.appId,
            appEnvData: [],
            showEnvDetail: props.showEnvDetail
        }

    }

    getAppEnvData() {
        let url = API.gateway + "/application-server/application/environment/" + this.state.appId;
        let _this = this;
        Axios.get(url)
            .then(function (response) {
                console.log(response)
                _this.setState({

                    appEnvData: response.data
                })
            })
            .catch(function (response) {

                console.log(response);
            })
    }

    componentDidMount() {
        this.getAppEnvData();
    }

    popupConfirm = (id) => {
        console.log(id)
        Dialog.confirm({
            content: "你确定要删除该环境吗？",
            title: "确认删除",
            onOk: this.onDelete.bind(this, id)
        });
    };

    onDelete(id) {
        let deleteUrl = API.gateway + "/application-server/application/environment/" + id
        let _this = this;
        Axios.delete(deleteUrl)
            .then(function (response) {
                Toast.success("删除成功");
                _this.getAppEnvData();
            })
            .catch(function (response) {

                console.log(response);
            })
    }

    titleRender(title, id) {
        return (
            <div>{title}
                <Icon type="ashbin" onClick={this.popupConfirm.bind(this, id)}/>
            </div>
        )
    }

    showEnvDetailFun(id) {
        console.log(id)
        this.state.showEnvDetail(id)
    }

    cardRender() {

        return (
            <Row wrap gutter="20">
                {this.state.appEnvData.map((item, index) => {
                    console.log(item)
                    return (
                        <Col l={8} key={index}>
                            <Card>
                                {this.titleRender(item.title, item.id)}

                                <Button onClick={this.showEnvDetailFun.bind(this, item.id)}>
                                    配置部署
                                </Button>

                            </Card>

                        </Col>
                    )

                })}
            </Row>
        )


    }

    refreshApplicationEnvironmentList() {
        this.getAppEnvData();
    }

    render() {

        return (
            <div>
                <CreateApplicationEnvironmentDialog type="primary"
                                                    refreshApplicationEnvironmentList={this.refreshApplicationEnvironmentList.bind(this)}
                                                    appId={this.state.appId}>
                    新建环境
                </CreateApplicationEnvironmentDialog>
                {this.cardRender()}


            </div>
        )
    }


}