import
{
    Form,
    Button,
    Select,
    Feedback,
    Field, Loading
} from "@icedesign/base";
import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";
import {Col, Row} from "@alifd/next/lib/grid";
import PipelineBindPage from "../PipelineBindPage"
import ClusterInfoForm from "../ClusterInfoForm"

const FormItem = Form.Item;
const Option = Select.Option;
const Toast = Feedback.toast;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
const {Combobox} = Select;

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
            loading: true
        }
    }

    getenvData() {
        let url = API.gateway + "/application-server/app/env/" + this.state.appEnvId;
        Axios.get(url)
            .then((response) => {
                console.log(response)
                if (response.data == "") {
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
        const {init, getValue} = this.field
        return (

            <div style={{
                margin: "0 auto",
                width: "70%",
                display: "flex",
                flexWrap: "wrap",
                justifyContent: "center",
                flexDirection: "column"
            }}>


                <Form>
                    <Loading visible={this.state.loading} size='small' shape="dot-circle" color="#2077FF"
                    >
                    <FormItem label="部署方式"
                              {...formItemLayout}
                              validateStatus={this.field.getError("deploymentStrategy") ? "error" : ""}
                              help={this.field.getError("deploymentStrategy") ? "请选择部署方式" : ""}>

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


                    </Loading>

                </Form>

                <PipelineBindPage appId={this.state.appId} appEnvId={this.state.appEnvId}/>
                {this.clusterInfoRender()}

            </div>
        );
    }
}