import React, { Component } from 'react';
import {Dialog, Feedback, Field, Table} from "@icedesign/base";
import API from "../../API";
import Axios from "axios";
import Form from "../Role";

/**
 *  用户关联角色管理
 *  @author lizhiying
 *
 *  @since 3019.3.7
 *
 *
 * */
export default class UserRoleMapping extends Component {
    constructor(props) {
        super(props);
        this.state = {
            Dialogstyle: {
                width: "60%" ,height:"7000"
            },
            currentData : [{id:1,name:"测试用户1",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"},
                            {id:2,name:"测试用户2",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"},
                             {id:3,name:"测试用户3",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"}
                            ],
            currentUserId:0,
            currentRoles:[],
            currentPermissions:[],
            roleList:[],
            visible:false,
            permissionvisible:false,
            isLoading:true,

            pageNo:1,
            pageSize:8,
            totalCount:0
        };

    };

    //每次访问的刷新，查询当前用户的信息
    componentDidMount() {
        this.setState({isLoading:false})
    }

    //弹出角色功能点窗户
    editUserOpen=id=>{

        console.log(id)
        //获取当前用户角色
        this.setState({currentUserId:id})
        let getRoleUrl=API.permission+"/v1/users/roles/{id}"
        let param={userId:id}
        Axios.get(getRoleUrl,{params:(param)}).then(response=>
        {
            this.setState({currentRoles:response.data})
            console.log(this.state.currentRoles)
            //获取全部角色
            let url=API.permission+"/v1/roles/roles";
            Axios.get(url).then(response=>{
                this.setState({roleList:response.data})
            })

        })

        this.setState({visible:true
        });
    }

    onpermissionClose=reason=>{
        this.setState({permissionvisible:false})
    }

    //关闭用户角色弹窗
    onClose = reason => {
        console.log(reason);
        this.setState({
            visible: false
        });
    };
    removeRole=id=>{
        console.log("用户ID"+id)
        let url=API.permission+"/v1/users/roles"
        let param={userId:this.state.currentUserId,
            roleId:id}
        let getRoleUrl=API.permission+"/v1/users/roles/{id}"
        let userId={userId:this.state.currentUserId}
        Axios.delete(url,{params:(param)}).then(response=>
            //再次获取该用户角色

            Axios.get(getRoleUrl,{params:(userId)}).then(response=>
            {
                Feedback.toast.success("成功删除！")
                this.setState({currentRoles:response.data})
            })
        )
    }
    addRole=id=>{
        let url =API.gateway+"/permission-server/v1/users/rolemap"
        let param={userId:this.state.currentUserId,
            roleId:id}
        let getRoleUrl=API.permission+"/v1/users/roles/{id}"
        let userId={userId:this.state.currentUserId}
        Axios.post(url,{},{params:(param)}).then(response=>
            Axios.get(getRoleUrl,{params:(userId)}).then(response1=>
            {
                Feedback.toast.success("成功添加！")

                this.setState({currentRoles:response1.data})
            })
        )
    }

    //弹出用户功能点窗户
    showPermissions=id=>{

        //获取当前用户的功能点
        this.setState({currentUserId:id})
        let getPermissionUrl=API.permission+"/v1/users/permissions/{id}"
        let param={userId:id}
        Axios.get(getPermissionUrl,{params:(param)}).then(response=>
        {
            this.setState({currentPermissions:response.data})
        })

        this.setState({permissionvisible:true
        });
    }

    render() {
        const showroles =(value, index, record)=>{
            return(
                <button onClick={this.editUserOpen.bind(this,record.id)}>添加角色</button>
            )
        }
        const showpermissions =(value, index, record)=>{
            return(
                <button onClick={this.showPermissions.bind(this,record.id)}>查看功能点</button>
            )
        }
        const footer=(
            <a onClick={this.onClose} href="javascript:;">
                取消
            </a>
        )
        const footer2=(
            <a onClick={this.onpermissionClose} href="javascript:;">
                取消
            </a>
        )
        //删除角色按钮
        const deleterolemap=(value, index, record)=>{
            return (
                <button onClick={this.removeRole.bind(this, record.id)}>删除</button>
            );
        }
        //添加角色按钮
        const addrolemap=(value, index, record)=>{
            return (
                <button onClick={this.addRole.bind(this, record.id)}>添加</button>
            );
        }

        return(
            <div>
                <Dialog
                    title="用户关联角色列表"
                    visible={this.state.visible}
                    onClose={this.onClose}
                    style={this.state.Dialogstyle}
                    minMargin={5}
                    footer={footer}
                >
                            <h2>已关联角色</h2>
                            <Table
                                dataSource={this.state.currentRoles}
                                primaryKey="id">
                                <Table.Column  title="名称" dataIndex="name" />
                                <Table.Column title="删除操作" cell={deleterolemap} width="10%" />
                            </Table>



                            <h2>所有角色</h2>
                            <Table
                                dataSource={this.state.roleList}
                                primaryKey="id">
                                <Table.Column  title="名称" dataIndex="name" />
                                <Table.Column title="添加" cell={addrolemap} width="10%" />
                            </Table>
                </Dialog>

                <Dialog
                    title="用户功能点表"
                    visible={this.state.permissionvisible}
                    onClose={this.onpermissionClose}
                    style={this.state.Dialogstyle}
                    minMargin={5}
                    footer={footer2}>
                    <Table
                        dataSource={this.state.currentPermissions}>
                        <Table.Column  title="名称" dataIndex="name" />
                        <Table.Column  title="功能点描述" dataIndex="description" />
                    </Table>

                </Dialog>

                <Table
                    isLoading={this.state.isLoading}
                    dataSource={this.state.currentData}>
                    <Table.Column title="用户名称" dataIndex="name"/>
                    <Table.Column title="用户邮箱"   dataIndex="email"/>
                    <Table.Column title="最后修改时间" dataIndex="mtime"/>
                    <Table.Column title="分配角色" cell={showroles}width="10%"/>
                    <Table.Column title="查看功能点" cell={showpermissions}width="10%"/>

                </Table>
            </div>
        )
    }

}