import React, {Component} from 'react';
import './Register.scss';
import Logo from '../../../components/Logo'
import Footer from "../../../components/Footer";
import { Form, Input, Button, Radio, Field } from "@icedesign/base";
const { Item: FormItem } = Form;
const { Group: RadioGroup } = Radio;

export default class Register extends Component{
    constructor(props){
        super(props);
        this.field = new Field(this);
    }
    componentDidMount(){

    }

    handleReset(e) {
        e.preventDefault();
        this.field.reset();
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

    userExists(rule, value, callback) {
        console.log(value);
        let reg = /^(?!.*?_$)[a-zA-Z0-9_]+$/;
        if(reg.test(value)){
            callback();
        }else{
            callback([new Error(" 至少一个数字、字母、下划线，不能以下划线结尾")]);
        }
    }

    checkPass(rule, value, callback) {
        const { validate } = this.field;
        let reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.-=])[0-9a-zA-Z!@#$%.-=]{4,20}$/;
        if(reg.test(value)){
            validate(["rePasswd"]);
            callback();
        }else{
            callback([new Error("长度至少为6个字符，最大长度为20, 必须包含一个数字 0-9、包含一个小写字符、包含一个大写字符、包含一个的特殊字符@#$%.-=")]);
        }

    }

    checkPass2(rule, value, callback) {
        const { getValue } = this.field;
        if (value && value !== getValue("passwd")) {
            callback("两次输入密码不一致！");
        } else {
            callback();
        }
    }
    render(){
        const { init, getError, getState } = this.field;
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
                        欢迎注册DevopsPlatform
                    </div>
                    <Form field={this.field}>
                        <FormItem
                            label="用户名："
                            {...formItemLayout}
                            hasFeedback
                            help={
                                getState("name") === "validating"
                                    ? "校验中..."
                                    : (getError("name") || []).join(", ")
                            }
                        >
                            <Input
                                hasLimitHint
                                placeholder="请输入用户名"
                                {...init("name", {
                                    rules: [
                                        { required: true, message: "至少一个数字、字母、下划线，不能以下划线结尾" },
                                        { validator: this.userExists }
                                    ]
                                })}
                            />
                        </FormItem>

                        <FormItem label="邮箱：" {...formItemLayout} hasFeedback>
                            <Input
                                type="email"
                                placeholder="请输入邮箱"
                                {...init("email", {
                                    rules: [
                                        { required: true, trigger: "onBlur" },
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
                                maxLength={20}
                                placeholder="请输入密码"
                                htmlType="password"
                                {...init("passwd", {
                                    rules: [
                                        { required: true, whitespace: true ,min: 6, message: "密码用至少为 6 个字符"},
                                        { validator: this.checkPass.bind(this) }
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

                        <FormItem wrapperCol={{ offset: 6 }}>
                            <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                                确定
                            </Button>
                            &nbsp;&nbsp;&nbsp;
                            <Button onClick={this.handleReset.bind(this)}>重置</Button>
                        </FormItem>
                    </Form>
                </div>
                <Footer/>
            </div>
        )
    }
}
