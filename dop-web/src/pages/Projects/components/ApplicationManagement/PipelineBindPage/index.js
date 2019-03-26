import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API.js"
import
{
    Table,
    Input,
    Form,
    Icon,
    Field,
    Button,
    Select,
    Feedback,
    NumberPicker, Dialog
} from "@icedesign/base";

import {Col, Row} from "@alifd/next/lib/grid";

const FormItem = Form.Item;
const Option = Select.Option;
const Toast = Feedback.toast;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
const {Combobox} = Select;
export default class PipelineBindPage extends Component {
    constructor(props) {
        super(props);
        this.field = new Field(this)
        this.state = {
            appEnvId: props.appEnvId,
            editMode: false,
            pipelineData: [],
            currentPipeline: "",
            appId: props.appId
        }
    }

    toggleEditMode() {
        this.setState
        ({
            editMode: !this.state.editMode
        })
    }

    onEdit() {
        let _this = this
        let pipelineListUrl = API.gateway + "/pipeline-server/v1/pipeline/cuser"
        Axios.get(pipelineListUrl, {
            params: {
                cuser: 1
                // window.sessionStorage.getItem('user-id')
            }
        })
            .then((response) => {
                console.log("pipelineData", response)
                _this.setState({
                    pipelineData: response.data
                })
            })
        this.toggleEditMode()
    }

    getPipelineData() {
        let _this = this
        let getUrl = API.gateway + "/pipeline-server/v1/pipeline/appId/" + this.state.appEnvId
        Axios.get(getUrl)
            .then((response) => {
                console.log("pipelineId", response)
                if (response.data.pipelineId == null) {
                    _this.setState({
                        editMode: true
                    })
                } else {

                    Axios.get(API.gateway + "/pipeline-server/v1/pipeline/byId", {
                        params: {
                            id: response.data.pipelineId
                        }
                    })
                        .then((response) => {
                            console.log("currentPipeline", response)
                            _this.setState({
                                currentPipeline: response.data,
                                editMode: false
                            })
                        })

                }
            })

        let pipelineListUrl = API.gateway + "/pipeline-server/v1/pipeline/cuser"
        Axios.get(pipelineListUrl, {
            params: {
                cuser: 1
                // window.sessionStorage.getItem('user-id')
            }
        })
            .then((response) => {
                console.log("pipelineData", response)
                _this.setState({
                    pipelineData: response.data
                })
            })


    }

    componentDidMount() {
        this.getPipelineData()
    }

    submitPipelineInfo() {
        let _this = this
        this.field.validate((errors, values) => {
            console.log(errors);
            if (errors == null) {

                // let updateUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/pipeline"
                // Axios.put(updateUrl, {}, {
                //     params: {
                //         pipelineId: _this.field.getValue('pipeline')
                //     }
                // })
                //     .then((response) => {

                let bindUrl = API.gateway + "/pipeline-server/v1/pipeline/appId"
                Axios.post(bindUrl, {}, {
                    params: {
                        envid: this.state.appEnvId,
                        pipelineId: _this.field.getValue('pipeline'),
                        appid: this.state.appId
                    }
                })
                    .then((response) => {
                        console.log("response", response)
                        Toast.success("更新流水线成功")
                        _this.getPipelineData()

                    })
                    .catch((response, error) => {
                        console.log("error", error)
                        Toast.error("更新失败")
                    })


                // })
                // .catch((response) => {
                //     console.log(response)
                //     Toast.error("更新失败")
                // })


            }
        })

    }

    pipelineRender() {
        const {init, getValue} = this.field
        if (this.state.editMode) {
            return (
                <Form>
                    <FormItem label="流水线："
                              {...formItemLayout}
                              validateStatus={this.field.getError("pipeline") ? "error" : ""}
                              help={this.field.getError("pipeline") ? "请选择流水线" : ""}>
                        <Combobox {...init("pipeline", {
                            rules: [{
                                required: true,
                                message: "该项不能为空"
                            }]
                        })}>
                            {this.state.pipelineData.map((item) => {
                                return (<Option value={item.id}>{item.name}</Option>)
                            })}
                        </Combobox>
                    </FormItem>
                    <Button onClick={this.submitPipelineInfo.bind(this)}
                            type="primary"
                            style={{marginRight: "5px"}}>>
                        确认
                    </Button>
                    <Button onClick={this.onEdit.bind(this)}>
                        取消
                    </Button>
                </Form>

            )
        } else {
            return (
                <Row>
                    <Col>
                        <div>流水线
                        </div>
                    </Col>
                    <Col>
                        <div>
                            {this.state.currentPipeline.name}
                        </div>
                    </Col>
                    <Col>
                        <Icon type='edit' onClick={this.toggleEditMode.bind(this)}>
                        </Icon>
                    </Col>
                </Row>
            )
        }
    }

    render() {
        return (
            this.pipelineRender()
        )
    }
}