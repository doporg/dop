
import React, {Component} from 'react';
import {Card, Dialog, Loading} from '@icedesign/base';
import Axios from "axios";
import API from "../../../../API";
import {
    Form,
    Input,
    Button,
    Field,
    Feedback
} from "@icedesign/base";
import ApplicationVariable from "../ApplicationVariable";


const Toast = Feedback.toast;
const FormItem = Form.Item;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
const style = {
    padding: "2%",
    background: "#F7F8FA",
    marginLeft: "2%",
    width: "58%"
};
const style1 = {
    padding: "2%",
    background: "#F7F8FA",
    marginTop: "2%",
    width: "100%"
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
        this.state = {
            urlEditMode: false,
            basicEditMode: false,
            appId: props.appId,
            appBasicData: [],
            userData: [],
            loading: true
        }
    }

    //加载应用基本信息
    componentDidMount() {
        this.setState({
            loading: true
        })
        let _this = this;
        let url = API.gateway + '/application-server/app/' + _this.state.appId + "/urlInfo";

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
            this.setState({
                loading: true
            })

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let url = API.gateway + '/application-server/app/' + this.state.appId;
                Axios.put(url, {}, {
                        params: {
                            description: this.field.getValue('description')
                        }
                    }
                )
                    .then(function (response) {


                        //提交完成后刷新当前页面
                        let url = API.gateway + '/application-server/app/' + _this.state.appId + '/urlInfo';
                        Axios.get(url)
                            .then(function (response) {
                                console.log(response)
                                _this.setState({
                                    appBasicData: response.data,
                                    basicEditMode: false,
                                    loading: false
                                })
                            })
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
        this.field.validate((errors, values) => {

            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let url = API.gateway + '/application-server/app/' + this.state.appId + "/urlInfo"
                this.setState({
                    loading: true
                })
                Axios.put(url, {}, {
                        params: {
                            warehouseUrl: this.field.getValue("warehouseUrl"),
                            productionDbUrl: this.field.getValue("productionDbUrl"),
                            testDbUrl: this.field.getValue("testDbUrl"),
                            productionDomain: this.field.getValue("productionDomain"),
                            testDomain: this.field.getValue("testDomain"),
                        }
                    }
                )
                    .then(function (response) {

                        //提交完成后刷新当前页面
                        let url = API.gateway + '/application-server/app/' + _this.state.appId + "/urlInfo";
                        Axios.get(url)
                            .then(function (response) {
                                console.log(response)
                                _this.setState({
                                    appBasicData: response.data,
                                    urlEditMode: false,
                                    loading: false
                                })
                            })
                        Toast.success("更新成功！")
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            }
        });
    }

    render() {
        const {init, getValue} = this.field;
        const opr = () => {
            if (!this.state.basicEditMode) {
                return <span>{this.state.appBasicData.description}</span>
            } else {
                return <Input defaultValue={this.state.appBasicData.description} {...init('description')}
                              placeholder="应用描述"/>
            }
        }

        const urlFormRender = () => {
            console.log((this.state.appBasicData))
            if (this.state.urlEditMode) {
                return (


                    <div>

                    <FormItem{...formItemLayout} label="Git仓库地址：">
                        <Input defaultValue={this.state.appBasicData.warehouseUrl} {...init('warehouseUrl')}
                               placeholder="Git仓库地址"/>
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
                        <Input defaultValue={this.state.appBasicData.productionDomain}  {...init('productionDomain')}
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
                        <span>{this.state.appBasicData.warehouseUrl}</span>
                    </FormItem>

                    <FormItem{...formItemLayout} label="开发数据库地址：">
                        <span>{this.state.appBasicData.productionDbUrl}</span>
                    </FormItem>

                    <FormItem{...formItemLayout} label="测试数据库地址：">
                        <span>{this.state.appBasicData.testDbUrl}</span>
                    </FormItem>

                    <FormItem{...formItemLayout} label="开发域名：">
                        <span>{this.state.appBasicData.productionDomain}</span>
                    </FormItem>

                    <FormItem{...formItemLayout} label="测试域名：">
                        <span>{this.state.appBasicData.testDomain}</span>
                    </FormItem>
                    </div>
                )
            }
        }


        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF"
            >

                <div style={{
                    margin: "0 auto",
                    width: "70%",
                    display: "flex",
                    flexWrap: "wrap",
                    justifyContent: "flex-start"
                }}
                >
            <Card
                style={{width: "40%", height: "40%"}}
                title={this.state.userData.name}
                subTitle="应用拥有人"
                extra={<a href="#">转交应用&gt;</a>}
            >
                <img
                    src={this.state.userData.avatarURL == "" ? "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQBAMAAAB8P++eAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAYUExURcHBwb+/v7+/v76+vujo6OHh4cnJydTU1IOqnXYAAAADdFJOUxPppyMYpxkAAAD6SURBVEjH7dfbDYIwFAbguoHRCYwTKLcBOIUBaHQAIAxQwvwSEQpyaH/FFxP+5y89vacV4uQBOQix86DsxRmDV3HE4EV4YDa4QQRWSjYILKnNzQ0jekY7Yd3B1AVDeiV3wKCHsQPWPUwdkIbYYWSgtsLAwMwKfQNjFCZWWPwBhEcNz+NoZfLfrLXZPYkD+gtd/H6H97UT5+EK0FPY1ZbABaDYygysuTEvtqg9sI9AiyV/o8xgRNj0DLtHaiuszOahxgJLGueeL8Gpa8vnPHx30yEZGKo5lBwMiEnGwIKDKQMVB+UaSGzWwO2psMGPIfxgh78A8KcC/aY8ACmMo3JtJ3ljAAAAAElFTkSuQmCC" : this.state.userData.avatarURL}
                    style={styles.avatar}/>
            </Card>


                    <Form labelAlign={"left"}
                  style={style}>

                <div>基本信息

                    <Button type="primary"
                            style={this.state.basicEditMode == true ? {display: "None"} : {}}
                            onClick={this.basicEdit.bind(this)}>
                        修改
                    </Button>

                </div>

                    <FormItem{...formItemLayout}
                             label="应用名称：">
                        <div  {...init('appTitle')}
                              placeholder="应用名称">{this.state.appBasicData.title}</div>
                    </FormItem>

                    <FormItem{...formItemLayout}
                             label="注册时间：">
                        <div>{this.state.appBasicData.ctime}</div>
                    </FormItem>

                    <FormItem  {...formItemLayout}
                               label="应用描述：">
                        {opr()}
                    </FormItem>

                        <div style={this.state.basicEditMode == false ? {display: "None"} : {}}>
                        <Button onClick={this.basicConfirm.bind(this)}
                                type="primary"
                                style={{marginRight: "5px"}}>
                            保存
                        </Button>

                        < Button onClick={this.basicEditCancel.bind(this)}> 取消 </Button>
                    </div>
                </Form>

            <Form labelAlign={"left"}
                  style={style1}>
                <div>URL信息
                <Button type="primary"
                        style={this.state.urlEditMode == true ? {display: "None"} : {}}
                        onClick={this.urlEdit.bind(this)}>
                    修改
                </Button>
                </div>
                {urlFormRender()}
                <div style={this.state.urlEditMode == false ? {display: "None"} : {}}>
                    <Button onClick={this.urlConfirm.bind(this)}
                            type="primary"
                            style={{marginRight: "5px"}}>
                        保存
                    </Button>
                    <Button onClick={this.urlEditCancel.bind(this)}>取消</Button>
                </div>
            </Form>


        </div>
            </Loading>)
    }
}
const styles = {
    avatar: {
        maxWidth: "none",
        height: "80px",
        opacity: "1",
        width: "80px",
        margiLeft: "30px",
        marginTop: "30px",
    },
    basicUrlInput: {
        visible: "false"
    }


};