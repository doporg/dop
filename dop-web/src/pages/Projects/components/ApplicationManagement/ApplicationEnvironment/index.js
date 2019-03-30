import React, {Component} from 'react';
import {Breadcrumb, Button, Card, Dialog, Feedback, Icon, Loading} from '@icedesign/base';
import Axios from "axios";
import API from "../../../../API";
import {Col, Row} from "@alifd/next/lib/grid";
import CreateApplicationEnvironmentDialog from "../CreateApplicationEnvrionment";
import TopBar from "./topbar"


const Toast = Feedback.toast;

export default class ApplicationEnvironment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            appId: props.appId,
            appEnvData: [],
            showEnvDetail: props.showEnvDetail,
            loading: true,
            projectId: props.projectId
        }

    }

    getAppEnvData() {
        this.setState({
            loading: true
        })
        let url = API.gateway + "/application-server/app/" + this.state.appId + "/allEnv"
        let _this = this;
        Axios.get(url)
            .then(function (response) {
                console.log(response)
                _this.setState({

                    appEnvData: response.data,
                    loading: false
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
        let deleteUrl = API.gateway + "/application-server/app/env/" + id
        let _this = this;
        this.setState({
            loading: true
        })
        Axios.delete(deleteUrl)
            .then(function (response) {
                _this.getAppEnvData();
                Toast.success("删除成功");
            })
            .catch(function (response) {

                console.log(response);
            })
    }

    titleRender(title, id) {
        return (
            <div>{title}
                <Icon type="ashbin" onClick={this.popupConfirm.bind(this, id)}
                      style={{
                          cursor: "pointer",
                          color: "#FFA003",
                          margin: "5px"
                      }}/>
            </div>
        )
    }

    showEnvDetailFun(id) {
        console.log(id)
        this.state.showEnvDetail(id)
    }

    getYaml(id) {


        let url = API.gateway + "/application-server/app/env/" + {id} + "/yamlFile"
        Axios.get(url)
            .then((response) => {
                console.log(response)///yaml
            })
            .catch((response) => {
                console.log(response)
            })

    }

    cardRender() {

        return (


            <Row wrap gutter="20">
                {this.state.appEnvData.map((item, index) => {
                    console.log(item)
                    return (
                        <Col style={{width: "100%"}} key={index}>
                            <Card style={{width: "100%", height: "70%"}}>
                                {this.titleRender(item.title, item.id)}
                                {/*<Button onClick={this.getYaml.bind(this, item.id)}>*/}
                                {/*部署主干*/}
                                {/*</Button>*/}
                                <Button onClick={this.showEnvDetailFun.bind(this, item.id)}
                                        style={{margin: "20px"}}>
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

            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF"
            >


                <div>
                    <TopBar

                        style={{zIndex: "100"}}
                        extraBefore={<Breadcrumb>
                            <Breadcrumb.Item link="#/project">所有项目</Breadcrumb.Item>
                            <Breadcrumb.Item
                                link={"#/application?projectId=" + this.state.projectId}>{"项目：" + this.state.projectId}</Breadcrumb.Item>
                            <Breadcrumb.Item
                                link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{"应用：" + this.state.appId}</Breadcrumb.Item>
                        </Breadcrumb>}
                        extraAfter={<CreateApplicationEnvironmentDialog type="primary"
                                                                        style={{margin: "20px"}}
                                                                        refreshApplicationEnvironmentList={this.refreshApplicationEnvironmentList.bind(this)}
                                                                        appId={this.state.appId}>
                            新建环境
                        </CreateApplicationEnvironmentDialog>}
                    />


                    {this.cardRender()}


                </div>
            </Loading>
        )
    }


}