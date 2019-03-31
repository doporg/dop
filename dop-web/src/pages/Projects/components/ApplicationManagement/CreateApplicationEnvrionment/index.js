import {Button, Dialog, Field, Form, Grid, Input, Loading, Select} from "@icedesign/base";


import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API.js"

const {Combobox} = Select;
const FormItem = Form.Item;
const {Row, Col} = Grid;
const Option = Select.Option;
const style = {
    padding: "20px",
    background: "#FFF",
    margin: "20px",
    width: "100%"
};

const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};


/**
 *  创建应用变量的弹窗
 *  @author Bowen
 *
 * */

class ApplicationEnvironmentForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            appId: props.appId,
            loading: false
        }
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {

        // 校验表单数据
        let _this = this;
        this.setState({
            loading: true
        })
        this.field.validate((errors, values) => {
            console.log(errors, values);
            console.log(_this.field.getValue('environmentLevel'))
            console.log(_this.field.getValue('deploymentStrategy'))

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let postUrl = API.gateway + "/application-server/app/" + this.state.appId + "/env/";
                Axios.post(postUrl, {}, {
                        params: {
                            "title": _this.field.getValue('title'),
                            "environmentLevel": _this.field.getValue('environmentLevel'),
                            "deploymentStrategy": _this.field.getValue('deploymentStrategy')
                        }
                    }
                )
                    .then(function (response) {
                        console.log(response);
                        _this.setState({
                            loading: false
                        })
                        props.finished();
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            }
        });

        //
        // console.log("handleSubmit");
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.isSubmit) {
            this.handleSubmit(nextProps);
        }
    }

    render() {
        const {init, getValue} = this.field;
        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF" style={{width: "90%"}}
            >
                <div>
                    <Form
                        labelAlign={"left"}
                        style={style}
                    >
                        <FormItem
                            {...formItemLayout}
                            validateStatus={this.field.getError("title") ? "error" : ""}
                            help={this.field.getError("title") ? "请输入Key" : ""}
                            label="环境名称："
                            required>
                            <Input {...init('title', {rules: [{required: true, message: "该项不能为空"}]})}
                                   placeholder="环境名称"/>
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            validateStatus={this.field.getError("environmentLevel") ? "error" : ""}
                            label="环境级别："
                            required>
                            <Select  {...init('environmentLevel', {rules: [{required: true, message: "该项不能为空"}]})}
                                     placeholder="环境级别">
                                <Option value="DAILY">日常环境</Option>
                                <Option value="PRERELEASE">预发环境</Option>
                                <Option value="RELEASE">正式环境</Option>
                            </Select>
                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            validateStatus={this.field.getError("deploymentStrategy") ? "error" : ""}
                            label="部署方式："
                            required>
                            <Select  {...init('deploymentStrategy', {rules: [{required: true, message: "该项不能为空"}]})}
                                     placeholder="部署方式">
                                <Option value="KUBERNETES">Kubernetes部署</Option>
                            </Select>
                        </FormItem>

                    </Form>
                </div>
            </Loading>)
    }
}

/**
 *    创建应用环境的弹窗
 *
 * */
export default class CreateApplicationEnvironmentDialog extends Component {


    onClose = () => {
        this.setState({
            visible: false
        });
    };

    onOk = () => {
        this.setState({
            isSubmit: true
            // visible: false
        });
    }


    onOpen = () => {
        this.setState({
            visible: true
        });
    };
    onCreateDialogClose = () => {
        this.setState({
            createDialogVisible: false
        });
    };

    constructor(props) {
        super(props);

        this.state = {
            //是否已经提交
            isSubmit: false,
            footerAlign: "center",
            visible: false,
            style: {
                width: "30%"
            },
            createDialogStyle: {
                width: "10%"
            },
            createDialogVisible: false,
            refreshApplicationEnvironmentList: props.refreshApplicationEnvironmentList,
            appId: props.appId
        }
    };

    /**
     *    回调函数传给子组件表单用于创建完成后修改提交状态和关闭弹窗
     *
     * */
    finished() {
        this.setState({
            visible: false,
            createDialogVisible: true,
            isSubmit: false
        })

        this.state.refreshApplicationEnvironmentList();
        console.log("finished");
    }

    render() {
        return (

            <span>
                <Button onClick={this.onOpen} type="primary"
                        style={{margin: "20px"}}>
          新建环境
        </Button>
        <Dialog
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title="新建环境"
            style={this.state.style}
            footerAlign={this.state.footerAlign}
        >
          <ApplicationEnvironmentForm isSubmit={this.state.isSubmit} finished={this.finished.bind(this)}
                                      appId={this.state.appId}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title="新建成功"
        style={this.state.createDialogStyle}
        footerAlign={this.state.footerAlign}>
新建成功！
</Dialog>
      </span>
        );
    }


}
