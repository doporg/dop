import {Field, Form, Input, Loading,Select,Grid} from "@icedesign/base";
import API from "../../../API";
import Axios from "axios";
import React, {Component} from 'react';
import RoleController from '../RoleController'

const FormItem = Form.Item;
const {Col} = Grid
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
            loading: false,
            projectId:this.props.projectId
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
            let _this = this;
            // 没有异常则提交表单
            if (errors == null) {
                let url = API.image + '/v1/projects/'+_this.state.projectId+"/members";
                Axios.post(url, {}, {
                        params: {
                            userName: values.userName,
                            roleId: values.role
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
        const {init} = this.field;

        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                <div>
                    <Form
                        labelAlign={"left"}
                        style={style}
                    >
                        <Col>
                            <FormItem {...formItemLayout}
                                      validateStatus={this.field.getError("title") ? "error" : ""}
                                      help={this.field.getError("title") ? "请输入名称" : ""}
                                      label="用户名称："
                                      required>
                                <Input {...init('userName', {rules: [{required: true, message: "该项不能为空"}]})}
                                       placeholder="请输入用户名称"/>
                            </FormItem>
                            <FormItem {...formItemLayout}
                                      validateStatus={this.field.getError("role") ? "error" : ""}
                                      help={this.field.getError("role") ? "请选择用户角色" : ""}
                                      label="角色："
                                      required>
                                <RoleController   {...init('role', {
                                    rules: [{required: true}],initValue:"0"
                                })}/>
                            </FormItem>
                        </Col>
                    </Form>
                </div>
            </Loading>
        )
    }
}
