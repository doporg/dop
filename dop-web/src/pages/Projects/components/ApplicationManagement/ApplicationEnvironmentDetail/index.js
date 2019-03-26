import
{
    Form,
    Button,
    Select,
    Feedback,
    Field
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
            envData: []
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
                        editMode: true
                    })
                } else {
                    this.setState({
                        envData: response.data,
                        editMode: false
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
            <div>
                <Form>

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

                    <PipelineBindPage appId={this.state.appId} appEnvId={this.state.appEnvId}/>


                </Form>
                {this.clusterInfoRender()}

            </div>
        );
    }
}