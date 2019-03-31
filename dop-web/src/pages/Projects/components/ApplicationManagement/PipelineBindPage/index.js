import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API.js"
import {Link} from 'react-router-dom';
import {Field, Form, Loading} from "@icedesign/base";

const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
const FormItem = Form.Item
export default class PipelineBindPage extends Component {
    constructor(props) {
        super(props);
        this.field = new Field(this)
        this.state = {
            appEnvId: props.appEnvId,
            // editMode: false,
            // pipelineData: [],
            currentPipeline: "",
            appId: props.appId,
            loading: true
        }
    }

    // toggleEditMode() {
    //     this.setState
    //     ({
    //         editMode: !this.state.editMode
    //     })
    // }
    //
    // onEdit() {
    //     let _this = this
    //     this.setState({
    //         loading: true
    //     })
    //     let pipelineListUrl = API.gateway + "/pipeline-server/v1/pipeline/cuser"
    //     Axios.get(pipelineListUrl, {
    //         params: {
    //             cuser: 18
    //             // window.sessionStorage.getItem('user-id')
    //         }
    //     })
    //         .then((response) => {
    //             console.log("pipelineData", response)
    //             _this.setState({
    //                 loading: false,
    //                 pipelineData: response.data
    //             })
    //         })
    //         .catch((response) => {
    //             console.log(response)
    //             Toast.error("获取流水线信息失败")
    //             _this.setState({
    //                 loading: false
    //             })
    //         })
    //
    //     this.toggleEditMode()
    // }

    getPipelineData() {
        let _this = this
        let getUrl = API.gateway + "/pipeline-server/v1/pipeline/envId/" + this.state.appEnvId
        this.setState({
            loading: true
        })
        Axios.get(getUrl)
            .then((response) => {
                console.log("pipelineId", response)
                if (response.data == "") {
                    _this.setState({
                        editMode: true,
                        currentPipeline: "",
                        loading: false
                    })
                } else {

                    Axios.get(API.gateway + "/pipeline-server/v1/pipeline/" + response.data)
                        .then((response) => {
                            console.log("currentPipeline", response)

                            _this.setState({
                                currentPipeline: response.data,
                                editMode: false,
                                loading: false
                            })
                        })

                }
            })

        // let pipelineListUrl = API.gateway + "/pipeline-server/v1/pipeline/cuser"
        // Axios.get(pipelineListUrl, {
        //     params: {
        //         cuser: 18
        //         // window.sessionStorage.getItem('user-id')
        //     }
        // })
        //     .then((response) => {
        //         console.log("pipelineData", response)
        //         _this.setState({
        //             pipelineData: response.data,
        //             loading: false
        //         })
        //     })



    }

    componentDidMount() {
        this.getPipelineData()
    }

    // submitPipelineInfo() {
    //     let _this = this
    //     this.setState({
    //         loading: true
    //     })
    //     this.field.validate((errors, values) => {
    //         console.log(errors);
    //         if (errors == null) {
    //
    //             // let updateUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/pipeline"
    //             // Axios.put(updateUrl, {}, {
    //             //     params: {
    //             //         pipelineId: _this.field.getValue('pipeline')
    //             //     }
    //             // })
    //             //     .then((response) => {
    //
    //             let bindUrl = API.gateway + "/pipeline-server/v1/pipeline/appId"
    //             Axios.post(bindUrl, {}, {
    //                 params: {
    //                     envid: this.state.appEnvId,
    //                     pipelineId: _this.field.getValue('pipeline'),
    //                     appid: this.state.appId
    //                 }
    //             })
    //                 .then((response) => {
    //                     console.log("response", response)
    //                     Toast.success("更新流水线成功")
    //
    //                     _this.getPipelineData()
    //
    //                 })
    //                 .catch((response, error) => {
    //                     console.log("error", error)
    //                     Toast.error("更新失败")
    //                 })
    //
    //
    //             // })
    //             // .catch((response) => {
    //             //     console.log(response)
    //             //     Toast.error("更新失败")
    //             // })
    //
    //
    //         }
    //     })
    //
    // }

    pipelineRender() {
        // const {init, getValue} = this.field
        // if (this.state.editMode) {
        //     return (
        //
        //         <Form>
        //             <FormItem label="流水线："
        //                       {...formItemLayout}
        //                       validateStatus={this.field.getError("pipeline") ? "error" : ""}
        //                       help={this.field.getError("pipeline") ? "请选择流水线" : ""}>
        //                 <Combobox
        //                     fillProps="label"
        //                     {...init("pipeline", {
        //                     rules: [{
        //                         required: true,
        //                         message: "该项不能为空"
        //                     }]
        //                 })}>
        //                     {this.state.pipelineData.map((item) => {
        //                         return (<Option value={item.id}>{item.name}</Option>)
        //                     })}
        //                 </Combobox>
        //             </FormItem>
        //             <Button onClick={this.submitPipelineInfo.bind(this)}
        //                     type="primary"
        //                     style={{marginRight: "5px"}}>>
        //                 确认
        //             </Button>
        //             <Button onClick={this.onEdit.bind(this)}>
        //                 取消
        //             </Button>
        //         </Form>
        //
        //     )
        // } else {
            return (

                <Form style={{width: "100%", textAlign: "center", alignItems: "center"}}>
                    <Loading style={{width: "30%"}} visible={this.state.loading} size='small' shape="dot-circle"
                             color="#2077FF"
                    >
                        <FormItem   {...formItemLayout}
                                    label="流水线">
                            <Link
                                to={this.state.currentPipeline == "" ? "pipeline" : "pipeline/project/" + this.state.currentPipeline.id}>
                                <div style={{
                                    textAlign: "center",
                                    padding: "5px"
                                }}>{this.state.currentPipeline == "" ? "点击前往绑定流水线" : this.state.currentPipeline.name}</div>
                            </Link>
                        </FormItem>


                    </Loading>

                </Form>

            )
        // }
    }

    render() {
        return (

            this.pipelineRender()

        )
    }
}