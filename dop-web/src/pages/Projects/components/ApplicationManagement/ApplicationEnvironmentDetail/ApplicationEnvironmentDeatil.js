import {Breadcrumb, Button, Field, Form, Loading, Select} from "@icedesign/base";
import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";
import PipelineBindPage from "../PipelineBindPage/PipelineBindPage"
import ClusterInfoForm from "../ClusterInfoForm/ClusterInfoForm"
import TopBar from "./topbar";
import "./ApplicationEnvironmentDetail.scss"

const FormItem = Form.Item;
const Option = Select.Option;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};


/**
 * 展示应用环境详情的列表
 * @author Bowen
 **/
export default class ApplicationEnvironmentDetail extends Component {

    constructor(props) {
        super(props);
        console.log(props)
        this.field = new Field(this)
        this.state = {
            appId: props.appId,
            appEnvId: props.appEnvId,
            envData: [],
            loading: true,
            projectId: props.projectId,
            switchPage: props.switchPage
        }
    }

    getenvData() {
        let url = API.gateway + "/application-server/app/env/" + this.state.appEnvId;
        Axios.get(url)
            .then((response) => {
                console.log(response)
                if (response.data === "") {
                    this.setState({
                        envData: [],
                        editMode: true,
                        loading: false
                    })
                } else {
                    this.setState({
                        envData: response.data,
                        editMode: false,
                        loading: false
                    })
                }
            })
    }





    componentDidMount() {
        this.getenvData();

    }

    onChange(e, value) {
        this.field.setValue("deploymentStrategy", value)
    }

    clusterInfoRender() {
        if (this.field.getValue('deploymentStrategy') === 'KUBERNETES' && this.state.appEnvId !== undefined) {
            return (<ClusterInfoForm

                appEnvId={this.state.appEnvId}/>)
        }
    }
    render() {
        const {init} = this.field
        return (
            <div>

                <TopBar
                    extraBefore={<Breadcrumb>
                        <Breadcrumb.Item link="#/project">所有项目</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/application?projectId=" + this.state.projectId}>{"项目：" + this.state.projectId}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{"应用：" + this.state.appId}</Breadcrumb.Item>
                        <Breadcrumb.Item>{"应用环境：" + this.state.appEnvId}</Breadcrumb.Item>
                    </Breadcrumb>}
                />
                <div className="form-container">
                    <Loading className="form-loading" visible={this.state.loading} shape="dot-circle"
                             color="#2077FF"
                    >
                        <Form className="deployment-strategy-form">

                            <FormItem label="部署方式:"
                                      className="deployment-strategy-form"
                              {...formItemLayout}
                              validateStatus={this.field.getError("deploymentStrategy") ? "error" : ""}
                              help={this.field.getError("deploymentStrategy") ? "请选择部署方式" : ""}
                    >

                        <Select placeholder="部署方式"
                                onChange={this.onChange.bind(this)}

                                {...init('deploymentStrategy', {
                                    initValue: this.state.envData.deploymentStrategy,
                                    rules: [{required: true, message: "该项不能为空"}]
                                })}>
                            <Option value="KUBERNETES">Kubernetes部署</Option>
                            <Option value="test">测试</Option>
                        </Select>

                    </FormItem>




                </Form>
                    </Loading>
                <PipelineBindPage appId={this.state.appId} appEnvId={this.state.appEnvId}/>
                {this.clusterInfoRender()}

                    <Button onClick={this.state.switchPage.bind(this, "envList")} type="primary">返回环境列表</Button>

                </div>
            </div>
        );
    }
}