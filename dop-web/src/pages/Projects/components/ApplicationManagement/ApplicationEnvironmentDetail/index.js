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
import {Col, Row} from "@alifd/next/lib/grid";

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
        this.editField = new Field(this);
        console.log(props)
        this.state = {
            id: props.id,
            editMode: true,
            envDetailData: [],
            nameSpaceData: [],
            serviceData: [],
            deploymentData: [],
            containerData: [],
            createService: false
        }
    }

    getEnvDetailData() {
        let url = API.gateway + "/application-server/application/environment/detail/" + this.state.id;
        Axios.get(url)
            .then((response) => {
                console.log(response)
                if (response.data == "") {
                    this.setState({
                        envDetailData: [],
                        editMode: true
                    })
                } else {
                    this.setState({
                        envDetailData: response.data,
                        editMode: false
                    })
                    let _this = this
                    let namespaceUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster/allNamespaces"
                    Axios.get(namespaceUrl)
                        .then((response) => {
                            console.log("response", response)
                            if (response.data != "") {
                                _this.setState({
                                    nameSpaceData: response.data
                                })
                            }
                        })
                    if (this.state.nameSpaceData != []) {
                        console.log("envDetailData", _this.state.envDetailData)
                        let servicesUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster/allServices"
                        Axios.get(servicesUrl, {
                            params: {
                                namespace: _this.state.envDetailData.nameSpace
                            }
                        })
                            .then((response) => {
                                console.log("response", response)
                                if (response.data != "") {
                                    _this.setState({
                                        serviceData: response.data
                                    })
                                }
                            })
                        if (this.state.serviceData != []) {
                            let deploymentUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster/allDeployment"
                            Axios.get(deploymentUrl, {
                                params: {
                                    namespace: _this.state.envDetailData.nameSpace,
                                    service: _this.state.envDetailData.service
                                }
                            })
                                .then((response) => {
                                    console.log("response", response)
                                    if (response.data != "") {
                                        _this.setState({
                                            deploymentData: response.data.deployment,
                                            containerData: response.data.containers
                                        })
                                    }
                                    console.log(_this.field.getValue("deployment"), _this.state.containerData)
                                })
                        }
                    }

                }


            })
    }

    envDetailConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存修改吗？",
            title: "确认修改",
            onOk: this.envDetailSubmit.bind(this)
        });
    };
    clusterInfoConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存提交集群信息吗？",
            title: "确认提交",
            onOk: this.clusterInfoSubmit.bind(this)
        });
    };

    toggleEditMode() {
        this.setState({
            editMode: !this.state.editMode
        })
    }

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
        if (this.state.createService) {
            let createServiceUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster/services"
            Axios.post(createServiceUrl, {}, {
                params: {
                    name: this.field.getValue("service"),
                    namespace: this.field.getValue("nameSpace"),
                    port: this.editField.getValue('port'),
                }
            })
                .then(() => {
                    Toast.success("服务创建成功")
                    this.field.validate((errors, values) => {

                        console.log(errors, values);

                        // 没有异常则提交表单
                        if (errors == null) {
                            let url = API.gateway + '/application-server/application/environment/detail/yaml/' + this.state.id;
                            Axios.put(url, {}, {
                                    params: {
                                        deploymentStrategy: this.field.getValue('deploymentStrategy'),
                                        nameSpace: this.field.getValue('nameSpace'),
                                        service: this.field.getValue('service'),
                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                        replicas: this.editField.getValue('replicas'),
                                        image_url: this.field.getValue('imageUrl'),
                                        releaseBatch: this.field.getValue('releaseBatch')
                                    }
                                }
                            )
                                .then(function (response) {
                                    Toast.success("更新成功！")

                                    //提交完成后刷新当前页面
                                    _this.getEnvDetailData()
                                })
                                .catch(function (error) {
                                    console.log(error);
                                });
                        }
                    })
                })
        } else {
            this.postYamlInfo()
        }
    }

    postYamlInfo() {
        let _this = this
        this.field.validate((errors, values) => {

            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                let url = API.gateway + '/application-server/application/environment/detail/yaml/' + this.state.id;
                Axios.put(url, {}, {
                        params: {
                            deploymentStrategy: this.field.getValue('deploymentStrategy'),
                            nameSpace: this.field.getValue('nameSpace'),
                            service: this.field.getValue('service'),
                            deployment: this.field.getValue('deployment'),
                            containers: this.field.getValue('container'),
                            releaseStrategy: this.field.getValue('releaseStrategy'),
                            image_url: this.field.getValue('imageUrl'),
                            releaseBatch: this.field.getValue('releaseBatch')
                        }
                    }
                )
                    .then(function (response) {
                        Toast.success("更新成功！")

                        //提交完成后刷新当前页面
                        _this.getEnvDetailData()
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            }
        })
    }

    componentDidMount() {
        this.getEnvDetailData();

    }

    onNamespaceInputUpdate(e, value) {
        this.field.setValue("nameSpace", value.value)
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

    onInputBlur(e, value) {
        console.log("inputUpdate", value)
        this.field.setValue("service", value)
    }

    onServiceChange(e, value) {
        let _this = this
        console.log("change", value)
        this.field.setValue("service", value.value)
        this.field.setValue("deployment", "")
        this.field.setValue("container", "")
        // console.log(_this.field.getValue("nameSpace"))
        if (!this.state.createService) {
            let namespaceUrl = API.gateway + "/application-server/application/environment/detail/" + this.state.id + "/cluster/allDeployment"
            Axios.get(namespaceUrl, {
                params: {
                    namespace: _this.field.getValue("nameSpace"),
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
    }

    onDeploymentChange(e, value) {
        this.field.setValue("deployment", value.value)
    }

    toggleCreateService() {
        this.setState({
            createService: !this.state.createService
        })
    }

    checkDeploymentData() {
        return this.state.createService || (this.state.deploymentData == undefined) || (this.state.deploymentData == "") || (this.state.deploymentData.length < 2) && (this.state.containerData.length == 0 || this.state.containerData[this.state.deploymentData[0]].length < 2)
    }

    formRender() {
        if (this.state.editMode) {
            const {init, getValue} = this.field
            // const {clusterInit, clusterGetValue} = this.clusterField
            return (

                <div>
                    <Form>

                        <FormItem label="目标集群URL"
                                  {...formItemLayout}
                                  validateStatus={this.clusterField.getError("targetClusterUrl") ? "error" : ""}
                                  help={this.clusterField.getError("targetClusterUrl") ? "请输入目标集群" : ""}>
                            <Input placeholder="目标集群"
                                   defaultValue={this.state.envDetailData == [] ? "" : this.state.envDetailData.targetClusterUrl}
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

                        <FormItem label="部署方式"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("deploymentStrategy") ? "error" : ""}
                                  help={this.field.getError("deploymentStrategy") ? "请选择部署方式" : ""}>
                            <Select placeholder="部署方式"
                                    defaultValue={this.state.envDetailData == [] ? "" : this.state.envDetailData.deploymentStrategy}
                                    {...init('deploymentStrategy', {rules: [{required: true, message: "该项不能为空"}]})}>
                                <Option value="KUBERNETES">Kubernetes部署</Option>
                            </Select>
                        </FormItem>


                        <FormItem label="发布策略"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("releaseStrategy") ? "error" : ""}
                                  help={this.field.getError("releaseStrategy") ? "发布策略" : ""}>
                            <Select placeholder="发布策略"
                                    {...init('releaseStrategy', {rules: [{required: true, message: "该项不能为空"}]})}
                                    defaultValue={this.state.envDetailData == [] ? "" : this.state.envDetailData.releaseStrategy}>
                                <Option value="BATCH" disabled={true}>分批发布</Option>
                                <Option value="BLUE_GREEN" disabled={true}>蓝绿发布</Option>
                                <Option value="ROLLING_UPDATE">滚动升级</Option>
                            </Select>
                        </FormItem>

                        <FormItem style={{display: this.field.getValue('releaseBatch') == 'BATCH' ? "" : "None"}}
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


                        <FormItem label="镜像地址"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("imageUrl") ? "error" : ""}
                                  help={this.field.getError("imageUrl") ? "镜像仓库地址" : ""}>
                            <Input placeholder="镜像仓库地址"
                                   defaultValue={this.state.envDetailData == [] ? "" : this.state.envDetailData.imageUrl}
                                   {...init('imageUrl', {
                                       rules: [{
                                           required: true,
                                           message: "该项不能为空"
                                       }]
                                   })}>
                            </Input>
                        </FormItem>

                        <FormItem label="命名空间"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("nameSpace") ? "error" : ""}
                                  help={this.field.getError("nameSpace") ? "请选择命名空间" : ""}>
                            <Combobox
                                placeholder="命名空间"
                                {...init('nameSpace', {rules: [{required: true, message: "该项不能为空"}]})}
                                defaultValue={this.state.envDetailData == [] ? "" : this.state.envDetailData.nameSpace}
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
                                onChange={this.onServiceChange.bind(this)}
                                onInputBlur={this.onInputBlur.bind(this)}
                                defaultValue={this.state.envDetailData == [] ? "" : this.state.envDetailData.service}

                            >
                                {this.state.serviceData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>

                            <div
                                onClick={this.toggleCreateService.bind(this)}>{this.state.createService ? "创建服务" : "选择服务"}</div>
                        </FormItem>


                        <FormItem style={{display: this.state.createService ? "" : "None"}}
                                  label="端口"
                                  {...formItemLayout}
                                  valiateStatus={this.editField.getError("port") ? "error" : ""}
                                  help={this.editField.getError("port") ? "请输入正确的端口" : ""}
                        >
                            <Input placeholder="开放端口"
                                   defaultValue={0}
                                   {...this.editField.init("port", {rules: [{required: true, message: "该项不能为空"}]})}/>

                        </FormItem>

                        <FormItem style={{display: this.state.createService ? "" : "None"}}
                                  label="副本数量"
                                  {...formItemLayout}
                                  validateStatus={this.editField.getError("replicas") ? "error" : ""}
                                  help={this.editField.getError("replicas") ? "副本数量" : ""}>
                            <NumberPicker min={1}
                                          max={99}
                                          placeholder="副本数量"
                                          defaultValue={0}
                                          {...this.editField.init('replicas', {
                                              rules: [{
                                                  required: true,
                                                  message: "该项不能为空"
                                              }]
                                          })}/>
                        </FormItem>


                        <FormItem
                            label="部署"
                            {...formItemLayout}
                            validateStatus={this.field.getError("deployment") ? "error" : ""}
                            help={this.field.getError("deployment") ? "部署" : ""}
                            style={{display: this.checkDeploymentData() ? "None" : ""}}

                        >
                            <Combobox
                                placeholder="部署"
                                {...init('deployment', {
                                    rules: this.checkDeploymentData() ? "" : [{
                                        required: true,
                                        message: "该项不能为空"
                                    }]
                                })}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onDeploymentChange.bind(this)}
                                defaultValue=""
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
                                  style={{display: this.checkDeploymentData() ? "None" : ""}}

                        >
                            <Combobox

                                placeholder="容器"
                                {...init('container', {
                                    rules: this.checkDeploymentData() ? "" : [{
                                        required: true,
                                        message: "该项不能为空"
                                    }]
                                })}
                                fillProps="value"
                                hasClear={true}
                                defaultValue={""}
                                // onInputUpdate={this.onInputUpdate.bind(this)}
                            >
                                {
                                    (this.field.getValue("deployment") == undefined || this.field.getValue("deployment") == "") ? [] : this.state.containerData[this.field.getValue("deployment")].map((item) => {
                                        return (<Option value={String(item)}>{String(item)}</Option>)
                                    })}
                            </Combobox>
                        </FormItem>


                        <Button onClick={this.envDetailConfirm.bind(this)}
                                type="primary"
                                style={{marginRight: "5px"}}>
                            保存
                        </Button>
                        < Button onClick={this.toggleEditMode.bind(this)}>
                            取消
                        </Button>
                    </Form>
                </div>
            )

        } else {
            return (<Col>
                <Button onClick={this.toggleEditMode.bind(this)}
                        type="primary"
                        style={{marginRight: "5px"}}>
                    编辑
                </Button>
                <Row>
                    <div>部署方式</div>
                    <div>{this.state.envDetailData.deploymentStrategy}</div>
                </Row>
                <Row>
                    <div>集群Url</div>
                    <div>{this.state.envDetailData.targetClusterUrl}</div>
                </Row>
                <Row>
                    <div>镜像地址</div>
                    <div>{this.state.envDetailData.imageUrl}</div>
                </Row>
                <Row>
                    <div>命名空间</div>
                    <div>{this.state.envDetailData.nameSpace}</div>
                </Row>
                <Row>
                    <div>服务</div>
                    <div>{this.state.envDetailData.service}</div>
                </Row>
                <Row>
                    <div>发布策略</div>
                    <div>{this.state.envDetailData.releaseStrategy}</div>
                </Row>
            </Col>)
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