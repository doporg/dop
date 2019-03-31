import {
    Button,
    Dialog,
    Feedback,
    Field,
    Form,
    Icon,
    Input,
    Loading,
    NumberPicker,
    Select,
    Switch
} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../../API";
import Axios from "axios"

const FormItem = Form.Item;
const Option = Select.Option;
const Toast = Feedback.toast;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
const {Combobox} = Select;
export default class K8sInfoPage extends Component {

    constructor(props) {
        super(props);
        this.field = new Field(this)
        this.editField = new Field(this)
        this.yamlPathField = new Field(this)
        this.yamlEditorfield = new Field(this)
        this.state = {
            appEnvId: props.appEnvId,
            yamlData: [],
            nameSpaceData: [],
            editMode: false,
            serviceData: [],
            deploymentData: [],
            containerData: [],
            createService: false,
            yamlMode: "profile",
            editDeploymentYaml: false
        }

    }

    getNameSpaceData() {
        let _this = this
        let namespaceUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster/allNamespaces"
        Axios.get(namespaceUrl)
            .then((response) => {
                console.log("nameSpace", response)
                _this.setState({
                    nameSpaceData: response.data
                })
            })
    }

    getServiceData(namespace) {

        if (this.field.getValue("nameSpace") === "") {
            this.setState({
                serviceData: ""
            })
        } else {
            let _this = this;
            let namespaceUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster/allServices"
            Axios.get(namespaceUrl, {
                params: {
                    namespace: namespace,
                }
            })
                .then((response) => {
                    console.log("services", response);
                    _this.setState({
                        serviceData: response.data
                    })
                })
        }
    }


    getDeploymentData() {
        if (this.field.getValue("nameSpace") === "" || this.field.getValue("service") === "") {
            this.setState({
                deploymentData: "",
                containerData: ""
            })
        } else {
            let _this = this;
            let namespaceUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster/allDeployment"
            Axios.get(namespaceUrl, {
                params: {
                    namespace: _this.field.getValue("nameSpace"),
                    service: _this.field.getValue("service")
                }
            })
                .then((response) => {
                    console.log("response", response);
                    _this.setState({
                        deploymentData: response.data.deployment,
                        containerData: response.data.containers
                    });
                    console.log(_this.field.getValue("deployment"), _this.state.containerData)
                })
        }
    }

    getYamlData() {
        let _this = this;
        this.setState({
            loading: true
        })
        let url = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yaml"
        Axios.get(url)
            .then((response) => {
                console.log("yaml", response);
                if (response.data === "") {
                    _this.setState({
                        editMode: true,
                        yamlData: [],
                        loading: false
                    })
                } else {

                    if (response.data.yamlFilePath !== "") {
                        _this.setState({
                            editMode: false,
                            yamlData: response.data,
                            yamlMode: 'path',
                            loading: false
                        })


                    } else {
                        _this.setState({
                            editMode: false,
                            yamlData: response.data,
                            yamlMode: 'profile',

                        })
                        _this.getNameSpaceData()
                        _this.getServiceData(this.state.yamlData.nameSpace)
                        _this.yamlEditorfield.setValue("deploymentYaml", response.data.deploymentEditableYaml)
                        _this.setState({loading: false})
                    }

                    _this.field.setValue("yamlMode", this.state.yamlMode)
                    //
                    // if(response.data.deploymentEditableYaml !=="")
                    // {
                    //     _this.setState({
                    //         editDeploymentYaml:true
                    //     })
                    // } else {
                    //     _this.setState({
                    //         yamlMode: false
                    //     })
                    // }

                }
            })
            .catch((response) => {
                console.log("catch", response)
            })
    }

    componentDidMount() {


        this.getYamlData()



    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.refreshK8sInfo) {
            console.log("will", nextProps)
            this.getYamlData()
            // this.getNameSpaceData()
            // this.getServiceData(this.state.yamlData.nameSpace)
            nextProps.refreshFinished()
        }
    }

    envDetailConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存修改吗？",
            title: "确认修改",
            onOk: this.envDetailSubmit.bind(this)
        });
    };

    onNamespaceInputUpdate(e, value) {
        this.field.setValue("nameSpace", value.value)
        this.getServiceData(value.value)

    }

    onInputBlur(e, value) {
        console.log("inputUpdate", value)
        this.field.setValue("service", value)
    }

    onServiceChange(e, value) {
        console.log("onserviceChanged")
        this.field.setValue("service", value.value)
        this.field.setValue("deployment", "")
        this.field.setValue("container", "")
        if (!this.state.createService) {
            this.getDeploymentData()
        }
    }

    onDeploymentChange(e, value) {
        this.field.setValue("deployment", value.value)
    }

    toggleEditMode() {
        this.setState({
            editMode: !this.state.editMode
        })
    }

    toggleCreateService() {
        console.log("toggleCreateService")
        this.setState({
            createService: !this.state.createService
        })
    }

    switchYamlMode(e, values) {
        console.log(values)
        let value = values.value
        // if (value == 'yaml') {
        //     this.setState({
        //         yamlMode: "yaml"
        //     })
        // }

        this.setState({
            yamlMode: value
            })


        this.field.setValue("yamlMode", value)
    }

    checkDeploymentData() {
        return this.state.createService ||
            (this.state.deploymentData == null) ||
            (this.state.deploymentData == "") ||
            (this.state.deploymentData == []) ||
            ((this.state.deploymentData.length < 2) &&
                (this.state.containerData.length == 0 || this.state.containerData[this.state.deploymentData[0]].length < 2))
    }


    envDetailSubmit() {
        this.setState({
            loading: true
        })
        let _this = this;
        if (this.state.yamlMode == 'path') {
            this.yamlPathField.validate((errors, values) => {
                console.log(errors)
                if (errors == null) {
                    let url = API.gateway + '/application-server/app/env/' + this.state.appEnvId + '/yaml';
                    Axios.put(url, {}, {
                            params: {
                                deploymentStrategy: "KUBERNETES",
                                releaseStrategy: this.field.getValue('releaseStrategy'),
                                imageUrl: this.field.getValue('imageUrl'),
                                releaseBatch: this.field.getValue('releaseBatch'),
                                yamlFilePath: this.yamlPathField.getValue('yamlFilePath')
                            }
                        }
                    ).then((response) => {
                        Toast.success("更新成功！")
                        _this.setState({
                            loading: false
                        })
                        //提交完成后刷新当前页面
                        _this.getYamlData()
                    })
                }
            })

        } else {
            if (this.state.createService) {
                let createServiceUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster/service"
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
                                let url = API.gateway + '/application-server/app/env/' + this.state.appEnvId + "/yaml";

                                let existUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yamlStatus"
                                Axios.get(existUrl)
                                    .then((response) => {
                                        console.log("responsssss", response)
                                        if (response.data) {
                                            Axios.put(url, {}, {
                                                    params: {
                                                        deploymentStrategy: "KUBERNETES",
                                                        nameSpace: this.field.getValue('nameSpace'),
                                                        service: this.field.getValue('service'),
                                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                                        replicas: this.editField.getValue('replicas'),
                                                        imageUrl: this.field.getValue('imageUrl'),
                                                        releaseBatch: this.field.getValue('releaseBatch')
                                                    }
                                                }
                                            )
                                                .then(function (response) {
                                                    Toast.success("更新成功！")
                                                    _this.setState({
                                                        loading: false
                                                    })
                                                    //提交完成后刷新当前页面
                                                    _this.getYamlData()
                                                })
                                                .catch(function (error) {
                                                    console.log(error);
                                                });

                                        } else {
                                            Axios.post(url, {}, {
                                                    params: {
                                                        deploymentStrategy: "KUBERNETES",
                                                        nameSpace: this.field.getValue('nameSpace'),
                                                        service: this.field.getValue('service'),
                                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                                        replicas: this.editField.getValue('replicas'),
                                                        imageUrl: this.field.getValue('imageUrl'),
                                                        releaseBatch: this.field.getValue('releaseBatch')
                                                    }
                                                }
                                            )
                                                .then(function (response) {
                                                    Toast.success("更新成功！")
                                                    _this.setState({
                                                        loading: false
                                                    })
                                                    //提交完成后刷新当前页面
                                                    _this.getYamlData()
                                                })
                                                .catch(function (error) {
                                                    console.log(error);
                                                });
                                        }
                                    })

                            }
                        })
                    })
            } else {
                this.postYamlInfo()
            }
        }
    }

    postYamlInfo() {
        let _this = this
        _this.setState({
            loading: true
        })
        this.field.validate((errors, values) => {

            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                let url = API.gateway + '/application-server/app/env/' + this.state.appEnvId + '/yaml'
                let existUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yamlStatus"
                Axios.get(existUrl)
                    .then((response) => {
                        console.log(response)
                        if (response.data) {
                            Axios.put(url, {}, {
                                    params: {
                                        deploymentStrategy: "KUBERNETES",
                                        nameSpace: this.field.getValue('nameSpace'),
                                        service: this.field.getValue('service'),
                                        deployment: this.field.getValue('deployment'),
                                        containers: this.field.getValue('container'),
                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                        imageUrl: this.field.getValue('imageUrl'),
                                        releaseBatch: this.field.getValue('releaseBatch')
                                    }
                                }
                            )
                                .then(function (response) {
                                    Toast.success("更新成功！")
                                    _this.setState({
                                        loading: false
                                    })
                                    //提交完成后刷新当前页面
                                    _this.getYamlData()
                                })
                                .catch(function (error) {
                                    console.log(error);
                                });
                        } else {
                            Axios.post(url, {}, {
                                    params: {
                                        deploymentStrategy: "KUBERNETES",
                                        nameSpace: this.field.getValue('nameSpace'),
                                        service: this.field.getValue('service'),
                                        deployment: this.field.getValue('deployment'),
                                        containers: this.field.getValue('container'),
                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                        imageUrl: this.field.getValue('imageUrl'),
                                        releaseBatch: this.field.getValue('releaseBatch')
                                    }
                                }
                            )
                                .then(function (response) {
                                    Toast.success("更新成功！")
                                    _this.setState({
                                        loading: false
                                    })
                                    //提交完成后刷新当前页面
                                    _this.getYamlData()
                                })
                                .catch(function (error) {
                                    console.log(error);
                                });
                        }
                    })
            }
        })
    }


    yamlInfoRender() {
        const {init, getValue} = this.field
        if (this.state.yamlData !== []) {
            if (this.state.yamlMode == "profile") {
                return (
                    <div>
                        <FormItem label="命名空间:"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("nameSpace") ? "error" : ""}
                                  help={this.field.getError("nameSpace") ? "请选择命名空间" : ""}>
                            <div
                                style={{
                                    display: this.state.editMode ? "None" : "",
                                    textAlign: "left",
                                    marginLeft: "10%",
                                    marginTop: "5px"
                                }}>{this.state.yamlData.nameSpace}</div>
                            <Combobox
                                style={{display: this.state.editMode ? "" : "None"}}
                                placeholder="命名空间"
                                {...init('nameSpace', {
                                    initValue: this.state.yamlData.nameSpace,
                                    rules: [{required: true, message: "该项不能为空"}]
                                })}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onNamespaceInputUpdate.bind(this)}
                            >
                                {this.state.nameSpaceData.length === 0 ? "" : this.state.nameSpaceData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>
                        </FormItem>

                        <FormItem style={{display: this.state.editMode ? "" : "None"}}
                                  label="是否新建服务:"
                                  {...formItemLayout}>
                            <Switch
                                checkedChildren="是"
                                unCheckedChildren="否"
                                onChange={this.toggleCreateService.bind(this)}
                                defaultChecked={this.state.createService}>
                            </Switch>
                        </FormItem>

                        <FormItem label="服务:"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("service") ? "error" : ""}
                                  help={this.field.getError("service") ? "服务" : ""}>
                            <div
                                style={{
                                    display: this.state.editMode ? "None" : "",
                                    textAlign: "left",
                                    marginLeft: "10%",
                                    marginTop: "5px"
                                }}>{this.state.yamlData.service}</div>
                            <Combobox
                                style={{display: this.state.editMode ? "" : "None"}}
                                placeholder="服务"
                                {...init('service', {
                                    initValue: this.state.yamlData.service,
                                    rules: [{required: true, message: "该项不能为空"}]
                                })}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onServiceChange.bind(this)}
                                onInputBlur={this.onInputBlur.bind(this)}

                            >
                                {this.state.serviceData.length === 0 ? "" : this.state.serviceData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>

                        </FormItem>
                        <FormItem style={{display: this.state.editMode && this.state.createService ? "" : "None"}}
                                  label="端口:"
                                  {...formItemLayout}
                                  validateStatus={this.editField.getError("port") ? "error" : ""}
                                  help={this.editField.getError("port") ? "请输入正确的端口" : ""}
                        >
                            <Input placeholder="开放端口"
                                   defaultValue={0}
                                   style={{width: "40%"}}
                                   {...this.editField.init("port", {rules: [{required: true, message: "该项不能为空"}]})}/>

                        </FormItem>

                        <FormItem style={{display: this.state.editMode && this.state.createService ? "" : "None"}}
                                  label="副本数量:"
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
                            <div
                                style={{
                                    display: this.state.editMode ? "None" : "",
                                    textAlign: "left",
                                    marginLeft: "10%",
                                    marginTop: "5px"
                                }}>{this.state.yamlData.deployment}</div>
                            <Combobox
                                style={{display: this.state.editMode ? "" : "None"}}
                                placeholder="部署"
                                {...init('deployment', {
                                    initValue: this.state.yamlData.deployment,
                                    rules: this.checkDeploymentData() ? "" : [{
                                        required: true,
                                        message: "该项不能为空"
                                    }]
                                })}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onDeploymentChange.bind(this)}

                            >
                                {this.state.deploymentData.length === 0 ? "" : this.state.deploymentData.map((item) => {
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
                            <div
                                style={{
                                    display: this.state.editMode ? "None" : "",
                                    textAlign: "left",
                                    marginLeft: "10%",
                                    marginTop: "5px"
                                }}>{this.state.yamlData.container}</div>
                            <Combobox
                                style={{display: this.state.editMode ? "" : "None"}}
                                placeholder="容器"
                                {...init('container', {
                                    initValue: this.state.yamlData.container,
                                    rules: this.checkDeploymentData() ? "" : [{
                                        required: true,
                                        message: "该项不能为空"
                                    }]
                                })}
                                fillProps="value"
                                hasClear={true}
                                // onInputUpdate={this.onInputUpdate.bind(this)}
                            >
                                {
                                    (this.field.getValue("deployment") === undefined || this.field.getValue("deployment") === "") ? [] : this.state.containerData[this.field.getValue("deployment")].map((item) => {
                                        return (<Option value={String(item)}>{String(item)}</Option>)
                                    })}
                            </Combobox>
                        </FormItem>
                    </div>
                )
            } else {
                return (
                    <FormItem label="Yaml文件路径"
                              {...formItemLayout}
                              validateStatus={this.yamlPathField.getError("yamlFilePath") ? "error" : ""}
                              help={this.yamlPathField.getError("yamlFilePath") ? "请输入文件路径" : ""}
                    >
                        <div
                            style={{
                                display: this.state.editMode ? "None" : "",
                                textAlign: "left",
                                marginLeft: "10%",
                                marginTop: "5px"
                            }}>{this.state.yamlData.yamlFilePath}</div>
                        <Input placeholder="Yaml文件路径"
                               style={{display: this.state.editMode ? "" : "None", width: "60%"}}
                               {...this.yamlPathField.init("yamlFilePath", {
                                   initValue: this.state.yamlData.yamlFilePath,
                                   rules: [{
                                       required: true,
                                       message: "该项不能为空"
                                   }]
                               })}/>

                    </FormItem>
                )
            }

        }
    }

    yamlEditorConfirm() {
        Dialog.confirm({
            content: "你确定要保存修改吗？",
            title: "确认修改",
            onOk: this.yamlEditorSubmit.bind(this)
        });

    }

    yamlEditorSubmit() {
        let _this = this
        _this.setState({
            loading: true
        })
        let url = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/deploymentYaml"
        Axios.put(url, {deploymentEditableYaml: _this.yamlEditorfield.getValue("deploymentYaml")}
        ).then((response) => {
            Toast.success("更新成功")
            _this.setState({
                loading: false
            })
        })
        this.toggleYamlEditor()

    }

    toggleYamlEditor() {
        this.setState({
            editDeploymentYaml: !this.state.editDeploymentYaml
        })
    }

    yamlEditorRender() {
        const {init, getValue} = this.yamlEditorfield
        if (this.state.yamlData !== [] && this.state.yamlData.deploymentEditableYaml !== "") {
            return <Form>
                <FormItem label="Deployment Yaml"
                          style={{width: "100%", height: "100%"}}
                          {...formItemLayout}
                          validateStatus={this.field.getError("deploymentYaml") ? "error" : ""}
                          help={this.field.getError("deploymentYaml") ? "该项不能为空" : ""}>
                        <Input
                            size="large"
                            rows="20"
                            style={{width: "90%"}}
                            multiple placeholder="Deployment Yaml"
                            readOnly={!this.state.editDeploymentYaml}
                            {...init('deploymentYaml', {
                                initValue: this.state.yamlData.deploymentEditableYaml,
                                rules: [{
                                    required: true,
                                    message: "该项不能为空"
                                }]
                            })}>>
                        </Input>

                        <Icon
                            style={{display: this.state.editDeploymentYaml ? "None" : "", float: "right"}}
                            type='edit'
                            onClick={this.toggleYamlEditor.bind(this)}/>

                </FormItem>
                <Button
                    style={{display: this.state.editDeploymentYaml ? "" : "None", marginRight: "5px"}}
                    onClick={this.yamlEditorConfirm.bind(this)}
                    type="primary">
                    保存
                </Button>
                < Button
                    style={{display: this.state.editDeploymentYaml ? "" : "None"}}
                    onClick={this.toggleYamlEditor.bind(this)}>
                    取消
                </Button>
            </Form>

        }


    }

    k8sBasicRender() {
        const {init, getValue} = this.field
        if (this.state.yamlData !== []) {
            return (
                <div>
                    <FormItem label="发布策略:"
                              {...formItemLayout}
                              validateStatus={this.field.getError("releaseStrategy") ? "error" : ""}
                              help={this.field.getError("releaseStrategy") ? "发布策略" : ""}>
                        <div
                            style={{
                                display: this.state.editMode ? "None" : "",
                                textAlign: "left",
                                marginLeft: "10%",
                                marginTop: "5px"
                            }}>{this.state.yamlData.releaseStrategy}
                            <Icon
                                style={{display: this.state.editMode ? "None" : "", float: "right"}}
                                type='edit' onClick={this.toggleEditMode.bind(this)}/></div>
                        <Select placeholder="发布策略"
                                style={{display: this.state.editMode ? "" : "None"}}
                                {...init('releaseStrategy', {
                                    initValue: this.state.yamlData.releaseStrategy,
                                    rules: [{required: true, message: "该项不能为空"}]
                                })}
                        >
                            <Option value="BATCH" disabled={true}>分批发布</Option>
                            <Option value="BLUE_GREEN" disabled={true}>蓝绿发布</Option>
                            <Option value="ROLLING_UPDATE">滚动升级</Option>
                        </Select>

                    </FormItem>

                    <FormItem style={{display: this.field.getValue('releaseBatch') === 'BATCH' ? "" : "None"}}
                              label="发布批次:"
                              {...formItemLayout}
                              validateStatus={this.field.getError("releaseBatch") ? "error" : ""}
                              help={this.field.getError("releaseBatch") ? "发布策略" : ""}>
                        <div
                            style={{
                                display: this.state.editMode ? "None" : "",
                                textAlign: "left",
                                marginLeft: "10%",
                                marginTop: "5px"
                            }}>{this.state.yamlData.releaseBatch}</div>
                        <NumberPicker
                            style={{display: this.state.editMode ? "" : "None"}}
                            min={1}
                            max={99}
                            placeholder="发布批次"
                            {...init('releaseBatch', {initValue: this.state.yamlData.releaseBatch})}/>
                    </FormItem>


                    {/*<FormItem label="镜像地址"*/}
                    {/*{...formItemLayout}*/}
                    {/*validateStatus={this.field.getError("imageUrl") ? "error" : ""}*/}
                    {/*help={this.field.getError("imageUrl") ? "镜像仓库地址" : ""}>*/}
                    {/*<div style={{display: this.state.editMode ? "None" : ""}}>{this.state.yamlData.imageUrl}</div>*/}
                    {/*<Input placeholder="镜像仓库地址"*/}
                    {/*style={{display: this.state.editMode ? "" : "None"}}*/}

                    {/*{...init('imageUrl', {*/}
                    {/*initValue: this.state.yamlData.imageUrl,*/}
                    {/*rules: [{*/}
                    {/*required: true,*/}
                    {/*message: "该项不能为空"*/}
                    {/*}]*/}
                    {/*})}>*/}
                    {/*</Input>*/}

                    {/*</FormItem>*/}

                    <FormItem style={{display: this.state.editMode ? "" : "None"}}
                              label="YAML文件来源:"
                              {...formItemLayout}>
                        <Combobox
                            fillProps="label"
                            onChange={this.switchYamlMode.bind(this)}
                            {...init('yamlMode')}
                            defaultValue={this.state.yamlMode}>
                            <Option value="profile">
                                使用配置
                            </Option>
                            <Option value="path">
                                使用Yaml文件相对路径
                            </Option>

                        </Combobox>
                    </FormItem>

                </div>
            )
        }
    }


    render() {

        return (
            <div style={{width: "100%"}}>
                <Loading visible={this.state.loading} style={{width: "70%"}} size='small' shape="dot-circle"
                         color="#2077FF">
                    <Form style={{width: "100%"}}>

                        {this.k8sBasicRender()}


                        {this.yamlInfoRender()}
                        <Button
                            style={{display: this.state.editMode ? "" : "None", marginRight: "5px"}}
                            onClick={this.envDetailConfirm.bind(this)}
                            type="primary"
                        >
                            保存
                        </Button>
                        < Button
                            style={{display: this.state.editMode ? "" : "None"}}
                            onClick={this.toggleEditMode.bind(this)}>
                            取消
                        </Button>
                    </Form>
                    {this.yamlEditorRender()}
                </Loading>
            </div>
        )
    }


}