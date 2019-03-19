import
{
    Table,
    Input,
    Form,
    Field,
    Button,
    Select,
    Feedback,
    NumberPicker, Dialog
} from "@icedesign/base";
import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";

const FormItem = Form.Item;
const Option = Select.Option;
const Toast = Feedback.toast;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
const {Combobox} = Select;

/**
 * 展示应用环境详情的列表（仅编辑界面）
 * @author Bowen
 **/
export default class ApplicationEnvironmentDetail extends Component {

    constructor(props) {
        super(props);
        this.clusterField = new Field(this);
        this.field = new Field(this);
        console.log(props)
        this.state = {
            id: props.id,
            editMode: true,
            envDetailData: [],
            nameSpaceData: [],
            serviceData: [],
            deploymentData: [],
            containerData: []
        }
    }

    getEnvDetailData() {
        let url = API.gateway + "/application-server/application/environment/detail/" + this.state.id;
        Axios.get(url)
            .then((response) => {
                this.setState({
                    envDetailData: response.data
                })
            })
    }

    envDetailConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存修改吗？",
            title: "确认修改",
            onOk: this.testKubernetes.bind(this)
        });
    };
    clusterInfoConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存提交集群信息吗？",
            title: "确认提交",
            onOk: this.clusterInfoSubmit.bind(this)
        });
    };

    clusterInfoSubmit = () => {
        let _this = this;

        //校验输入
        this.clusterField.validate((errors, values) => {
            console.log(errors);

            // 没有异常则提交表单
            if (errors == null) {
                let postUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster"
                Axios.post(postUrl, {
                    targetClusterUrl: _this.clusterField.getValue("targetClusterUrl"),
                    targetClusterToken: _this.clusterField.getValue("targetClusterToken")
                })
                    .then((response) => {
                            Toast.success("保存成功")

                            let namespaceUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster/allNamespaces"
                            Axios.get(namespaceUrl)
                                .then((response) => {
                                    console.log("response", response)
                                    _this.setState({
                                        nameSpaceData: response.data
                                    })
                                })

                        }
                    )
                    .catch(
                        (response) => {
                            Toast.error("保存失败")
                        }
                    )
            }

        })
    }

    testKubernetes = () => {
        let url = API.gateway + "/application-server/application/environment/detail/cluster"
        Axios.get(url).then((response) => {
            console.log(response)
        })
            .catch((response) => {
                console.log(response)
            })

    }


    envDetailSubmit() {
        let _this = this;

        //校验输入
        this.field.validate((errors, values) => {

            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let url = API.gateway + '/application-server/application/environment/detail/' + this.state.id;
                Axios.put(url, {}, {
                        params: {
                            deploymentStrategy: this.field.getValue('deploymentStrategy'),
                            targetCluster: this.field.getValue('targetCluster'),
                            nameSpace: this.field.getValue('nameSpace'),
                            service: this.field.getValue('service'),
                            releaseBatch: this.field.getValue('releaseBatch')
                        }
                    }
                )
                    .then(function (response) {
                        Toast.success("更新成功！")

                        //提交完成后刷新当前页面
                        let url = API.gateway + '/application-server/application/environment/detail/' + this.state.id;
                        Axios.get(url)
                            .then(function (response) {
                                console.log(response)
                                _this.setState({
                                    appBasicData: response.data,
                                    basicEditMode: false
                                })
                            })
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            }
        });

    }


    componentDidMount() {
        this.getEnvDetailData();
    }

    onNamespaceInputUpdate(e, value) {
        this.field.setValue("nameSpace", value)
        let _this = this
        let namespaceUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster/allServices"
        Axios.get(namespaceUrl, {
            params: {
                namespace: value.value
            }
        })
            .then((response) => {
                console.log("response", response)
                _this.setState({
                    serviceData: response.data
                })
            })
    }

    onServiceInputUpdate(e, value) {
        let _this = this
        // console.log(_this.field.getValue("nameSpace"))
        let namespaceUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster/allDeployment"
        Axios.get(namespaceUrl, {
            params: {
                namespace: _this.field.getValue("nameSpace").value,
                service: value.value
            }
        })
            .then((response) => {
                console.log("response", response)
                _this.setState({
                    deploymentData: response.data.deployment,
                    containerData: response.data.containers
                })
                console.log(_this.field.getValue("deployment"), _this.state.containerData)
            })
    }

    onDeploymentChange(e, value) {
        this.field.setValue("deployment", value)
    }

    formRender() {
        if (this.state.editMode) {
            const {init, getValue} = this.field
            // const {clusterInit, clusterGetValue} = this.clusterField
            return (

                <div>
                    <Form>
                        <FormItem label="部署方式"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("deploymentStrategy") ? "error" : ""}
                                  help={this.field.getError("deploymentStrategy") ? "请选择部署方式" : ""}>
                            <Select placeholder="部署方式"
                                    {...init('deploymentStrategy', {rules: [{required: true, message: "该项不能为空"}]})}>
                                <Option value="KUBERNETES">Kubernetes部署</Option>
                            </Select>
                        </FormItem>

                        <FormItem label="目标集群URL"
                                  {...formItemLayout}
                                  validateStatus={this.clusterField.getError("targetClusterUrl") ? "error" : ""}
                                  help={this.clusterField.getError("targetClusterUrl") ? "请输入目标集群" : ""}>
                            <Input placeholder="目标集群"
                                   {...this.clusterField.init('targetClusterUrl', {
                                       rules: [{
                                           required: true,
                                           message: "该项不能为空"
                                       }]
                                   })}>
                            </Input>
                        </FormItem>
                        <FormItem label="Token"
                                  {...formItemLayout}
                                  validateStatus={this.clusterField.getError("targetClusterToken") ? "error" : ""}
                                  help={this.clusterField.getError("targetClusterToken") ? "请输入Token" : ""}>
                            <Input multiple placeholder="AccessToken"
                                   {...this.clusterField.init('targetClusterToken', {
                                       rules: [{
                                           required: true,
                                           message: "该项不能为空"
                                       }]
                                   })}>
                            </Input>
                        </FormItem>


                        <Button onClick={this.clusterInfoSubmit.bind(this)}
                                type="primary"
                                style={{marginRight: "5px"}}>
                            提交
                        </Button>

                        < Button> 取消 </Button>

                        <FormItem label="命名空间"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("nameSpace") ? "error" : ""}
                                  help={this.field.getError("nameSpace") ? "请选择命名空间" : ""}>
                            <Combobox
                                placeholder="命名空间"
                                {...init('nameSpace', {rules: [{required: true, message: "该项不能为空"}]})}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onNamespaceInputUpdate.bind(this)}
                            >
                                {this.state.nameSpaceData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>
                        </FormItem>


                        <FormItem label="服务"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("service") ? "error" : ""}
                                  help={this.field.getError("service") ? "服务" : ""}>
                            <Combobox
                                placeholder="服务"
                                {...init('service', {rules: [{required: true, message: "该项不能为空"}]})}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onServiceInputUpdate.bind(this)}

                            >
                                {this.state.serviceData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>
                        </FormItem>

                        <FormItem label="部署"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("deployment") ? "error" : ""}
                                  help={this.field.getError("deployment") ? "部署" : ""}
                                  style={{display: (this.state.deploymentData.length < 2) && (this.state.containerData.length == 0 || this.state.containerData[this.state.deploymentData[0]].length < 2) ? "None" : ""}}>
                            <Combobox
                                placeholder="部署"
                                {...init('deployment', {rules: [{required: true, message: "该项不能为空"}]})}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onDeploymentChange.bind(this)}
                            >
                                {this.state.deploymentData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>
                        </FormItem>

                        <FormItem label="容器"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("container") ? "error" : ""}
                                  help={this.field.getError("container") ? "容器" : ""}
                                  style={{display: (this.state.deploymentData.length < 2) && (this.state.containerData.length == 0 || this.state.containerData[this.state.deploymentData[0]].length < 2) ? "None" : ""}}>
                            <Combobox
                                placeholder="容器"
                                {...init('container', {rules: [{required: true, message: "该项不能为空"}]})}
                                fillProps="value"
                                hasClear={true}
                                // onInputUpdate={this.onInputUpdate.bind(this)}
                            >
                                {
                                    this.field.getValue("deployment") == undefined ? [] : this.state.containerData[this.field.getValue("deployment").value].map((item) => {
                                        return (<Option value={String(item)}>{String(item)}</Option>)
                                    })}
                            </Combobox>
                        </FormItem>

                        <FormItem label="发布策略"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("releaseStrategy") ? "error" : ""}
                                  help={this.field.getError("releaseStrategy") ? "发布策略" : ""}>
                            <Select placeholder="发布策略"
                                    {...init('releaseStrategy', {rules: [{required: true, message: "该项不能为空"}]})}>
                                <Option value="BATCH" disabled={true}>分批发布</Option>
                                <Option value="BLUE_GREEN" disabled={true}>蓝绿发布</Option>
                                <Option value="ROLLING_UPDATE">滚动升级</Option>
                            </Select>
                        </FormItem>

                        <FormItem style={{display: this.field.getValue('releaseStrategy') == 'BATCH' ? "" : "None"}}
                                  label="发布批次"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("releaseBatch") ? "error" : ""}
                                  help={this.field.getError("releaseBatch") ? "发布策略" : ""}>
                            <NumberPicker min={1}
                                          max={99}
                                          placeholder="发布批次"
                                          defaultValue={0}
                                          {...init('releaseBatch')}/>
                        </FormItem>

                        <Button onClick={this.envDetailConfirm.bind(this)}
                                type="primary"
                                style={{marginRight: "5px"}}>
                            保存
                        </Button>

                        < Button> 取消 </Button>

                    </Form>
                </div>
            )

        }
    }

    render() {
        const {init, getValue} = this.field;
        return (
            <div>
                {this.formRender()}
            </div>
        );

    }


}