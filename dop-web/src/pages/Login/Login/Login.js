import React, {Component} from 'react';
import Logo from '../../../components/Logo'
import './Login.scss';
import Footer from "../../../components/Footer";
import {Form, Input, Button, Field} from "@icedesign/base";

const {Item: FormItem} = Form;

export default class Login extends Component {
    constructor(props) {
        super(props);
        this.field = new Field(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        this.field.validate((errors, values) => {
            if (errors) {
                console.log("Errors in form!!!");
                return;
            }
            console.log("Submit!!!");
            console.log(values);
        });
    }

    checkPass(rule, value, callback) {
        const {validate} = this.field;
        if (value) {
            validate(["rePasswd"]);
        }
        callback();
    }
    register(){
        this.props.history.push('/register')
    }

    render() {
        const {init} = this.field;
        const formItemLayout = {
            labelCol: {
                span: 6,
            },
            wrapperCol: {
                span: 14,
            },
        };
        return (
            <div className="login-body">
                <div className="header-content">
                    <Logo isDark/>
                </div>
                <div className="login-content">
                    <div className="left">
                        <div className="img">
                            <img src={require('./images/logo.png')} width="100%"/>
                        </div>
                    </div>
                    <div className="right">
                        <div className="form">
                            <div className="title">
                                密码登陆
                            </div>
                            <Form field={this.field} className="form-body">

                                <FormItem label="邮箱：" {...formItemLayout} hasFeedback>
                                    <Input
                                        type="email"
                                        placeholder=""
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

                                <FormItem label="密码：" {...formItemLayout} hasFeedback>
                                    <Input
                                        htmlType="password"
                                        {...init("passwd", {
                                            rules: [
                                                {required: true, whitespace: true, message: "请填写密码"},
                                                {validator: this.checkPass.bind(this)}
                                            ]
                                        })}
                                    />
                                </FormItem>

                                <FormItem  className="submit">
                                    <Button type="primary" onClick={this.handleSubmit.bind(this)}  size="large" className="login-form-button">
                                        登陆
                                    </Button>
                                    <div className="register-content">
                                        <span>忘记密码</span>
                                        <span onClick={this.register.bind(this)}>免费注册</span>
                                    </div>
                                </FormItem>
                            </Form>
                        </div>
                    </div>
                </div>
                <Footer/>
            </div>
        )
    }
}
