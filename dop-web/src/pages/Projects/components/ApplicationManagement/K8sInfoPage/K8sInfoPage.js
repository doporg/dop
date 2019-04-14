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
import "./K8sInfoPage.scss"

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
            nameSpaceData: [],
            editMode: false,
            serviceData: [],
            deploymentData: [],
            yamlData: [],
            containerData: [],
            createService: false,
            yamlMode: "profile",
            editDeploymentYaml: false,
            loading: true
        }

    }

    /*
    * 获取集群内所有命名空间
    */
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

    /*
   * 获取集群内指定命名空间的所有服务
   */
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

    /*
      * 获取集群内指定命名空间的对应服务的所有部署
      */
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


    /*
  * 获取该服务在数据库中的属性
  */
    getYamlData() {
        let _this = this;
        this.setState({
            loading: true
        })
        let url = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yaml"
        Axios.get(url)
            .then((response) => {
                // console.log("yaml", response);
                // console.log(response.data.yamlFilePath !== "")

                //如果该服务在数据库中无属性
                if (response.data === "") {
                    _this.setState({
                        editMode: true,
                        yamlData: []
                    })
                    //获取所有命名空间
                    _this.getNameSpaceData()
                    _this.setState({loading: false})
                } else {
                    //判断是否使用yaml文件路径 分别赋值
                    if (response.data.yamlFilePath !== "") {

                        _this.setState({
                            editMode: false,
                            yamlData: response.data,
                            yamlMode: 'path',
                            loading: false
                        })


                    }
                    //如果是使用配置，则根据配置去集群获取对应的信息，加载到下拉框以便用户更改
                    else {
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

                    // _this.field.setValue("yamlMode", this.state.yamlMode)
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
        if (value === 'profile') {
            this.getNameSpaceData()
        }
        this.setState({
            yamlMode: value
        })
    }

    checkDeploymentData() {
        return this.state.createService ||
            (this.state.deploymentData === null) ||
            (this.state.deploymentData === "") ||
            (this.state.deploymentData.length === 0) ||
            ((this.state.deploymentData.length < 2) &&
                (this.state.containerData.length === 0 || this.state.containerData[this.state.deploymentData[0]].length < 2))
    }


    //提交时的响应函数
    envDetailSubmit() {
        let _this = this;
        //先判断是否使用yaml路径
        if (this.state.yamlMode === 'path') {
            this.yamlPathField.validate((errors, values) => {
                console.log(errors)
                if (errors === null) {
                    this.setState({
                        loading: true
                    })
                    //再判断数据库中是否有yaml信息
                    let existUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yamlStatus"
                    Axios.get(existUrl)
                        .then((response) => {
                            console.log("responsssss", response)
                            //有则更新
                            if (response.data) {
                                let url = API.application + '/app/env/' + this.state.appEnvId + '/yaml';
                                Axios.put(url, {}, {
                                        params: {
                                            deploymentStrategy: "KUBERNETES",
                                            releaseStrategy: this.field.getValue('releaseStrategy'),
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
                            //无则创建
                            else {
                                let url = API.application + '/app/env/' + this.state.appEnvId + '/yaml';
                                Axios.post(url, {}, {
                                        params: {
                                            deploymentStrategy: "KUBERNETES",
                                            releaseStrategy: this.field.getValue('releaseStrategy'),
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

                }
            })


        }
        //如果使用配置
        else {
            //判断是否需要新建服务
            if (this.state.createService) {
                //如果是则新建服务
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
                            if (errors === null) {
                                //然后判断是否存在yaml属性
                                let url = API.application + '/app/env/' + this.state.appEnvId + "/yaml";

                                let existUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yamlStatus"
                                Axios.get(existUrl)
                                    .then((response) => {
                                        console.log("responsssss", response)
                                        if (response.data) {
                                            //存在则更新
                                            Axios.put(url, {}, {
                                                    params: {
                                                        deploymentStrategy: "KUBERNETES",
                                                        nameSpace: this.field.getValue('nameSpace'),
                                                        service: this.field.getValue('service'),
                                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                                        replicas: this.editField.getValue('replicas'),
                                                        // imageUrl: this.field.getValue('imageUrl'),
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
                                        //不存在则新建
                                        else {
                                            Axios.post(url, {}, {
                                                    params: {
                                                        deploymentStrategy: "KUBERNETES",
                                                        nameSpace: this.field.getValue('nameSpace'),
                                                        service: this.field.getValue('service'),
                                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                                        replicas: this.editField.getValue('replicas'),
                                                        // imageUrl: this.field.getValue('imageUrl'),
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
            }
            //如果不需要新建服务则按类似流程执行
            else {
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
            if (errors === null) {
                let url = API.application + '/app/env/' + this.state.appEnvId + '/yaml'
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
                                        // imageUrl: this.field.getValue('imageUrl'),
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
                                        // imageUrl: this.field.getValue('imageUrl'),
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
        const {init} = this.field
        if (!this.state.loading) {
            if (this.state.yamlMode === "profile") {
                return (
                    <div>
                        <FormItem label="命名空间:"
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("nameSpace") ? "error" : ""}
                                  help={this.field.getError("nameSpace") ? "请选择命名空间" : ""}>
                            <div
                                className={this.state.editMode ? "form-item-text hide" : "form-item-text"}
                            >{this.state.yamlData.nameSpace}</div>
                            <Combobox
                                className={this.state.editMode ? "form-item-combobox" : "form-item-combobox hide"}
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

                        <FormItem className={this.state.editMode ? "form-item-switch" : "form-item-switch hide"}
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
                                className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.service}</div>
                            <Combobox
                                className={this.state.editMode ? "form-item-combobox" : "form-item-combobox hide"}
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
                        <FormItem
                            className={this.state.editMode && this.state.createService ? "form-item-create-service" : "form-item-create-service hide"}
                                  label="端口:"
                                  {...formItemLayout}
                                  validateStatus={this.editField.getError("port") ? "error" : ""}
                                  help={this.editField.getError("port") ? "请输入正确的端口" : ""}
                        >
                            <Input placeholder="开放端口"
                                   defaultValue={0}
                                   className="port-input"
                                   {...this.editField.init("port", {rules: [{required: true, message: "该项不能为空"}]})}/>

                        </FormItem>

                        <FormItem
                            className={this.state.editMode && this.state.createService ? "form-item-create-service" : "form-item-create-service hide"}
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
                            className={this.checkDeploymentData() ? "form-item-deployment-detail hide" : "form-item-deployment-detail"}
                        >
                            <div
                                className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.deployment}</div>
                            <Combobox
                                className={this.state.editMode ? "form-item-combobox" : "form-item-combobox hide"}
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
                                  className={this.checkDeploymentData() ? "form-item-deployment-detail hide" : "form-item-deployment-detail"}

                        >
                            <div
                                className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.container}</div>
                            <Combobox
                                className={this.state.editMode ? "form-item-combobox" : "form-item-combobox hide"}
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
                              required
                    >
                        <div
                            className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.yamlFilePath}</div>
                        <Input placeholder="Yaml文件路径"
                               className={this.state.editMode ? "form-item-yaml-path-input" : "form-item-yaml-path-input hide"}
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
        const {init} = this.yamlEditorfield
        if (!this.state.loading && this.state.yamlData.length !== 0 && this.state.yamlData.deploymentEditableYaml !== "") {
            return <Form className="yaml-editor-form">
                <FormItem label="Deployment Yaml"
                          className="yaml-editor-form-item"
                          {...formItemLayout}
                          validateStatus={this.field.getError("deploymentYaml") ? "error" : ""}
                          help={this.field.getError("deploymentYaml") ? "该项不能为空" : ""}>
                    <Input
                        className="yaml-input"
                        // size="large"
                        rows="20"
                        multiple placeholder="Deployment Yaml"
                        readOnly={!this.state.editDeploymentYaml}
                        {...init('deploymentYaml', {
                            initValue: this.state.yamlData.deploymentEditableYaml,
                            rules: [{
                                required: true,
                                message: "该项不能为空"
                            }]
                        })}>
                    </Input>

                    <Icon
                        className={this.state.editDeploymentYaml ? "edit-yaml-icon hide" : "edit-yaml-icon"}
                        type='edit'
                        onClick={this.toggleYamlEditor.bind(this)}/>

                </FormItem>
                <Button
                    className={this.state.editDeploymentYaml ? "save-yaml-button" : "save-yaml-button hide"}

                    onClick={this.yamlEditorConfirm.bind(this)}
                    type="primary">
                    保存
                </Button>
                < Button
                    className={this.state.editDeploymentYaml ? "cancel-yaml-button" : "cancel-yaml-button hide"}
                    onClick={this.toggleYamlEditor.bind(this)}>
                    取消
                </Button>
            </Form>

        }


    }

    k8sBasicRender() {
        const {init} = this.field
        console.log("yamlData", this.state.yamlData, this.state.yamlData !== [])
        if (!this.state.loading) {
            console.log(this.state.yamlData.yamlFilePath === "")
            return (
                <div>
                    <FormItem label="发布策略:"
                              {...formItemLayout}
                              validateStatus={this.field.getError("releaseStrategy") ? "error" : ""}
                              help={this.field.getError("releaseStrategy") ? "发布策略" : ""}>
                        <div
                            className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.releaseStrategy}
                            <Icon
                                className={this.state.editMode ? "edit-k8s-icon hide" : "edit-k8s-icon"}
                                type='edit' onClick={this.toggleEditMode.bind(this)}/></div>
                        <Select placeholder="发布策略"
                                className={this.state.editMode ? "form-item-select" : "form-item-select hide"}
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

                    <FormItem
                        className={this.field.getValue('releaseBatch') === 'BATCH' ? "form-item-release-batch" : "form-item-release-batch hide"}
                              label="发布批次:"
                              {...formItemLayout}
                              validateStatus={this.field.getError("releaseBatch") ? "error" : ""}
                              help={this.field.getError("releaseBatch") ? "发布策略" : ""}>
                        <div
                            className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.releaseBatch}</div>
                        <NumberPicker
                            className={this.state.editMode ? "form-item-NumberPicker" : "form-item-NumberPicker hide"}
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

                    <FormItem className={this.state.editMode ? "form-item-yaml-origin" : "form-item-yaml-origin hide"}
                              label="YAML文件来源:"
                              {...formItemLayout}>
                        <Select
                            fillProps="label"
                            onChange={this.switchYamlMode.bind(this)}
                            defaultValue={(this.state.yamlData.length === 0 || this.state.yamlFilePath === "") ? "profile" : "path"}>
                            <Option value="profile">
                                使用配置
                            </Option>
                            <Option value="path">
                                使用Yaml文件相对路径
                            </Option>

                        </Select>
                    </FormItem>

                </div>
            )
        }
    }


    render() {

        return (
            <div className="form-loading-container">
                <Form className="form">
                    <Loading visible={this.state.loading} shape="dot-circle"
                             color="#2077FF" className="form-loading">


                        {this.k8sBasicRender()}


                        {this.yamlInfoRender()}
                        <Button
                            className={this.state.editMode ? "save-button" : "save-button hide"}
                            onClick={this.envDetailConfirm.bind(this)}
                            type="primary"
                        >
                            保存
                        </Button>
                        < Button
                            className={this.state.editMode ? "cancel-button" : "cancel-button hide"}
                            onClick={this.toggleEditMode.bind(this)}>
                            取消
                        </Button>


                        {this.yamlEditorRender()}
                </Loading>
                </Form>
            </div>
        )
    }


}