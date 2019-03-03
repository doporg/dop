import React, {Component} from 'react';
import './ModifyPwd.scss';
import Logo from '../../../components/Logo'
import Footer from "../../../components/Footer";
import {Form, Input, Button, Field, Feedback} from "@icedesign/base";
import API from "../../API";
import Axios from "axios/index";
import {Encryption, PublicKey} from '../index'


const {Item: FormItem} = Form;
const {toast} = Feedback;


export default class ModifyPwd extends Component {
    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            visible: true
        }
    }

    handleReset(e) {
        e.preventDefault();
        this.field.reset();
    }

    handleSubmit(e) {
        e.preventDefault();
        let self = this;
        self.field.validate((errors, values) => {
            if (errors) {
                toast.show({
                    type: "error",
                    content: "表单错误",
                    duration: 1000
                });
                return;
            }
            console.log("Submit!!!");
            self.submit(values);
        });
    }

    reset(e) {
        e.preventDefault();
        let self = this;
        self.field.validate((errors, values) => {
            if (errors) {
                toast.show({
                    type: "error",
                    content: "表单错误",
                    duration: 1000
                });
                return;
            }
            console.log("Submit!!!");
            self.code(values);
        });
    }

    code(data) {
        console.log(data)
        let url = API.gateway + '/user-server/v1/account/reset?email=' + data.email;
        let self = this;
        Axios.post(url).then((response) => {
            if (response.status === 200) {
                self.setState({
                    visible: false
                });
                toast.show({
                    type: "success",
                    content: "请注意检查邮箱获取验证码",
                    duration: 2000
                });
            }
        }).catch((error) => {
            toast.show({
                type: "error",
                content: error,
                duration: 2000
            });
        })
    }

    submit(data) {
        console.log(data)
        let url = API.gateway + '/user-server/v1/users/password';
        let self = this;
        delete data.rePasswd;
        PublicKey().then((publicKey) => {
            data.password = Encryption(data.passwd, publicKey);
            delete data.passwd;
            console.log(data);
            Axios.put(url, data).then((response) => {
                console.log(response)
                if (response.status === 200) {
                    toast.show({
                        type: "success",
                        content: "修改成功",
                        duration: 2000
                    })
                }
                self.props.history.push('/login');
            }).catch((error)=>{
                toast.show({
                    type: "error",
                    content: error,
                    duration: 2000
                });
                self.props.history.push('/login');
            })
        }).catch((error) => {
            toast.show({
                type: "error",
                content: error,
                duration: 2000
            });
        })
    }


    checkPass(rule, value, callback) {
        const {validate} = this.field;
        let reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.-=])[0-9a-zA-Z!@#$%.-=]{4,20}$/;
        if (reg.test(value)) {
            validate(["rePasswd"]);
            callback();
        } else {
            callback([new Error("长度至少为6个字符，最大长度为20, 必须包含一个数字 0-9、包含一个小写字符、包含一个大写字符、包含一个的特殊字符@#$%.-=")]);
        }

    }

    checkPass2(rule, value, callback) {
        const {getValue} = this.field;
        if (value && value !== getValue("passwd")) {
            callback("两次输入密码不一致！");
        } else {
            callback();
        }
    }

    render() {
        const {init} = this.field;
        const formItemLayout = {
            labelCol: {
                span: 6
            },
            wrapperCol: {
                span: 14
            }
        };
        return (
            <div className="register-body">
                <div className="header-content">
                    <Logo isDark/>
                </div>
                <div className="register-content">
                    <div className="title">
                        修改密码DevopsPlatform
                    </div>
                    {(() => {
                        if (this.state.visible) {
                            return (
                                <Form field={this.field}>
                                    <FormItem label="邮箱：" {...formItemLayout} hasFeedback>
                                        <Input
                                            type="email"
                                            placeholder="请输入邮箱"
                                            {...init("email", {
                                                rules: [
                                                    {required: true, trigger: "onBlur"},
                                                    {
                                                        type: "email",
                                                        message: <span>请输入正确的邮箱地址</span>,
                                                        trigger: ["onBlur", "onChange"]
                                                    }
                                                ]
                                            })}
                                        />
                                    </FormItem>

                                    <FormItem wrapperCol={{offset: 6}}>
                                        <Button type="primary" onClick={this.reset.bind(this)}>
                                            确定
                                        </Button>
                                    </FormItem>
                                </Form>
                            )
                        } else {
                            return (
                                <Form field={this.field}>
                                    <FormItem label="邮箱：" {...formItemLayout} hasFeedback>
                                        <Input
                                            type="email"
                                            placeholder="请输入邮箱"
                                            {...init("email", {
                                                rules: [
                                                    {required: true, trigger: "onBlur"},
                                                    {
                                                        type: "email",
                                                        message: <span>请输入正确的邮箱地址</span>,
                                                        trigger: ["onBlur", "onChange"]
                                                    }
                                                ]
                                            })}
                                        />
                                    </FormItem>

                                    <FormItem label="新密码：" {...formItemLayout} hasFeedback>
                                        <Input
                                            maxLength={20}
                                            placeholder="请输入密码"
                                            htmlType="password"
                                            {...init("passwd", {
                                                rules: [
                                                    {required: true, whitespace: true, min: 6, message: "密码用至少为 6 个字符"},
                                                    {validator: this.checkPass.bind(this)}
                                                ]
                                            })}
                                        />
                                    </FormItem>

                                    <FormItem label="确认密码：" {...formItemLayout} hasFeedback>
                                        <Input
                                            htmlType="password"
                                            placeholder="两次输入密码保持一致"
                                            {...init("rePasswd", {
                                                rules: [
                                                    {
                                                        required: true,
                                                        whitespace: true,
                                                        message: "请再次输入密码"
                                                    },
                                                    {
                                                        validator: this.checkPass2.bind(this)
                                                    }
                                                ]
                                            })}
                                        />
                                    </FormItem>

                                    <FormItem
                                        label="验证码："
                                        {...formItemLayout}
                                    >
                                        <Input
                                            hasLimitHint
                                            placeholder="请检查您的邮箱获得验证码"
                                            {...init("code", {
                                                rules: [
                                                    {required: true, message: "请检查您的邮箱获得验证码"},
                                                ]
                                            })}
                                        />
                                    </FormItem>

                                    <FormItem wrapperCol={{offset: 6}}>
                                        <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                                            确定
                                        </Button>
                                        &nbsp;&nbsp;&nbsp;
                                        <Button onClick={this.handleReset.bind(this)}>重置</Button>
                                    </FormItem>
                                </Form>
                            )

                        }
                    })()}

                </div>
                <Footer/>
            </div>
        )
    }
}
