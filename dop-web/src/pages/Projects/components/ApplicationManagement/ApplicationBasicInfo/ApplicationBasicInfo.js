import React, {Component} from 'react';
import {Breadcrumb, Button, Card, Dialog, Feedback, Field, Form, Input, Loading} from '@icedesign/base';
import Axios from "axios";
import API from "../../../../API";
import TopBar from "./topbar";
import "./ApplicationBasicInfo.scss"

const Toast = Feedback.toast;
const FormItem = Form.Item;
const formItemLayout = {
    labelCol: {span: 10},
    wrapperCol: {span: 14}
};


/**
 * 应用基本信息页面
 * @author Bowen
 */

export default class ApplicationBasicInfo extends Component {

    constructor(props) {
        super(props);
        console.log(props)
        this.field = new Field(this);
        console.log(props)
        this.urlField = new Field(this);
        this.state = {
            urlEditMode: false,
            basicEditMode: false,
            appId: props.appId,
            appBasicData: [],
            userData: [],
            loading: true,
            projectId: props.projectId
        }
    }

    //加载应用基本信息
    componentDidMount() {
        this.getData()
    }

    getData() {
        this.setState({
            loading: true
        })
        let _this = this;
        let url = API.application + '/app/' + _this.state.appId + "/urlInfo";

        //获取应用基本信息
        Axios.get(url)
            .then(function (response) {
                console.log(response)

                //获取用户基本信息
                let userUrl = API.gateway + '/user-server/v1/users/' + response.data.ouser;
                let appBasicData = response.data;
                Axios.get(userUrl)
                    .then(function (response) {
                        _this.setState({
                            appBasicData: appBasicData,
                            basicEditMode: false,
                            userData: response.data,
                            loading: false
                        })

                    })

            })
    }

    //切换基本信息的编辑状态
    basicEdit() {
        this.setState({
            basicEditMode: true
        });
    }

    //切换基本信息的编辑状态
    basicEditCancel() {
        this.setState({
            basicEditMode: false
        });
    }

    //切换URL信息的编辑状态
    urlEdit() {
        this.setState({
            urlEditMode: true
        });
    }

    //切换URL信息的编辑状态
    urlEditCancel() {
        this.setState({
            urlEditMode: false
        });
    }

    basicConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存修改吗？",
            title: "确认修改",
            onOk: this.basicSubmit.bind(this)
        });
    };

    urlConfirm = () => {
        Dialog.confirm({
            content: "你确定要保存修改吗？",
            title: "确认修改",
            onOk: this.urlSubmit.bind(this)
        });
    };

    //基本信息提交处理函数
    basicSubmit() {
        let _this = this;

        //校验输入
        this.field.validate((errors, values) => {

            console.log(errors, values);


            // 没有异常则提交表单
            if (errors == null) {
                this.setState({
                    loading: true
                })
                console.log("noerros");
                let url = API.application + '/app/' + this.state.appId;
                Axios.put(url, {}, {
                        params: {
                            title: this.field.getValue('title'),
                            description: this.field.getValue('description')
                        }
                    }
                )
                    .then(function (response) {

                        _this.getData()
                        Toast.success("更新成功！")
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            }
        });

    }

//Url信息提交处理函数
    urlSubmit() {
        let _this = this;
        //校验输入
        this.urlField.validate((errors, values) => {

            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let url = API.application + '/app/' + this.state.appId + "/urlInfo"
                this.setState({
                    loading: true
                })
                Axios.put(url, {}, {
                        params: {
                            warehouseUrl: this.urlField.getValue("warehouseUrl"),
                            imageUrl: this.urlField.getValue("imageUrl"),
                            productionDbUrl: this.urlField.getValue("productionDbUrl"),
                            testDbUrl: this.urlField.getValue("testDbUrl"),
                            productionDomain: this.urlField.getValue("productionDomain"),
                            testDomain: this.urlField.getValue("testDomain"),
                        }
                    }
                )
                    .then(function (response) {

                        //提交完成后刷新当前页面
                        _this.getData()
                        Toast.success("更新成功！")
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            }
        });
    }

    render() {

        const titleOpr = () => {
            const {init} = this.field;
            if (!this.state.basicEditMode) {
                return (
                    <div  {...init('title')}
                          placeholder="应用名称"
                          className="form-item-text">{this.state.appBasicData.title}</div>)
            } else {
                return (<Input defaultValue={this.state.appBasicData.title} {...init('title', {
                    rules: [{
                        required: true,
                        message: "该项不能为空"
                    }]
                })}
                               placeholder="应用标题"/>)
            }

        }
        const opr = () => {
            const {init} = this.field;
            if (!this.state.basicEditMode) {
                return (
                    <span className="form-item-text">{this.state.appBasicData.description}</span>
                )
            } else {
                return (
                    <Input defaultValue={this.state.appBasicData.description} {...init('description')}
                           placeholder="应用描述"/>)
            }
        }

        const urlFormRender = () => {
            const {init} = this.urlField;
            console.log((this.state.appBasicData))
            if (this.state.urlEditMode) {
                return (
                    <div>
                        <FormItem{...formItemLayout} label="Git仓库地址："
                                 validateStatus={this.urlField.getError("warehouseUrl") ? "error" : ""}
                                 help={this.urlField.getError("warehouseUrl") ? "请输入Git仓库地址" : ""}
                                 required>
                            <Input
                                {...init('warehouseUrl', {
                                    rules: [{
                                        required: true,
                                        message: "该项不能为空"
                                    }]
                                })}
                                defaultValue={this.state.appBasicData.warehouseUrl}

                                placeholder="Git仓库地址"/>
                        </FormItem>

                        <FormItem{...formItemLayout} label="镜像仓库地址："
                                 validateStatus={this.urlField.getError("imageUrl") ? "error" : ""}
                                 help={this.urlField.getError("imageUrl") ? "请输入镜像仓库地址" : ""}

                                 required>
                            <Input
                                {...init('imageUrl', {
                                    rules: [{
                                        required: true,
                                        message: "该项不能为空"
                                    }]
                                })}
                                defaultValue={this.state.appBasicData.imageUrl}

                                placeholder="镜像仓库地址"/>
                        </FormItem>

                        <FormItem{...formItemLayout} label="开发数据库地址：">
                            <Input defaultValue={this.state.appBasicData.productionDbUrl} {...init('productionDbUrl')}
                                   placeholder="开发数据库地址"/>
                        </FormItem>

                        <FormItem{...formItemLayout} label="测试数据库地址：">
                            <Input defaultValue={this.state.appBasicData.testDbUrl} {...init('testDbUrl')}
                                   placeholder="测试数据库地址"/>
                        </FormItem>

                        <FormItem{...formItemLayout} label="开发域名：">
                            <Input
                                defaultValue={this.state.appBasicData.productionDomain}  {...init('productionDomain')}
                                placeholder="开发域名"/>
                        </FormItem>

                        <FormItem{...formItemLayout} label="测试域名：">
                            <Input defaultValue={this.state.appBasicData.testDomain} {...init('testDomain')}
                                   placeholder="测试域名"/>
                        </FormItem>

                    </div>
                )
            } else {
                return (


                    <div>

                        <FormItem{...formItemLayout} label="Git仓库地址：">
                            <span className="form-item-text">{this.state.appBasicData.warehouseUrl}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label="镜像仓库地址：">
                            <span className="form-item-text">{this.state.appBasicData.imageUrl}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label="开发数据库地址：">
                            <span
                                className="form-item-text">{this.state.appBasicData.productionDbUrl}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label="测试数据库地址：">
                            <span className="form-item-text">{this.state.appBasicData.testDbUrl}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label="开发域名：">
                            <span
                                className="form-item-text">{this.state.appBasicData.productionDomain}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label="测试域名：">
                            <span className="form-item-text">{this.state.appBasicData.testDomain}</span>
                        </FormItem>
                    </div>
                )
            }
        }


        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF"

            >

                <TopBar
                    extraBefore={<Breadcrumb>
                        <Breadcrumb.Item link="#/project">所有项目</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/application?projectId=" + this.state.projectId}>{"项目：" + this.state.projectId}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{"应用：" + this.state.appId}</Breadcrumb.Item>
                    </Breadcrumb>}
                />
                <div className="card-container "
                >

                    <Card
                        className="user-card"
                        title={this.state.userData.name}
                        bodyHeight="100%"
                        subTitle="应用拥有人"
                        extra={<a href="#">转交应用&gt;</a>}
                    >
                        <img
                            src={this.state.userData.avatarURL == "" ? "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQBAMAAAB8P++eAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAYUExURcHBwb+/v7+/v76+vujo6OHh4cnJydTU1IOqnXYAAAADdFJOUxPppyMYpxkAAAD6SURBVEjH7dfbDYIwFAbguoHRCYwTKLcBOIUBaHQAIAxQwvwSEQpyaH/FFxP+5y89vacV4uQBOQix86DsxRmDV3HE4EV4YDa4QQRWSjYILKnNzQ0jekY7Yd3B1AVDeiV3wKCHsQPWPUwdkIbYYWSgtsLAwMwKfQNjFCZWWPwBhEcNz+NoZfLfrLXZPYkD+gtd/H6H97UT5+EK0FPY1ZbABaDYygysuTEvtqg9sI9AiyV/o8xgRNj0DLtHaiuszOahxgJLGueeL8Gpa8vnPHx30yEZGKo5lBwMiEnGwIKDKQMVB+UaSGzWwO2psMGPIfxgh78A8KcC/aY8ACmMo3JtJ3ljAAAAAElFTkSuQmCC" : this.state.userData.avatarURL}
                            className="avatar"/>
                    </Card>

                    <Card
                        className="basic-card"
                        title={"基本信息"}
                        bodyHeight="35%"
                        subTitle={
                            <div className="sub-title">
                                <Button type="primary"
                                        className={this.state.basicEditMode == true ? "edit-button hide" : "edit-button"}
                                        onClick={this.basicEdit.bind(this)}
                                        size="small">
                                    修改
                                </Button>
                                <Button onClick={this.basicConfirm.bind(this)}
                                        type="primary"
                                        size="small"
                                        className={this.state.basicEditMode == true ? "save-button" : "save-button hide"}>
                                    保存
                                </Button>

                                < Button
                                    size="small"
                                    className={this.state.basicEditMode == true ? "cancel-button" : "cancel-button hide"}
                                    onClick={this.basicEditCancel.bind(this)}> 取消 </Button>
                            </div>}
                    >
                        <Form labelAlign={"left"}
                              className="card-form">


                            <FormItem{...formItemLayout}
                                     validateStatus={this.field.getError("title") ? "error" : ""}
                                     help={this.field.getError("title") ? "请输入应用名称" : ""}
                                     required
                                     label="应用名称：">
                                {titleOpr()}

                            </FormItem>

                            <FormItem{...formItemLayout}
                                     label="注册时间：">
                                <div className="form-item-text">{this.state.appBasicData.ctime}</div>
                            </FormItem>

                            <FormItem  {...formItemLayout}
                                       label="应用描述：">
                                {opr()}
                            </FormItem>

                        </Form>
                    </Card>


                    <Card
                        className="url-card"
                        title={"URL信息"}
                        bodyHeight="45%"
                        subTitle={
                            <div className="sub-title">
                                <Button type="primary"
                                        size="small"
                                        className={this.state.urlEditMode == true ? "edit-button hide" : "edit-button"}
                                        onClick={this.urlEdit.bind(this)}>
                                    修改
                                </Button>
                                <Button onClick={this.urlConfirm.bind(this)}
                                        type="primary"
                                        size="small"
                                        className={this.state.urlEditMode == true ? "save-button" : "save-button hide"}
                                >
                                    保存
                                </Button>
                                <Button
                                    size="small"
                                    className={this.state.urlEditMode == true ? "cancel-button" : "cancel-button hide"}
                                    onClick={this.urlEditCancel.bind(this)}>取消</Button>
                            </div>}
                    >
                        <Form labelAlign={"left"}
                              className="card-form">
                            {urlFormRender()}
                        </Form>
                    </Card>


                </div>
            </Loading>)
    }
}