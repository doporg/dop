import React, {Component} from 'react';
import {Breadcrumb, Button, Card, Dialog, Feedback, Icon, Loading, Table} from '@icedesign/base';
import TopBar from "./topbar"
import Axios from "axios";
import API from "../../../../API";
import "./ProjectBasicInfo.scss"

/**
 * 项目基本信息页面
 * @author Bowen
 */
const Toast = Feedback.toast;
export default class ProjectBasicInfo extends Component {
    static displayName = 'ProjectBasicInfo';

    constructor(props) {
        super(props);
        this.state = {
            //在应用列表点击应用ID跳转到该页面，通过正则表达式解析应用id
            projectId: props.projectId,
            projectData: undefined,
            loading: true,
            userData: [],
            visible: false,
            memberData: []
        }

    }

    //加载应用基本信息
    componentDidMount() {
        this.getData()
    }

    popupConfirm(id) {

        console.log(id)
        Dialog.confirm({
            content: "你确定要删除该成员吗？",
            title: "确认删除",
            onOk: this.onDelete.bind(this, id)
        });
    }

    onDelete(id) {
        let deleteUrl = API.application + "/project/" + this.state.projectId + "/members"
        Axios.delete(deleteUrl, {
            params: {
                userId: id,
                organizationId: 123,

            }
        })
            .then((response) => {
                    Toast.success("删除成功")
                    this.getData()
                }
            )
            .catch((response) => {
                console.log(response)
            })

    }

    nameRender(value, index, record) {
        console.log("nameRender", value, index, record)
        return (
            <div className="name-render-div">{value} <Icon onClick={this.popupConfirm.bind(this, record.id)}
                                                           type="ashbin" className="name-render-delete-icon"/></div>
        )

    }

    getMemberData() {
        this.setState({
            loading: true
        })
        let _this = this
        let url = API.application + "/project/" + this.state.projectId + "/members"
        Axios.get(url, {
            params: {
                organizationId: "1"
            }
        })
            .then((response) => {
                console.log(response)
                this.setState({
                    memberData: response.data,
                    loading: false
                })
            })
            .catch((reason => {
                console.log(reason)
            }))
    }

    getData() {
        this.setState({
            loading: true
        })
        let _this = this
        let url = API.application + '/project/' + this.state.projectId;

        //获取应用基本信息
        Axios.get(url)
            .then(function (response) {
                console.log(response)
                let projectData = response.data
                //获取用户基本信息
                let userUrl = API.application + "/userInfo";
                Axios.get(userUrl, {
                    params: {
                        userId: projectData.cuser
                    }
                })
                    .then(function (response) {
                        console.log(response)
                        _this.setState({
                            projectData: projectData,
                            userData: response.data,
                            loading: false
                        })

                    })
                    .catch((response) => {
                        console.log(response)
                    })


            })
            .catch((response) => {
                    console.log(response)
                }
            )


    }

    manageMember() {
        this.getMemberData()
        this.setState({
            visible: true
        });

    }

    onClose = () => {
        this.setState({
            visible: false
        });
    };

    deleteRender(value) {
        console.log(value);
    }

    addMember() {

    }


    render() {
        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                <div>
                    <TopBar
                        extraBefore={<Breadcrumb>
                            <Breadcrumb.Item link="#/project">所有项目</Breadcrumb.Item>
                            <Breadcrumb.Item
                                link={"#/projectDetail?projectId=" + this.state.projectId}>{"项目：" + this.state.projectId}</Breadcrumb.Item>
                        </Breadcrumb>}/>
                    <Card
                        className="user-card"
                        title={this.state.userData.name}
                        bodyHeight="100%"
                        subTitle="应用拥有人"
                        extra={<a href="">转交应用&gt;</a>}
                    >
                        <img alt="avatar"
                             src={this.state.userData.avatarURL === "" ? "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQBAMAAAB8P++eAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAYUExURcHBwb+/v7+/v76+vujo6OHh4cnJydTU1IOqnXYAAAADdFJOUxPppyMYpxkAAAD6SURBVEjH7dfbDYIwFAbguoHRCYwTKLcBOIUBaHQAIAxQwvwSEQpyaH/FFxP+5y89vacV4uQBOQix86DsxRmDV3HE4EV4YDa4QQRWSjYILKnNzQ0jekY7Yd3B1AVDeiV3wKCHsQPWPUwdkIbYYWSgtsLAwMwKfQNjFCZWWPwBhEcNz+NoZfLfrLXZPYkD+gtd/H6H97UT5+EK0FPY1ZbABaDYygysuTEvtqg9sI9AiyV/o8xgRNj0DLtHaiuszOahxgJLGueeL8Gpa8vnPHx30yEZGKo5lBwMiEnGwIKDKQMVB+UaSGzWwO2psMGPIfxgh78A8KcC/aY8ACmMo3JtJ3ljAAAAAElFTkSuQmCC" : this.state.userData.avatarURL}
                             className="avatar"/>
                        <span>
            <Button onClick={this.manageMember.bind(this)} type="primary">
            管理成员
            </Button>
            <Dialog
                visible={this.state.visible}
                onOk={this.onOk}
                onCancel={this.onClose}
                onClose={this.onClose}
                className="dialog"
            >
            <div>
            <h2>成员管理</h2>
            <div>
            <Button onClick={this.addMember.bind(this)} type="primary">
        添加成员
        </Button>
            </div>
            <Table dataSource={this.state.memberData}>
        <Table.Column cell={this.idRender}
                      title="成员ID"
                      dataIndex="id"/>

        <Table.Column title="成员名称"
                      dataIndex="name"
                      cell={this.nameRender.bind(this)}/>


        </Table>
        </div>
        </Dialog>
        </span>
                    </Card>
                </div>
            </Loading>
        )
    }
}