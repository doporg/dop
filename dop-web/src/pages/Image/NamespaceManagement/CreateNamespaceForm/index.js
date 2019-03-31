import {Field, Form, Input, Loading} from "@icedesign/base";
import API from "../../../API";
import Axios from "axios";
import React, {Component} from 'react';

import PrivateController from "../PrivateController/index"

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
export default class NamespaceForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            loading: false
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
            let _this = this

            this.setState({
                loading: true
            })
            // 没有异常则提交表单
            if (errors == null) {
                let url = API.image + '/v1/projects';
                Axios.post(url, {}, {
                        params: {
                            organizationId: "123",
                            title: this.field.getValue('title')
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
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.isSubmit) {
            this.handleSubmit(nextProps);
        }
    }

    render() {
        const {init, getValue} = this.field;

        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                <div>
                    <Form
                        labelAlign={"left"}
                        style={style}
                    >
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("title") ? "error" : ""}
                                  help={this.field.getError("title") ? "请输入名称" : ""}
                                  label="命名空间名称："
                                  required>
                            <Input {...init('title', {rules: [{required: true, message: "该项不能为空"}]})}
                                   placeholder="请输入命名空间名称"/>
                        </FormItem>
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("private") ? "error" : ""}
                                  help={this.field.getError("private") ? "请选择公开性" : ""}
                                  label="公开性："
                                  required>
                            <PrivateController   {...init('private', {
                                rules: [{required: true, initValue: "public"}]
                            })}/>
                        </FormItem>
                        <FormItem {...formItemLayout}
                                  label="命名空间描述：">
                            <Input  {...init('description')}
                                    multiple placeholder="命名空间描述"/>
                        </FormItem>
                    </Form>
                </div>
            </Loading>
        )
    }
}
