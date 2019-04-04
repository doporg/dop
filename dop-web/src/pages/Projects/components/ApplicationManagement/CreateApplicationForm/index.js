import {Field, Form, Input, Loading} from "@icedesign/base";
import API from "../../../../API";
import Axios from "axios";
import React, {Component} from 'react';
import ProductModeController from "../ProductModeController"

const FormItem = Form.Item;
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
// const  REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
/**
 *    弹窗中的表单
 *
 * */
export default class ApplicationForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            projectId: props.projectId,
            loading: false
        }
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {
        let _this = this
        // 校验表单数据
        this.field.validate((errors, values) => {
            console.log(errors, values);

            // 没有异常则提交表单
            if (errors === null) {
                console.log("noerros");
                this.setState({
                    loading: true
                })
                let url = API.gateway + '/application-server/app/' + this.state.projectId;
                Axios.post(url, {}, {
                        params: {
                            title: this.field.getValue('title'),
                            productMode: this.field.getValue('productMode'),
                            appDescription: this.field.getValue('description'),
                            gitUrl: this.field.getValue('gitUrl'),
                            imageUrl: this.field.getValue('imageUrl')
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
                        _this.setState({
                            loading: false
                        })
                    });

            }
        });

        //
        // console.log("handleSubmit");
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.isSubmit) {
            this.handleSubmit(nextProps);
            // console.log((nextProps));

        }
    }

    render() {
        const {init, getValue} = this.field;
        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF" style={{width: "90%"}}>
                <div>
                    <Form
                        labelAlign={"left"}
                        style={style}
                    >
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("title") ? "error" : ""}
                                  help={this.field.getError("title") ? "请输入名称" : ""}
                                  label="应用名称："
                                  required>
                            <Input {...init('title', {rules: [{required: true, message: "该项不能为空"}]})}
                                   placeholder="请输入项目名称"/>
                        </FormItem>

                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("productMode") ? "error" : ""}
                                  help={this.field.getError("productMode") ? "请选择开发模式" : ""}
                                  label="开发模式："
                                  required>

                            <ProductModeController {...init('productMode', {
                                rules: [{required: true, initValue: "BRANCH"}]
                            })}/>
                        </FormItem>
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("gitUrl") ? "error" : ""}
                                  label="git仓库地址："
                                  help={this.field.getError("gitUrl") ? "请输入Git仓库地址" : ""}
                                  required>
                            <Input  {...init('gitUrl', {rules: [{required: true, message: "该项不能为空"}]})}
                                    placeholder="git仓库地址"/>
                        </FormItem>
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("imageUrl") ? "error" : ""}
                                  help={this.field.getError("imageUrl") ? "请输入镜像仓库地址" : ""}
                                  label="镜像仓库地址："
                                  required>
                            <Input  {...init('imageUrl', {rules: [{required: true, message: "该项不能为空"}]})}
                                    placeholder="镜像仓库地址"/>
                        </FormItem>
                        <FormItem {...formItemLayout} label="应用描述：">
                            <Input  {...init('description')} multiple placeholder="应用描述"/>
                        </FormItem>
                    </Form>
                </div>
            </Loading>
        )
    }
}
