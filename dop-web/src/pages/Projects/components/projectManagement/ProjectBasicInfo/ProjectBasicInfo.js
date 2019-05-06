import React, {Component} from 'react';
import {Breadcrumb, Button, Card, Dialog, Feedback, Icon, Loading, Table} from '@icedesign/base';
import TopBar from "./topbar"
import Axios from "axios";
import API from "../../../../API";
import "./ProjectBasicInfo.scss"
import AddMemberList from "../AddMemberList/AddMemberList"
import {injectIntl} from "react-intl";

/**
 * 项目基本信息页面
 * @author Bowen
 */
const Toast = Feedback.toast;

class ProjectBasicInfo extends Component {
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
            addMemberListVisible: false,
            memberData: [],
            memberDataLoading: false
        }

    }

    //加载应用基本信息
    componentDidMount() {
        this.getData()
        // this.getMemberData()
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
        let _this = this
        let deleteUrl = API.application + "/project/" + this.state.projectId + "/members"
        Axios.delete(deleteUrl, {
            params: {
                userId: id,
                organizationId: 123,

            }
        })
            .then((response) => {
                    Toast.success("删除成功")
                _this.getData()
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
            memberDataLoading: true
        })
        let _this = this
        let url = API.application + "/project/" + this.state.projectId + "/members"
        Axios.get(url, {
            params: {
                organizationId: "1"
            }
        })
            .then((response) => {
                console.log("then", response)
                _this.setState({
                    memberData: response.data,
                    memberDataLoading: false,
                    addMemberListVisible: false
                })
            })
            .catch((response) => {
                console.log("catch", response)
                _this.setState({
                    memberDataLoading: false,
                    addMemberListVisible: false
                })
            })
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
                        _this.setState({
                                loading: false
                            }
                        )
                    })


            })
            .catch((response) => {
                    console.log(response)
                _this.setState({
                    loading: false
                })
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
        this.setState({
            addMemberListVisible: true
        })

    }


    render() {
        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                <div>
                    <TopBar
                        extraBefore={<Breadcrumb>
                            <Breadcrumb.Item
                                link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                            <Breadcrumb.Item
                                link={"#/projectDetail?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>
                        </Breadcrumb>}/>
                    <Card
                        className="user-card"
                        title={this.state.userData.name}
                        bodyHeight="100%"
                        subTitle={this.props.intl.messages["projects.text.projectOwner"]}
                        extra={<a href="">{this.props.intl.messages["projects.text.handOverApplication"]}&gt;</a>}
                    >
                        <img alt="avatar"
                             src={this.state.userData.avatarURL === "" ? "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQBAMAAAB8P++eAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAYUExURcHBwb+/v7+/v76+vujo6OHh4cnJydTU1IOqnXYAAAADdFJOUxPppyMYpxkAAAD6SURBVEjH7dfbDYIwFAbguoHRCYwTKLcBOIUBaHQAIAxQwvwSEQpyaH/FFxP+5y89vacV4uQBOQix86DsxRmDV3HE4EV4YDa4QQRWSjYILKnNzQ0jekY7Yd3B1AVDeiV3wKCHsQPWPUwdkIbYYWSgtsLAwMwKfQNjFCZWWPwBhEcNz+NoZfLfrLXZPYkD+gtd/H6H97UT5+EK0FPY1ZbABaDYygysuTEvtqg9sI9AiyV/o8xgRNj0DLtHaiuszOahxgJLGueeL8Gpa8vnPHx30yEZGKo5lBwMiEnGwIKDKQMVB+UaSGzWwO2psMGPIfxgh78A8KcC/aY8ACmMo3JtJ3ljAAAAAElFTkSuQmCC" : this.state.userData.avatarURL}
                             className="avatar"/>
                        <span>
            <Button onClick={this.manageMember.bind(this)} type="primary">
            {this.props.intl.messages["projects.text.manageMembers"]}
            </Button>
            <Dialog
                visible={this.state.visible}
                onOk={this.onClose}
                onCancel={this.onClose}
                onClose={this.onClose}
                className="dialog"
            >
            <div>
            <h2> {this.props.intl.messages["projects.text.memberManagement"]}</h2>
            <div>
            <Button onClick={this.addMember.bind(this)} type="primary"
                    disabled={this.state.memberDataLoading}>
        {this.props.intl.messages["projects.text.addMember"]}
        </Button>
                <AddMemberList
                    getMemberData={this.getMemberData.bind(this)}
                    memberList={this.state.memberData}
                    visible={this.state.addMemberListVisible}
                    projectId={this.state.projectId}/>
            </div>
            <Table
                isLoading={this.state.memberDataLoading}
                dataSource={this.state.memberData}>
        <Table.Column cell={this.idRender}
                      title={this.props.intl.messages['projects.text.dialog.memberId']}
                      dataIndex="id"/>

        <Table.Column title={this.props.intl.messages['projects.text.dialog.memberName']}
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

export default injectIntl(ProjectBasicInfo)