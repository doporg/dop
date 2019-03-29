import React, { Component } from 'react';

import Axios from 'axios';
import API from '../../API';
import {Button, Dialog, Feedback, Field, Table} from "@icedesign/base";
import Pagination from "@icedesign/base/lib/pagination";
import Form from "@icedesign/base/lib/form";
import BalloonConfirm from "@icedesign/balloon-confirm";
import Input from "@icedesign/base/lib/input";
import Select, {Option} from "@icedesign/base/lib/select";
import { Link } from 'react-router-dom';
const { Item: FormItem } = Form;
/**
 *  角色管理
 *  @author lizhiying
 *
 *
 * */
let mUserMap=new Map()
export default class Role extends Component {


    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            currentData : [],
            permissionList:[],
            roleSelectList:[],
            currentPermission:[],
            rowSelection: {
                onChange: this.onSelectChange.bind(this),
                onSelectAll: function(selected) {
                    console.log("onSelectAll", selected);
                },
                selectedRowKeys: [],

            },
            permissionVisible:false,
            currentRoleId:0,
            visible:false,
            isLoading:true,
            deleteVisible:false,
            InputInfo:
                {
                    parentId:"",
                    name:"",
                },
            pageNo:1,
            pageSize:8,
            totalCount:0
        };

    }
    //每次访问的刷新
    componentDidMount() {
        this.setState({isLoading:true})
        let url = API.gateway + "/permission-server/v1/roles" ;
        let params=
            {
                pageNo:this.state.pageNo,
                pageSize:this.state.pageSize
            }
        Axios.get(url,{params:(params)}).then((response) => {
            this.setState({
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount})
            Axios.all(this.getUserName(response.data.pageList)).then(()=>{
                console.log(mUserMap)
                response.data.pageList.forEach(item=>{
                    item.muser=mUserMap.get(item.muser)
                })
                this.setState({currentData:response.data.pageList})
                this.setState({isLoading:false})
            })
        }).catch((error)=>{
            // handle error
            console.log(error);
        }).then(()=>{
            // always executed
        });
    }
    //翻页
    onChange=currentPage=> {
        this.setState({isLoading:true})
        let url = API.gateway + "/permission-server/v1/roles" ;
        let params=
            {
                pageNo:currentPage,
                pageSize:this.state.pageSize
            }
        // console.log(params)
        Axios.get(url,{params:(params)}).then((response) => {
            // handle success
            this.setState({
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount}
            );
            Axios.all(this.getUserName(response.data.pageList)).then(()=>{
                console.log(mUserMap)
                response.data.pageList.forEach(item=>{
                    item.muser=mUserMap.get(item.muser)
                })
                this.setState({currentData:response.data.pageList})
                this.setState({isLoading:false})
            })

        }).catch((error)=>{
            // handle error
            console.log(error);
        });
        console.log(this.state.pageNo)
    }
    //重置
    handleReset(e) {
        e.preventDefault();
        this.field.reset();
    }
    //提交
    handleSubmit(e)
    {
        e.preventDefault();
        this.field.validate((errors, values)=>{
            if(errors){
                console.log("Errors in form!!!");
                return;}
            console.log(values)
            let idList=this.state.rowSelection.selectedRowKeys
            console.log(idList)

            let createRoleUrl=API.gateway+"/permission-server/v1/roles"
            let byNameUrl=API.gateway+"/permission-server/v1/roles/byName"

            let RoleParam={parentId: values.parentId,name:values.name}
            let byNameParams={name:values.name}

            //先检测是否重复
            Axios.get(byNameUrl,{params:(byNameParams)}
            ).then((response) => {
                // handle success
                console.log("监测重复返回的东西："+response.data.name);
                if(response.data.name==values.name)
                {
                    Feedback.toast.error("角色名称重复！")
                }
                else {
                    //不重复 则关闭窗口 开始插入数据
                    this.setState({
                        visible: false
                    });
                    //插入角色
                    Axios.post(createRoleUrl, {},{params:(RoleParam)}
                    )
                        .then((response)=>{
                            console.log("创建角色成功")
                            console.log(response.data)
                            this.componentDidMount()
                            //插入关联关系
                            let createMapUrl=API.gateway+"/permission-server/v1/roles/permissionmap"
                            idList.forEach(permissionid=>{
                                let param={
                                    roleId:response.data,
                                    permissionId:permissionid}
                                Axios.post(createMapUrl,{},{params:(param)}).then(
                                    (response)=>{
                                        console.log("插入关联关系成功")
                                    }
                                )
                            })
                        }).catch((error)=> {
                        console.log(error);
                    });

                }
            })
        })
    }

    //通过get请求中的userId获取username的函数
    getUserName=(data)=>{
        let axiosList=[]
        let muserSet=[]
        data.forEach(item=>{
            muserSet.push(item.muser)
        })
        muserSet = new Set(muserSet)
        muserSet.forEach(item=>{
            let url = API.gateway + "/user-server/v1/users/"+item
            axiosList.push(Axios.get(url).then((response) => {
                mUserMap.set(item,response.data.name)
            }))
        })
        return axiosList
    }
    //弹出创建角色窗
    onOpen = () => {

        let roleUrl=API.gateway+"/permission-server/v1/roles/roles"
        Axios.get(roleUrl).then(response=>{

            let tmpList=[]
            response.data.forEach(item=>{
                tmpList.push({label:item.name,value:item.id
            })
            this.setState({roleSelectList:tmpList})
            let url=API.gateway+"/permission-server/v1/roles/permissions";
            Axios.get(url).then(response=>{

                this.setState({permissionList:response.data})
            }).catch(error=>{
                console.log(error)
            })
        })

        this.setState({
            visible: true
        });
    })
    }
    //关闭创建角色弹窗
    onClose = reason => {
        console.log(reason);
        this.setState({
            visible: false
        });
    };

    //关闭创建角色弹窗
    onPermissionClose = reason => {
        console.log(reason);
        this.setState({
            permissionVisible: false
        });
    };

    //勾选功能点
    onSelectChange(permissionids) {
        let { rowSelection } = this.state;
        rowSelection.selectedRowKeys = permissionids;
        console.log("onSelectChange", permissionids);
        this.setState({ rowSelection });
    }

    //弹出角色功能点窗户
    editRoleOpen=id=>{

        console.log("ID是"+id)
            //获取当前角色功能点
        this.setState({currentRoleId:id})
            let getPermissionUrl=API.gateway+"/permission-server/v1/roles/permissions/{id}"
            let param={roleId:id}
            Axios.get(getPermissionUrl,{params:(param)}).then(response=>
            {
                this.setState({currentPermission:response.data})
                console.log(this.state.currentPermission)
                //获取全部功能点
                let url=API.gateway+"/permission-server/v1/roles/permissions";
                Axios.get(url).then(response=>{
                    this.setState({permissionList:response.data})
                })

            })

        this.setState({
            permissionVisible: true
        });
    }


    //确认删除操作以及
    //删除功能点
    onConfirm = id => {

        const { dataSource } = this.state;
        let url = API.gateway + "/permission-server/v1/roles/{id}" ;
        let params= {id:id}
        Axios.delete(url,{params:(params)}
        )
            .then((response)=>{
                Feedback.toast.success('成功删除！')
                this.onChange(this.state.currentPage)
            }).catch((error)=> {
            console.log(error);
        });
    };
    //取消删除操作
    onCancel = () => {
        console.log('取消删除');
        Feedback.toast.error('删除已取消！')

    }

    removePermission=id=>{
        console.log("功能点ID"+id)
        let url=API.gateway+"/permission-server/v1/roles/permissions"
        let param={roleId:this.state.currentRoleId,
                    permissionId:id}
        let getPermissionUrl=API.gateway+"/permission-server/v1/roles/permissions/{id}"
        let roleId={roleId:this.state.currentRoleId}
         Axios.delete(url,{params:(param)}).then(response=>
             //再次获取该角色功能点

            Axios.get(getPermissionUrl,{params:(roleId)}).then(response=>
        {
            Feedback.toast.success("成功删除！")
            this.setState({currentPermission:response.data})
        })
         )
    }
    addPermission=id=>{
        let url =API.gateway+"/permission-server/v1/roles/permissionmap"
        let param={roleId:this.state.currentRoleId,
            permissionId:id}
        let getPermissionUrl=API.gateway+"/permission-server/v1/roles/permissions/{id}"
        let roleId={roleId:this.state.currentRoleId}
        Axios.post(url,{},{params:(param)}).then(response=>
            Axios.get(getPermissionUrl,{params:(roleId)}).then(response1=>
            {
                Feedback.toast.success("成功添加！")
                console.log(response)
                this.setState({currentPermission:response1.data})
            })
        )
    }
    render() {
        const { init } = this.field;
        const dialogStyle= {
                width: "60%" ,height:"7000"
            }
        //form样式定义
        const formItemLayout = {
            labelCol: {
                span: 6
            },
            wrapperCol: {
                span: 14
            }
        };
        //窗口按钮定义
        const footer = (
            <a onClick={this.onClose} href="javascript:">
                取消
            </a>
        );
        const footer2=(
            <a onClick={this.onPermissionClose} href="javascript:;">
                取消
            </a>
        )

        const showPermission =(value, index, record)=>{
            return(
                    <button onClick={this.editRoleOpen.bind(this,record.id)}>编辑角色</button>
            )
        }

        //删除操作定义
        const renderDelete = (value, index, record) => {
            return (
                <BalloonConfirm
                    onConfirm={this.onConfirm.bind(this, record.id)}
                    onCancel={this.onCancel}
                    title="您真的要删除吗？"
                >
                    <button>删除</button>
                </BalloonConfirm>

            );
        }
        //删除角色功能点对应按钮
        const deletePermissionMap=(value, index, record)=>{
            return (
                <button onClick={this.removePermission.bind(this, record.id)}>删除</button>
            );
        }
        //添加角色功能点对应按钮
        const addPermissionMap=(value, index, record)=>{
            return (
                <button onClick={this.addPermission.bind(this, record.id)}>添加</button>
            );
        }

        return(

            <div>
                <Button type="primary" onClick={this.onOpen} >创建新角色</Button>

                <Link to='/roles/userwithrole'>
                    <Button type="primary" >角色分配</Button>
                </Link>

                <Dialog
                    title="创建角色"
                    visible={this.state.visible}
                    onClose={this.onClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer}
                    shouldUpdatePosition={true}>

                    <Form field={this.field}>
                        <FormItem
                            label="角色名称："
                            {...formItemLayout}
                            hasFeedback
                        >
                            <Input
                                maxLength={10}
                                hasLimitHint
                                placeholder="请输入名称"
                                {...init("name", {
                                    rules: [
                                        { required: true, min: 1, message: "名称不能为空！" },
                                    ]
                                })}
                            />
                        </FormItem>

                        <FormItem label="父角色：" {...formItemLayout} required>
                            <Select dataSource={this.state.roleSelectList} style={{width:200}}  {...init("parentId")}>

                            </Select>
                        </FormItem>

                        <FormItem label="功能点：" {...formItemLayout} required>
                            <Table
                                dataSource={this.state.permissionList}
                                primaryKey="id"
                                isTree
                                rowSelection={this.state.rowSelection}
                            >
                                <Table.Column title="名称" dataIndex="name" />
                                <Table.Column title="功能描述" dataIndex="description" />
                            </Table>
                        </FormItem>

                        <FormItem wrapperCol={{ offset: 6 }}>
                            <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                                确定
                            </Button>
                            &nbsp;&nbsp;&nbsp;
                            <Button onClick={this.handleReset.bind(this)}>重置</Button>
                        </FormItem>

                    </Form>
                </Dialog>


                <Dialog
                    title="角色功能点列表"
                    visible={this.state.permissionVisible}
                    onClose={this.onPermissionClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer2}
                    shouldUpdatePosition={true}>
                        <Form>
                            <FormItem>
                                <h2>已有功能点</h2>
                                    <Table
                                        dataSource={this.state.currentPermission}
                                        primaryKey="id">
                                        <Table.Column width ='20%' title="名称" dataIndex="name" />
                                        <Table.Column title="功能描述" dataIndex="description" />
                                        <Table.Column title="删除操作" cell={deletePermissionMap} width="10%" />
                                    </Table>
                            </FormItem>

                            <FormItem>
                                <h2>所有功能点</h2>
                                        <Table
                                            dataSource={this.state.permissionList}
                                            primaryKey="id">
                                                <Table.Column width ='20%' title="名称" dataIndex="name" />
                                                <Table.Column title="功能描述" dataIndex="description" />
                                                <Table.Column title="添加" cell={addPermissionMap} width="10%" />
                                        </Table>
                            </FormItem>

                        </Form>
                </Dialog>

                <Table
                    isLoading={this.state.isLoading}
                    dataSource={this.state.currentData}>
                    <Table.Column title="角色ID" dataIndex="id"/>
                    <Table.Column title="角色名称" dataIndex="name"/>
                    <Table.Column title="创建人"   dataIndex="muser"/>
                    <Table.Column title="创建时间" dataIndex="mtime"/>
                    <Table.Column title="编辑角色" cell={showPermission} width="10%"/>
                    <Table.Column title="删除操作" cell={renderDelete} width="10%" />


                </Table>
                <Pagination total={this.state.totalCount}
                            current={this.state.pageNo}
                            onChange={this.onChange}
                            pageSize={this.state.pageSize
                            }
                            className="page-demo" />
            </div>
        )
    }
}
