import {Field, Form, Input} from "@icedesign/base";
import API from "../../../../API";
import Axios from "axios";
import React, {Component} from 'react';
import ProductModeController from "../ProductModeController"

const FormItem = Form.Item;
const style = {
    padding: "20px",
    background: "#F7F8FA",
    margin: "20px"
};
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
/**
 *    弹窗中的表单
 *
 * */
export default class ApplicationForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            projectId: props.projectId
        }
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {

        // 校验表单数据
        this.field.validate((errors, values) => {
            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let url = API.gateway + '/application-server/app/' + this.state.projectId;
                Axios.post(url, {}, {
                        params: {
                            title: this.field.getValue('title'),
                            productMode: this.field.getValue('productMode'),
                            appDescription: this.field.getValue('description'),
                            gitUrl: this.field.getValue('gitUrl')
                        }
                    }
                )
                    .then(function (response) {
                        console.log(response);
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
            // console.log((nextProps));

        }
    }

    render() {
        const {init, getValue} = this.field;
        return (
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
                              required>
                        <Input  {...init('gitUrl')} placeholder="git仓库地址"/>
                    </FormItem>
                    <FormItem {...formItemLayout} label="应用描述：">
                        <Input  {...init('description')} multiple placeholder="应用描述"/>
                    </FormItem>
                </Form>
            </div>
        )
    }
}
