import React, { Component } from 'react';
import {Dialog, Feedback, Field, Table} from "@icedesign/base";
import API from "../../../API";
import Axios from "axios";
import Button from "@icedesign/base/lib/button";
import Pagination from "@icedesign/base/lib/pagination";
import Search from "@icedesign/base/lib/search";



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
            /*此时按当前角色来查找用户，因为只有拥有此角色，用户才能进行相应的操作，才能规定范围*/
            currentData : [{id:1,name:"测试用户1",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"},
                            {id:2,name:"测试用户2",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"},
                             {id:3,name:"测试用户3",email:"xxx@xxx.com",mtime:"xxxx/xx/xx- xx:xx:xx"},
                                {id:22,name:"lzy",email:"552000264@qq.com",mtime:"xxxx/xx/xx- xx:xx:xx"}
                            ],
            currentUserId:0,
            currentRoles:[],
            currentPermissions:[],
            currentUserData:[],
            roleList:[],
            visible:false,
            permissionVisible:false,
            userDataVisible:false,

            isLoading:true,
            permissionIsLoading:true,
            roleIsLoading:true,
            dataIsLoading:true,

            rolePageNo:1,
            rolePageSize:8,
            roleTotalCount:0,

            dataPageNo:1,
            dataPageSize:8,
            dataTotalCount:0,

            permissionPageNo:1,
            permissionPageSize:8,
            permissionTotalCount:0
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
        this.setState({
            currentUserId:id ,
            currentRoles:[],
                roleIsLoading:true,
        })
        let getRoleUrl=API.permission+"/v1/users/roles/{id}"
        let param={userId:id}
        Axios.get(getRoleUrl,{params:(param)}).then(response=>
        {
            this.setState({currentRoles:response.data,roleIsLoading:false})
            console.log(this.state.currentRoles)
            //获取全部角色
            let url=API.permission+"/v1/roles";
            Axios.get(url).then(response=>{
                this.setState({
                    roleList:response.data.pageList,
                    rolePageNo:response.data.pageNo,
                    roleTotalCount:response.data.totalCount})
            })

        })

        this.setState({visible:true
        });
    }

    //用户角色页数变更
    onRoleChange=currentRolePage=>{
        let url=API.permission+"/v1/roles"
        let params={
            pageNo:currentRolePage,
            pageSize:this.state.pageSize
        }
        Axios.get(url,{params:(params)}).then(response=>{
            this.setState({
                roleList:response.data.pageList,
                rolePageNo:response.data.pageNo,
                roleTotalCount:response.data.totalCount
            })
        })
    }

    onPermissionClose=reason=>{
        this.setState({permissionVisible:false})
    }

    onUserDataClose=reason=>{
        this.setState({userDataVisible:false})
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
        let url =API.permission+"/v1/users/rolemap"
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
        this.setState({
            currentPermissions:[],
            currentUserId:id ,
            permissionIsLoading:true})
        let getPermissionUrl=API.permission+"/v1/users/permissions/{id}"
        let param={userId:id}
        Axios.get(getPermissionUrl,{params:(param)}).then(response=>
        {
            this.setState({currentPermissions:response.data ,permissionIsLoading:false})
        })

        this.setState({permissionVisible:true
        });
    }

    //用户功能点页数变更


    //弹出用户数据窗口
    showUserData=id=>{

        //获取当前用户的所有可操作数据
        this.setState({
            currentUserId:id,
            currentUserData:[],
            dataIsLoading:true
        })
        let getDataUrl=API.permission+"/v1/userData/byUser"
        let param={userId:id}
        Axios.get(getDataUrl,{params:(param)}).then(response=>
        {
            this.setState({currentUserData:response.data ,dataIsLoading:false})
        })

        this.setState({userDataVisible:true
        });
    }
    //删除用户数据
    deleteUserData=id=>{
        console.log("用户ID"+id)
        let url=API.permission+"/v1/userData/{id}"
        let param={id:id}
        let getDataUrl=API.permission+"/v1/userData/byUser"
        let userId={userId:this.state.currentUserId}
        Axios.delete(url,{params:(param)}).then(response=>
            //再次获取该用户所有数据

            Axios.get(getDataUrl,{params:(userId)}).then(response=>
            {
                Feedback.toast.success("成功删除！")
                this.setState({currentUserData:response.data})
            })
        )
    }

    //为用户添加角色时的搜索功能
    onSearchChange=key=>{

        let url=API.permission+"/v1/roles"
        let params={
            pageNo:1,
            pageSize:this.state.pageSize,
            key:key
        }
        Axios.get(url,{params:(params)}).then(response=>{
            this.setState({
                roleList:response.data.pageList,
                rolePageNo:response.data.pageNo,
                roleTotalCount:response.data.totalCount
            })
        })

    }

    //查看用户数据时的搜索功能
    onDataSearchChange=key=>{

        let getDataUrl=API.permission+"/v1/userData/byUser"
        let params={userId:this.state.currentUserId,
                    key:key}

        Axios.get(getDataUrl,{params:(params)}).then(response=>{
            this.setState({
                currentUserData:response.data
            })
        })

    }
    render() {
        const dialogStyle= {
            width: "60%" ,height:"70%"
        }
        const showRoles =(value, index, record)=>{
            return(
                <Button
                    type="primary"
                    shape="normal"
                    size="medium"
                    className="button"
                    onClick={this.editUserOpen.bind(this,record.id)}>添加角色</Button>
            )
        }
        const showPermissions =(value, index, record)=>{
            return(
                <Button onClick={this.showPermissions.bind(this,record.id)}>查看功能点</Button>
            )
        }
        const showUserData =(value, index, record)=>{
            return(
                <Button onClick={this.showUserData.bind(this,record.id)}>查看数据</Button>
            )
        }
        const footer=(
            <a onClick={this.onClose} href="javascript:">
                取消
            </a>
        )
        const footer2=(
            <a onClick={this.onPermissionClose} href="javascript:">
                取消
            </a>
        )

        const footer3=(
            <a onClick={this.onUserDataClose} href="javascript:">
                取消
            </a>
        )
        //删除角色按钮
        const deleteRoleMap=(value, index, record)=>{
            return (
                <Button
                    type="primary"
                    shape="warning"
                    size="medium"
                    className="button"
                    onClick={this.removeRole.bind(this, record.id)}>删除</Button>
            );
        }
        //添加角色按钮
        const addRoleMap=(value, index, record)=>{
            return (
                <Button
                    type="primary"
                    shape="normal"
                    size="medium"
                    className="button"
                    onClick={this.addRole.bind(this, record.id)}>添加</Button>
            );
        }

        const deleteUserData=(value, index, record)=>{
            return (
                <Button
                    type="primary"
                    shape="warning"
                    size="medium"
                    className="button"
                    onClick={this.deleteUserData.bind(this, record.id)}>删除</Button>
            );
        }

        return(
            <div>
                <Dialog
                    title="用户关联角色列表"
                    visible={this.state.visible}
                    onClose={this.onClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer}
                    shouldUpdatePosition={true}
                >
                            <h2>已关联角色</h2>
                            <Table
                                hasBorder={false}
                                dataSource={this.state.currentRoles}
                                primaryKey="id"
                                isLoading={this.state.roleIsLoading}>
                                <Table.Column  title="名称" dataIndex="name" />
                                <Table.Column title="删除操作" cell={deleteRoleMap} width="10%" />
                            </Table>



                            <h2>所有角色</h2>
                            <Search
                            onChange={this.onSearchChange.bind(this)}
                            placeholder="输入角色名称搜索"
                            hasIcon={false}
                            autoWidth
                            />
                            <Table
                                hasBorder={false}
                                dataSource={this.state.roleList}
                                primaryKey="id">
                                <Table.Column  title="名称" dataIndex="name" />
                                <Table.Column title="添加" cell={addRoleMap} width="10%" />
                            </Table>
                            <Pagination total={this.state.roleTotalCount}
                                current={this.state.rolePageNo}
                                onChange={this.onRoleChange}
                                pageSize={this.state.rolePageSize}
                                className="pagination" />
                </Dialog>

                <Dialog
                    title="用户功能点表"
                    visible={this.state.permissionVisible}
                    onClose={this.onPermissionClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer2}
                    shouldUpdatePosition={true}>
                    <Table
                        hasBorder={false}
                        isLoading={this.state.permissionIsLoading}
                        dataSource={this.state.currentPermissions}>
                        <Table.Column  title="名称" dataIndex="name" />
                        <Table.Column  title="功能点描述" dataIndex="description" />
                    </Table>

                </Dialog>

                <Dialog
                    title="用户数据表"
                    visible={this.state.userDataVisible}
                    onClose={this.onUserDataClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer3}
                    shouldUpdatePosition={true}>
                    <Search
                        onChange={this.onDataSearchChange.bind(this)}
                        placeholder="输入相关描述进行搜索"
                        hasIcon={false}
                        autoWidth
                    />
                    <Table
                        hasBorder={false}
                        dataSource={this.state.currentUserData}
                        isLoading={this.state.dataIsLoading}>
                        <Table.Column  title="ID" dataIndex="id" width="10%" />
                        <Table.Column  title="规则ID" dataIndex="ruleId" width="10%" />
                        <Table.Column  title="数据描述" dataIndex="description" />
                        <Table.Column  title="作用域参数值" dataIndex="fieldValue" width="15%" />
                        <Table.Column  title="删除操作" cell={deleteUserData}  width="10%" />
                    </Table>

                </Dialog>

                <Table
                    hasBorder={false}
                    isLoading={this.state.isLoading}
                    dataSource={this.state.currentData}>
                    <Table.Column title="用户名称" dataIndex="name"/>
                    <Table.Column title="用户邮箱"   dataIndex="email"/>
                    <Table.Column title="最后修改时间" dataIndex="mtime"/>
                    <Table.Column title="分配角色" cell={showRoles} width="10%"/>
                    <Table.Column title="查看可操作数据" cell={showUserData} width="10%"/>
                    <Table.Column title="查看功能点" cell={showPermissions} width="10%"/>

                </Table>
            </div>
        )
    }

}