import
{
    Input,
    Form,
    Field,
    Button,
    Feedback,
    Icon,
    Dialog, Loading
} from "@icedesign/base";
import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";
import K8sInfoPage from '../K8sInfoPage'
import {Col, Row} from "@alifd/next/lib/grid";

const FormItem = Form.Item;
const Toast = Feedback.toast;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
export default class ClusterInfoForm extends Component {
    constructor(props) {
        super(props)
        this.field = new Field(this)
        this.state = {
            editMode: false,
            clusterData: "",
            appEnvId: props.appEnvId,
            loading: true

        }
    }


    toggleEditMode() {
        this.setState({
            editMode: !this.state.editMode
        })
    }

    clusterInfoConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存提交集群信息吗？",
            title: "确认提交",
            onOk: this.clusterInfoSubmit.bind(this)
        });
    };

    clusterInfoSubmit = () => {
        let _this = this;
        this.setState({
            loading: true
        })
        //校验输入
        this.field.validate((errors, values) => {
            console.log(errors);

            // 没有异常则提交表单
            if (errors == null) {
                let postUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster"
                Axios.post(postUrl, {
                    targetClusterUrl: _this.field.getValue("targetClusterUrl"),
                    targetClusterToken: _this.field.getValue("targetClusterToken")
                })
                    .then((response) => {
                            Toast.success("保存成功")
                        _this.setState({
                            loading: false
                        })
                            _this.getClusterData()
                        }
                    )
                    .catch(
                        (response) => {
                            _this.setState({
                                loading: false
                            })
                            Toast.error("保存失败")
                        }
                    )
            }

        })
    }

    getClusterData() {
        let _this = this
        let urlUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster"
        _this.setState({
            loading: true
        })
        Axios.get(urlUrl)
            .then((response) => {
                console.log("cluster", response)
                _this.setState({
                    clusterData: response.data.targetClusterUrl,
                    editMode: false,
                    loading: false
                })
            })
    }

    componentDidMount() {
        this.getClusterData()
    }

    k8sInfoRender() {
        if (!(this.state.clusterData === undefined && this.state.clusterData === ""))
            return (<K8sInfoPage
                targetCluster={this.state.clusterData}
                appEnvId={this.state.appEnvId}
            />)

    }

    render() {
        const {init, getError} = this.field
        return (


            <Col>
                <Loading visible={this.state.loading} size='small' shape="dot-circle" color="#2077FF">
                    <Row>
                        <Form style={{width: "100%"}}>
                            <FormItem label="目标集群URL"
                                      {...formItemLayout}
                                      validateStatus={getError("targetClusterUrl") ? "error" : ""}
                                      help={getError("targetClusterUrl") ? "请输入目标集群" : ""}>
                                <Input
                                    style={{display: this.state.editMode ? "" : "None"}}
                                    placeholder="目标集群"
                                    {...init('targetClusterUrl', {
                                        initValue: this.state.clusterData,
                                        rules: [{
                                            required: true,
                                            message: "该项不能为空"
                                        }]
                                    })}>
                                </Input>
                                <div style={{display: this.state.editMode ? "None" : ""}}>
                                    {this.state.clusterData == "" ? "" : this.state.clusterData}
                                    <Icon type='edit' visible={!this.state.editMode ? "true" : "false"}
                                          onClick={this.toggleEditMode.bind(this)}/>
                                </div>
                            </FormItem>


                            <FormItem label="Token"
                                      style={{display: this.state.editMode ? "" : "None"}}
                                      {...formItemLayout}
                                      validateStatus={getError("targetClusterToken") ? "error" : ""}
                                      help={getError("targetClusterToken") ? "请输入Token" : ""}>
                                <Input multiple placeholder="AccessToken"
                                       {...init('targetClusterToken', {
                                           rules: [{
                                               required: true,
                                               message: "该项不能为空"
                                           }]
                                       })}>
                                </Input>
                            </FormItem>

                            <Button onClick={this.clusterInfoConfirm.bind(this)}
                                    type="primary"
                                    style={{marginRight: "5px", display: this.state.editMode ? "" : "None"}}>
                                提交
                            </Button>

                            < Button
                                style={{display: this.state.editMode ? "" : "None"}}
                                onClick={this.toggleEditMode.bind(this)}> 取消 </Button>
                        </Form>
                    </Row>
                </Loading>
                <Row>
                    {
                        this.k8sInfoRender()
                    }
                </Row>
            </Col>


        )
    }

}